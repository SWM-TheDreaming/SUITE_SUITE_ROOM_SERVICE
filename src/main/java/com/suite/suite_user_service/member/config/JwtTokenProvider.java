package com.suite.suite_user_service.member.config;


import com.suite.suite_user_service.member.dto.Token;
import com.suite.suite_user_service.member.entity.RefreshToken;
import com.suite.suite_user_service.member.handler.CustomException;
import com.suite.suite_user_service.member.handler.StatusCode;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final ConfigUtil configUtil;
    private String accessSecretKey;
    private String refreshSecretKey;
    //유효시간 7일
    private long accessTokenValidTime = 7 * 24 * 60 * 60 * 1000L;
    //유효시간 31일
    private long refreshTokenValidTime = 30 * 24 * 60 * 60 * 1000L;

    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        accessSecretKey = Base64.getEncoder().encodeToString(configUtil.getProperty("jwt.access.key").getBytes());
        refreshSecretKey = Base64.getEncoder().encodeToString(configUtil.getProperty("jwt.refresh.key").getBytes());
    }

    // 토큰 생성
    public Token createToken(String userPk) {  // userPK = email
        Claims claims = Jwts.claims().setSubject(userPk); // JWT payload 에 저장되는 정보단위
        Date now = new Date();
        String accessToken = getToken(claims, now, accessTokenValidTime, accessSecretKey);
        String refreshToken = getToken(claims, now, refreshTokenValidTime, refreshSecretKey);
        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .key(userPk).build();
    }

    private String getToken(Claims claims, Date currentTime, long tokenValidTime, String secretKey) {
        return Jwts.builder()
                .setClaims(claims) //정보 저장
                .setIssuedAt(currentTime)  //토큰 발행시간 정보
                .setExpiration(new Date(currentTime.getTime() + tokenValidTime)) //Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  //암호화 알고리즘
                .compact();
    }

    // 인증 정보 조회
    public Authentication getAuthentication(ServletRequest request, String token) {
        String available_token = extractToken(token);
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(available_token));
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (NullPointerException e) {
            request.setAttribute("exception", "ForbiddenException");
        }
        return null;
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
        return Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값 가져오기
    public String getAccessToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring("Bearer ".length());
    }

    private void validationAuthorizationHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException();
        }
    }

    //토큰의 유효성 검사
    public boolean validateToken(ServletRequest request, String jwtToken) {
        try {
            validationAuthorizationHeader(jwtToken);
            String token = extractToken(jwtToken);
            Jws<Claims> claims = Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (SignatureException e) {
            request.setAttribute("exception", "ForbiddenException");
        } catch (MalformedJwtException e) {
            request.setAttribute("exception", "MalformedJwtException");
        } catch (ExpiredJwtException e) {
            request.setAttribute("exception", "ExpiredJwtException");
        } catch (UnsupportedJwtException e) {
            request.setAttribute("exception", "UnsupportedJwtException");
        } catch (IllegalArgumentException e) {
            request.setAttribute("exception", "IllegalArgumentException");
        }
        return false;
    }

    // RefreshToken 유효성 검증 메소드
    public String validateRefreshToken(RefreshToken refreshTokenObj) {
        String refreshToken = refreshTokenObj.getRefreshToken();

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken);
            //AccessToken이 만료되지않았을떄만
            /*if(!claims.getBody().getExpiration().before(new Date())) {
                return recreationAccessToken(claims.getBody().get("sub").toString(), claims.getBody().get("roles"));
            }*/
            return recreationAccessToken(claims.getBody().get("sub").toString(), claims.getBody().get("roles"));
        }catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(StatusCode.EXPIRED_JWT);
        }
    }

    //AccessToken 새로 발급
    private String recreationAccessToken(String email, Object roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);
        Date currentTime = new Date();
        return getToken(claims, currentTime, accessTokenValidTime, accessSecretKey);
    }
}
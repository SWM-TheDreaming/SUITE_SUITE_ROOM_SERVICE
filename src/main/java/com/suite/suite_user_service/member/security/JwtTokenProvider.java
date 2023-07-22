package com.suite.suite_user_service.member.security;


import com.suite.suite_user_service.member.config.ConfigUtil;
import com.suite.suite_user_service.member.dto.Token;
import com.suite.suite_user_service.member.entity.Member;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String NICKNAME = "NICKNAME";
    public static final String ACCOUNTSTATUS = "ACCOUNTSTATUS";


    private final ConfigUtil configUtil;
    private String accessSecretKey;
    private String refreshSecretKey;
    private long accessTokenValidTime;
    private long refreshTokenValidTime;

    private final UserDetailsService userDetailsService;

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        accessSecretKey = Base64.getEncoder().encodeToString(configUtil.getProperty("jwt.access.key").getBytes());
        refreshSecretKey = Base64.getEncoder().encodeToString(configUtil.getProperty("jwt.refresh.key").getBytes());
        accessTokenValidTime = Long.parseLong(configUtil.getProperty("jwt.access.validtime"));
        refreshTokenValidTime = Long.parseLong(configUtil.getProperty("jwt.refresh.validtime"));
    }

    // 토큰 생성
    public Token createToken(Member member) {  // userPK = email
        Claims claims = Jwts.claims().setSubject(member.getEmail()); // JWT payload 에 저장되는 정보단위
        Date now = new Date();
        String accessToken = getToken(member, claims, now, accessTokenValidTime, accessSecretKey);
        String refreshToken = getToken(member, claims, now, refreshTokenValidTime, refreshSecretKey);
        return Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .key(member.getEmail()).build();
    }

    private String getToken(Member member, Claims claims, Date currentTime, long tokenValidTime, String secretKey) {
        return Jwts.builder()
                .setClaims(claims) //정보 저장
                .claim(ID, String.valueOf(member.getMemberId()))
                .claim(NAME, member.getMemberInfo().getName())
                .claim(NICKNAME, member.getMemberInfo().getNickname())
                .claim(ACCOUNTSTATUS, member.getAccountStatus())
                .setIssuedAt(currentTime)  //토큰 발행시간 정보
                .setExpiration(new Date(currentTime.getTime() + tokenValidTime)) //Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  //암호화 알고리즘
                .compact();

    }

    // 인증 정보 조회
    public Authentication getAuthentication(ServletRequest request, String token) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(extractToken(token).getSubject());
            return new JwtAuthenticationToken(userDetails, null, userDetails.getAuthorities(), extractToken(token));
        } catch (NullPointerException e) {
            request.setAttribute("exception", "ForbiddenException");
        }
        return null;
    }

    // 토큰에서 회원 정보 추출
    public Claims extractToken(String token) {
        return Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token).getBody();
    }



    //토큰의 유효성 검사
    public boolean validateToken(ServletRequest request, String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(jwtToken);
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
//    public String validateRefreshToken(RefreshToken refreshTokenObj) {
//        String refreshToken = refreshTokenObj.getRefreshToken();
//
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken);
//            //AccessToken이 만료되지않았을떄만
//            /*if(!claims.getBody().getExpiration().before(new Date())) {
//                return recreationAccessToken(claims.getBody().get("sub").toString(), claims.getBody().get("roles"));
//            }*/
//            return recreationAccessToken(claims.getBody().get("sub").toString(), claims.getBody().get("roles"));
//        }catch (Exception e) {
//            e.printStackTrace();
//            throw new CustomException(StatusCode.EXPIRED_JWT);
//        }
//    }
//
//    //AccessToken 새로 발급
//    private String recreationAccessToken(String email, Object roles) {
//        Claims claims = Jwts.claims().setSubject(email);
//        claims.put("roles", roles);
//        Date currentTime = new Date();
//        return getToken(claims, currentTime, accessTokenValidTime, accessSecretKey);
//    }
}
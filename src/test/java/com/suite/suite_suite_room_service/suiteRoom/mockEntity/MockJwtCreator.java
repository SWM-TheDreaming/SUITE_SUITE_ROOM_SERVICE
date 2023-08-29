package com.suite.suite_suite_room_service.suiteRoom.mockEntity;

import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;


public class MockJwtCreator {

    private String accessSecretKey;
    private String refreshSecretKey;
    private long accessTokenValidTime;
    private long refreshTokenValidTime;


    public MockJwtCreator(String accessSecretKey, String refreshSecretKey, long accessTokenValidTime, long refreshTokenValidTime) {
        this.accessSecretKey = Base64.getEncoder().encodeToString(accessSecretKey.getBytes());
        this.refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
        this.accessTokenValidTime = accessTokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
    }

    public MockToken createToken(AuthorizerDto authorizerDto) {  // userPK = email
        Claims claims = Jwts.claims().setSubject(authorizerDto.getEmail()); // JWT payload 에 저장되는 정보단위
        Date now = new Date();


        String accessToken = getMockToken(authorizerDto, claims, now, accessTokenValidTime, accessSecretKey);
        String refreshToken = getMockToken(authorizerDto, claims, now, accessTokenValidTime, refreshSecretKey);

        return MockToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .key(authorizerDto.getEmail())
                .build();

    }

    private String getMockToken(AuthorizerDto authorizerDto, Claims claims, Date currentTime, long tokenValidTime, String secretKey) {
        return Jwts.builder()
                .setClaims(claims) //정보 저장
                .claim(AuthorizerDto.ClaimName.ID.getValue(), String.valueOf(authorizerDto.getMemberId()))
                .claim(AuthorizerDto.ClaimName.NAME.getValue(), authorizerDto.getName())
                .claim(AuthorizerDto.ClaimName.NICKNAME.getValue(), authorizerDto.getNickName())
                .claim(AuthorizerDto.ClaimName.ACCOUNTSTATUS.getValue(), authorizerDto.getAccountStatus())
                .claim(AuthorizerDto.ClaimName.ROLE.getValue(), authorizerDto.getRole())
                .setIssuedAt(currentTime)  //토큰 발행시간 정보
                .setExpiration(new Date(currentTime.getTime() + tokenValidTime)) //Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  //암호화 알고리즘
                .compact();

    }



}
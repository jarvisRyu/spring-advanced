package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.exception.ServerException;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer "; //헤더 토큰앞에 붙는 고정문자열
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String secretKey; //application.yml,properties 에서 설정한 값을 주입.
    private Key key; //디코딩한 실제 key 저장할 객체
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //암호화할 알고리즘


    @PostConstruct  //jwtUtil bean 생성 후에 자동으로 메서드 실행
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);//암호화되있는 jwt.secret.key 값을 디코딩
        key = Keys.hmacShaKeyFor(bytes); //key객체에 넣음
    }

    //토큰생성
    public String createToken(Long userId, String email, UserRole userRole) {//사용자 아이디,이메일,권한
        Date date = new Date(); //현재시각 date 생성

        return BEARER_PREFIX + //"Bearer + Jwts 생성
                Jwts.builder()
                        .setSubject(String.valueOf(userId)) //사용자id
                        //claim= 토큰에 들어가는 정보
                        .claim("email", email)        //사용자 email
                        .claim("userRole", userRole)  //사용자 권한
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) //현재시간+ 60분 -만료시간설정
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact(); //JWT 문자열을 만들어 반환
                        //return "Bearer eyJhb... 반환
    }
    //토큰값 꺼내기
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            //hasText:	return (str != null && !str.isBlank()); null,공백검사
            //그리고 "Bearer"로 시작하는지 검사
            return tokenValue.substring(7); //Bearer 를 때어냄
        }
        throw new ServerException("Not Found Token"); //그 외엔 예외발생
    }
    //토큰 변환(파싱)
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key) //디코딩한 key
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

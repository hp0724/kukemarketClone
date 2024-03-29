package kukekyakya.kukemarket.handler;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtHandler {
    private String type ="Bearer ";

    public String createToken(String encodedKey, String subject, long maxAgeSeconds) {
        // "Bearer Jwt.builder subject 1019 만료기간 hs256"
        Date now = new Date();
        return type + Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + maxAgeSeconds * 1000L))
                .signWith(SignatureAlgorithm.HS256, encodedKey)
                .compact();
    }
    //토큰의 subject로 Member의 id가 저장
    public String extractSubject(String encodedKey, String token) {
        return parse(encodedKey, token).getBody().getSubject();
    }
    //jwt 검증
    public boolean validate(String encodedKey, String token) {
        try {
            parse(encodedKey, token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
    //parse 를통해서 jwt 만들기
    private Jws<Claims> parse(String key, String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(untype(token));
    }

    private String untype(String token) {
        return token.substring(type.length());
    }

}

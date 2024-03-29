package kukekyakya.kukemarket.service.sign;

import kukekyakya.kukemarket.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// 여기는 tokenCofig 사용으로 바뀜
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtHandler jwtHandler;

    @Value("${jwt.max-age.access}") // 1
    private long accessTokenMaxAgeSeconds;

    @Value("${jwt.max-age.refresh}") // 2
    private long refreshTokenMaxAgeSeconds;

    @Value("${jwt.key.access}") // 3
    private String accessKey;

    @Value("${jwt.key.refresh}") // 4
    private String refreshKey;

    public String createAccessToken(String subject) {
        return jwtHandler.createToken(accessKey, subject, accessTokenMaxAgeSeconds);
    }

    public String createRefreshToken(String subject) {
        return jwtHandler.createToken(refreshKey, subject, refreshTokenMaxAgeSeconds);
    }

    public boolean validateAccessToken(String token){
        return jwtHandler.validate(accessKey,token);

    }

    public boolean validateRefreshToken(String token){
        return jwtHandler.validate(refreshKey,token);
    }
    public String extractAccessTokenSubject(String token){
        return jwtHandler.extractSubject(accessKey,token);
    }
    public String extractRefreshTokenSubject(String token){
        return  jwtHandler.extractSubject(refreshKey,token);
    }
}

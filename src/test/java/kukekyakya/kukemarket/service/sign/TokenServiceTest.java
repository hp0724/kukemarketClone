package kukekyakya.kukemarket.service.sign;

import kukekyakya.kukemarket.handler.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    //의존성을 가지고 있는 객체들을 가짜로 만들어서 주입받을 수 있도록 함
    @InjectMocks TokenService tokenService;
    //객체들을 가짜로 만들어서 @InjectMocks로 지정된 객체에 주입
    @Mock
    JwtHandler jwtHandler;

    @BeforeEach
    void beforeEach(){
        //TokenService는 @Value를 이용하여 설정 파일에서 값을 읽어와야합니다
        //리플렉션을 이용해서 어떠한 객체의 필드 값을 임의의 값으로 주입
        ReflectionTestUtils.setField(tokenService, "accessTokenMaxAgeSeconds", 10L);
        ReflectionTestUtils.setField(tokenService, "refreshTokenMaxAgeSeconds", 10L);
        ReflectionTestUtils.setField(tokenService, "accessKey", "accessKey");
        ReflectionTestUtils.setField(tokenService, "refreshKey", "refreshKey");
    }

    @Test
    void createAccessTokenTest() {
        // given
        given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("access");

        // when
        String token = tokenService.createAccessToken("subject");

        // then
        assertThat(token).isEqualTo("access");
        verify(jwtHandler).createToken(anyString(), anyString(), anyLong());
    }

    @Test
    void createRefreshTokenTest() {
        // given
        given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("refresh");

        // when
        String token = tokenService.createRefreshToken("subject");

        // then
        assertThat(token).isEqualTo("refresh");
        verify(jwtHandler).createToken(anyString(), anyString(), anyLong());
    }

    @Test
    void validateAccessTokenTest(){
        given(jwtHandler.validate(anyString(),anyString())).willReturn(true);
        assertThat(tokenService.validateAccessToken("token")).isTrue();
    }

    @Test
    void invalidateAccessTokenTest(){
        given(jwtHandler.validate(anyString(),anyString())).willReturn(false);
        assertThat(tokenService.validateAccessToken("token")).isFalse();
    }

    @Test
    void validateRefreshTokenTest(){
        given(jwtHandler.validate(anyString(),anyString())) .willReturn(true);
        assertThat(tokenService.validateRefreshToken("token")).isTrue();
    }

    @Test
    void invalidateRefreshTokenTest(){
        given(jwtHandler.validate(anyString(),anyString())).willReturn(false);
        assertThat(tokenService.validateRefreshToken("token")).isFalse();

    }

    @Test
    void extractAccessTokenSubjectTest(){
        String subject ="subject";
        given(jwtHandler.extractSubject(anyString(),anyString())).willReturn(subject);
        String result = tokenService.extractAccessTokenSubject("token");
        assertThat(subject).isEqualTo(result);
    }

    @Test
    void extractRefreshTokenSubjectTest(){
        String subject ="subject";
        given(jwtHandler.extractSubject(anyString(),anyString())).willReturn(subject);
        String result = tokenService.extractRefreshTokenSubject("token");
        assertThat(subject).isEqualTo(result);
    }
}

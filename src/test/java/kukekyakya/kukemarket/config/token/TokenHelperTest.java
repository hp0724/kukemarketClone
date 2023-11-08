package kukekyakya.kukemarket.config.token;

import kukekyakya.kukemarket.handler.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TokenHelperTest {
    TokenHelper tokenHelper;
    @Mock
    JwtHandler jwtHandler;

    @BeforeEach
    void beforeEach(){
        tokenHelper = new TokenHelper(jwtHandler,"key",1000L);

    }

    @Test
    void createTokenTest(){
        given(jwtHandler.createToken(anyString(),anyString(),anyLong())).willReturn("token");

        String createdToken =tokenHelper.createToken("subject");

        assertThat(createdToken).isEqualTo("token");
        verify(jwtHandler).createToken(anyString(),anyString(),anyLong());


    }

    @Test
    void validateTest(){
        given(jwtHandler.validate(anyString(),anyString())).willReturn(true);
        boolean result = tokenHelper.validate("token");

        assertThat(result).isTrue();
    }

    @Test
    void invalidateTest() {
        // given
        given(jwtHandler.validate(anyString(), anyString())).willReturn(false);

        // when
        boolean result = tokenHelper.validate("token");

        // then
        assertThat(result).isFalse();
    }

    @Test
    void extractSubjectTest(){
        given(jwtHandler.extractSubject(anyString(),anyString())).willReturn("subject");
        String subject = tokenHelper.extractSubject("token");
        assertThat(subject).isEqualTo("subject");
    }
}

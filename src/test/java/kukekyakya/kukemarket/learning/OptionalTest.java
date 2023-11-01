package kukekyakya.kukemarket.learning;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.optional;

public class OptionalTest {
    @Test
    void doesNotInvokeOptionalInnerFunctionByOuterNullValueTest(){
        Long result = Optional.ofNullable(null)
                .map(id->Optional.ofNullable((Long) null).orElseThrow(RuntimeException::new))
                        .orElse(5L);
        //ofNullable에 null 주어진다면 , map은 호출되지 않는다.
        assertThat(result).isEqualTo(5L);
    }
    @Test
    void catchWhenExceptionlsThownInOptionalInnerFunctionTest(){
        assertThatThrownBy(
                ()->Optional.ofNullable(5L)
                        .map(id->Optional.ofNullable((Long)null).orElseThrow(RuntimeException::new))
                        .orElse(1L))
                .isInstanceOf(RuntimeException.class);

    }
}

package kukekyakya.kukemarket.dto.sign;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

public class SignInRequestValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Test
    void validateTest(){
        SignInRequest req =createRequest();
        Set<ConstraintViolation<SignInRequest>>validate =validator.validate(req);

        assertThat(validate).isEmpty();
    }
    
    @Test 
    void invalidateByNotFormattedEmailTest(){
        String invalidValue = "email";
        SignInRequest req = createRequestWithEmail(invalidValue);
        Set<ConstraintViolation<SignInRequest>> validate =validator.validate(req);
        //error 가 있다는 말씀
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }
    @Test
    void invalidateByEmptyEmailTest(){
        String invalidValue =null;
        SignInRequest req = createRequestWithEmail(invalidValue);

        Set<ConstraintViolation<SignInRequest>> validate=validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void  invalidateByBlankEmailTest() {
        String invalidValue =" ";
        SignInRequest req = createRequestWithEmail(invalidValue);
        Set<ConstraintViolation<SignInRequest>>  validate =validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }

    private SignInRequest createRequest() { // 6
        return new SignInRequest("email@email.com", "123456a!");
    }

    private SignInRequest createRequestWithEmail(String email) { // 7
        return new SignInRequest(email, "123456a!");
    }

    private SignInRequest createRequestWithPassword(String password) { // 8
        return new SignInRequest("email@email.com", password);
    }

}

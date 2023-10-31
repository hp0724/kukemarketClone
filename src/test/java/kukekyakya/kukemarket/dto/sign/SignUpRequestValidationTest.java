package kukekyakya.kukemarket.dto.sign;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class SignUpRequestValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest(){
        SignUpRequest req = createRequest();
        Set<ConstraintViolation<SignUpRequest>> validate =validator.validate(req);
        assertThat(validate).isEmpty();
    }
    @Test
    void invalidateByNotFormattedEmailTest(){
        String invalidValue="email";
        SignUpRequest req = createRequestWithEmail(invalidValue);
        Set<ConstraintViolation<SignUpRequest>> validate =validator.validate(req);
        //에러 발생해야 해서 empty 면 안됨
        assertThat(validate).isNotEmpty();
        // 해당 invalidValue 관련한 에러가 있어야함
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }
    @Test
    void invalidateByEmailTest() {
        String invalidValue = null;
        SignUpRequest req = createRequestWithEmail(invalidValue);

        Set<ConstraintViolation<SignUpRequest>>validate =validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }

    @Test
    void invalidateByBlankEmailTest(){
        String invalidValue =" ";
        SignUpRequest req =createRequestWithEmail(invalidValue);

        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyPasswordTest(){
        String invalidValue = null;
        SignUpRequest req = createRequestWithPassword(invalidValue);

        Set<ConstraintViolation<SignUpRequest>> validate =validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }

    @Test
    void invalidateByBlankPasswordTest() {
        String invalidValue ="         ";
        SignUpRequest req = createRequestWithPassword(invalidValue);

        Set<ConstraintViolation<SignUpRequest>> validate=validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByShortPasswordTest(){
        String invalidValue="12312a!";
        SignUpRequest req = createRequestWithPassword(invalidValue);

        Set<ConstraintViolation<SignUpRequest>> validate =validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }
    @Test
    void invalidateByNoneAlphabetPasswordTest() {
        String invalidValue ="1234!@#12334";
        SignUpRequest req = createRequestWithPassword(invalidValue);

        Set<ConstraintViolation<SignUpRequest>> validate =validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }

    @Test
    void invalidateByNoneSpecialCasePasswordTest() {
        // given
        String invalidValue = "abc123abc";
        SignUpRequest req = createRequestWithPassword(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyUsernameTest(){
        String invalidValue = null;
        SignUpRequest req= createRequestWithUsername(invalidValue);

        Set<ConstraintViolation<SignUpRequest>> validate =validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }

    @Test
    void invalidateByBlankUsernameTest(){
        String invalidValue = "  ";
        SignUpRequest req =createRequestWithUsername(invalidValue);

        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByShortUsernameTest() {
        // given
        String invalidValue = "한";
        SignUpRequest req = createRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNotAlphabetOrHangeulUsernameTest() {
        // given
        String invalidValue = "송2jae";
        SignUpRequest req = createRequestWithUsername(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByEmptyNicknameTest() {
        // given
        String invalidValue = null;
        SignUpRequest req = createRequestWithNickname(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankNicknameTest() {
        // given
        String invalidValue = " ";
        SignUpRequest req = createRequestWithNickname(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByShortNicknameTest() {
        // given
        String invalidValue = "한";
        SignUpRequest req = createRequestWithNickname(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNotAlphabetOrHangeulNicknameTest() {
        // given
        String invalidValue = "송2jae";
        SignUpRequest req = createRequestWithNickname(invalidValue);

        // when
        Set<ConstraintViolation<SignUpRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }



    private SignUpRequest createRequest() {
        return new SignUpRequest("email@email.com", "123456a!", "username", "nickname");
    }

    private SignUpRequest createRequestWithEmail(String email) {
        return new SignUpRequest(email, "123456a!", "username", "nickname");
    }

    private SignUpRequest createRequestWithPassword(String password) {
        return new SignUpRequest("email@email.com", password, "username", "nickname");
    }

    private SignUpRequest createRequestWithUsername(String username) {
        return new SignUpRequest("email@email.com", "123456a!", username, "nickname");
    }

    private SignUpRequest createRequestWithNickname(String nickname) {
        return new SignUpRequest("email@email.com", "123456a!", "username", nickname);
    }
}

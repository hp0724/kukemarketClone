package kukekyakya.kukemarket.dto.post;

import kukekyakya.kukemarket.factory.dto.PostCreateRequestFactory;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static kukekyakya.kukemarket.factory.dto.PostCreateRequestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

public class PostCreateRequestValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest(){
        PostCreateRequest req = createPostCreateRequestWithMemberId(null);
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);
        assertThat(validate).isEmpty();
    }
    @Test
    void invalidateByEmptyTitleTest(){
        String invalidValue =null;
        PostCreateRequest req = createPostCreateRequestWithTitle(invalidValue);

        Set<ConstraintViolation<PostCreateRequest>> validate =validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }
    @Test
    void invalidateByBlankTitleTest(){
        String invalidValue =" ";
        PostCreateRequest req = createPostCreateRequestWithTitle(invalidValue);
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }

    @Test
    void invalidateByEmptyContentTest() {
        // given
        String invalidValue = null;
        PostCreateRequest req = createPostCreateRequestWithContent(invalidValue);

        // when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByBlankContentTest() {
        // given
        String invalidValue = " ";
        PostCreateRequest req = createPostCreateRequestWithContent(invalidValue);

        // when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNullPriceTest() {
        // given
        Long invalidValue = null;
        PostCreateRequest req = createPostCreateRequestWithPrice(invalidValue);

        // when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNegativePriceTest() {
        // given
        Long invalidValue = -1L;
        PostCreateRequest req = createPostCreateRequestWithPrice(invalidValue);

        // when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNotNullMemberIdTest() {
        // given
        Long invalidValue = 1L;
        PostCreateRequest req = createPostCreateRequestWithMemberId(invalidValue);

        // when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNullCategoryIdTest() {
        // given
        Long invalidValue = null;
        PostCreateRequest req = createPostCreateRequestWithCategoryId(invalidValue);

        // when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByNegativeCategoryIdTest() {
        // given
        Long invalidValue = -1L;
        PostCreateRequest req = createPostCreateRequestWithCategoryId(invalidValue);

        // when
        Set<ConstraintViolation<PostCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }
}

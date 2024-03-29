package kukekyakya.kukemarket.factory.dto;

import kukekyakya.kukemarket.dto.category.CategoryCreateRequest;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static kukekyakya.kukemarket.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static kukekyakya.kukemarket.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequestWithName;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryCreateRequestValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest(){
        CategoryCreateRequest req = createCategoryCreateRequest();
        Set<ConstraintViolation<CategoryCreateRequest>> validate =validator.validate(req);
        assertThat(validate).isEmpty();
    }

    @Test
    void invalidateByEmptyNameTest(){
        String invalidValue =null;
        CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);

        Set<ConstraintViolation<CategoryCreateRequest>>validate = validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }

    @Test
    void invalidByBlankNameTest(){
        String invalidValue ="";
        CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);
        Set<ConstraintViolation<CategoryCreateRequest>>validate = validator.validate(req);

        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v->v.getInvalidValue()).collect(toSet())).contains(invalidValue);

    }
    @Test
    void invalidateByShortNameTest() {
        // given
        String invalidValue = "c";
        CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }

    @Test
    void invalidateByLongNameTest() {
        // given
        String invalidValue = "c".repeat(50);

        CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(toSet())).contains(invalidValue);
    }
}

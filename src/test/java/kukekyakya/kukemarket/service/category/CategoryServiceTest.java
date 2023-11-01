package kukekyakya.kukemarket.service.category;

import kukekyakya.kukemarket.dto.category.CategoryCreateRequest;
import kukekyakya.kukemarket.dto.category.CategoryDto;
import kukekyakya.kukemarket.exception.CategoryNotFoundException;
import kukekyakya.kukemarket.factory.entity.CategoryFactory;
import kukekyakya.kukemarket.repository.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static kukekyakya.kukemarket.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks CategoryService categoryService;
    @Mock
    CategoryRepository categoryRepository;

    @Test
    void readAllTest(){
        given(categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc())
                .willReturn(
                        List.of(CategoryFactory.createCategoryWithName("name1"),
                                CategoryFactory.createCategoryWithName("name2"))
                );

        List<CategoryDto> result = categoryService.readAll();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("name1");
        assertThat(result.get(1).getName()).isEqualTo("name2");
    }

    @Test
    void createTest(){
        CategoryCreateRequest req = createCategoryCreateRequest();
        categoryService.create(req);

        verify(categoryRepository).save(any());
    }

    @Test
    void deleteTest(){
        given(categoryRepository.existsById(anyLong())).willReturn(true);
        categoryService.delete(1L);
        verify(categoryRepository).deleteById(anyLong());
    }

    @Test
    void deleteExceptionByCategoryNotFoundTest(){
        given(categoryRepository.existsById(anyLong())).willReturn(false);
        assertThatThrownBy(()->categoryService.delete(1L)).isInstanceOf(CategoryNotFoundException.class);
    }
}
package kukekyakya.kukemarket.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import kukekyakya.kukemarket.dto.category.CategoryCreateRequest;
import kukekyakya.kukemarket.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static kukekyakya.kukemarket.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {


    @InjectMocks
    CategoryController categoryController;
    @Mock CategoryService categoryService;
    MockMvc mockMvc;
    ObjectMapper objectMapper= new ObjectMapper();

    @BeforeEach
    void beforeEach (){
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

    }

    @Test
    void readAllTest() throws Exception{
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
        verify(categoryService).readAll();
    }

    @Test
    void createTest ( )throws Exception{
        CategoryCreateRequest req = createCategoryCreateRequest();

        mockMvc.perform(
                post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
        verify(categoryService).create(req);
    }

    @Test
    void deleteTest() throws Exception {
        Long id = 1L;

        mockMvc.perform(
                delete("/api/categories/{id}",id)
            ).andExpect(status().isOk());
        verify(categoryService).delete(id);
    }

}

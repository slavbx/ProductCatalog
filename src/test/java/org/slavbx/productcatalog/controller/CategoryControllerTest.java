package org.slavbx.productcatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Import(TestContainerConfig.class)
@SpringBootTest
@DisplayName("Тестирование CategoryController")
class CategoryControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /categories - получение всех категорий")
    void getAllCategories() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].desc").exists())
                .andExpect(jsonPath("$[1].name").exists())
                .andExpect(jsonPath("$[1].desc").exists());
    }

    @Test
    @DisplayName("GET /categories/{id} - получение категории по id")
    void getCategoryById() throws Exception {
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    @DisplayName("GET /categories/name/{name} - получение категории по имени")
    void getCategoryByName() throws Exception {
        mockMvc.perform(get("/categories/name/Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.desc").value("Электронные устройства и аксессуары"));
    }

    @Test
    @DisplayName("POST /categories - создание новой категории")
    void createCategory() throws Exception {
        CategoryDto newCategory = CategoryDto.builder()
                .name("NewCategory")
                .desc("New category description")
                .build();

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategory)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("NewCategory"))
                .andExpect(jsonPath("$.desc").value("New category description"));

        mockMvc.perform(get("/categories/name/NewCategory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewCategory"));
    }

    @Test
    @DisplayName("PUT /categories - обновление категории")
    void updateCategory() throws Exception {
        CategoryDto updateCategory = CategoryDto.builder()
                .name("Electronics")
                .desc("Updated category description")
                .build();
        mockMvc.perform(put("/categories")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(updateCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"))
                .andExpect(jsonPath("$.desc").value("Updated category description"));
        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    @DisplayName("DELETE /categories/{name} - удаление категории по имени")
    void deleteCategory() throws Exception {
        CategoryDto toDeleteCategory = CategoryDto.builder()
                .name("toDelete")
                .desc("Категория для удаления")
                .build();
        mockMvc.perform(post("/categories")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(toDeleteCategory))).andExpect(status().isCreated());
        mockMvc.perform(delete("/categories/toDelete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));
    }
}
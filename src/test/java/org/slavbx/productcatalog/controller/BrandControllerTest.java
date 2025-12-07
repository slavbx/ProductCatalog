package org.slavbx.productcatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.dto.BrandDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Import(TestContainerConfig.class)
@SpringBootTest
@DisplayName("Тестирование BrandController")
class BrandControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /brands - получение всех брендов")
    void getAllBrands() throws Exception {
        mockMvc.perform(get("/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].desc").exists())
                .andExpect(jsonPath("$[1].name").exists())
                .andExpect(jsonPath("$[1].desc").exists());
    }

    @Test
    @DisplayName("GET /brands/{id} - получение бренда по id")
    void getBrandById() throws Exception {
        mockMvc.perform(get("/brands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zalman"))
                .andExpect(jsonPath("$.desc").value("Производитель систем охлаждения и корпусов"));
    }

    @Test
    @DisplayName("GET /brands/name/{name} - получение бренда по имени")
    void getBrandByName() throws Exception {
        mockMvc.perform(get("/brands/name/Zalman"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zalman"))
                .andExpect(jsonPath("$.desc").value("Производитель систем охлаждения и корпусов"));
    }

    @Test
    @DisplayName("POST /brands - создание нового бренда")
    void createBrand() throws Exception {
        BrandDto newBrand = BrandDto.builder()
                .name("Corsair")
                .desc("Производитель оперативной памяти и блоков питания")
                .build();

        mockMvc.perform(post("/brands")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(newBrand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Corsair"))
                .andExpect(jsonPath("$.desc").value("Производитель оперативной памяти и блоков питания"));

        mockMvc.perform(get("/brands/name/Corsair"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Corsair"));
    }

    @Test
    @DisplayName("PUT /brands - обновление бренда")
    void updateBrand() throws Exception {
        BrandDto updateBrand = BrandDto.builder()
                .name("Zalman")
                .desc("Обновленное описание производителя")
                .build();
        mockMvc.perform(put("/brands")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(updateBrand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zalman"))
                .andExpect(jsonPath("$.desc").value("Обновленное описание производителя"));
        mockMvc.perform(get("/brands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zalman"));
    }

    @Test
    @DisplayName("DELETE /brands/{name} - удаление бренда по имени")
    void deleteBrand() throws Exception {
        BrandDto toDeleteBrand = BrandDto.builder()
                .name("toDelete")
                .desc("Бренд для удаления")
                .build();
        mockMvc.perform(post("/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toDeleteBrand))).andExpect(status().isCreated());
        mockMvc.perform(delete("/brands/toDelete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));
    }
}
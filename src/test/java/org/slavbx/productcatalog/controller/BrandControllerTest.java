package org.slavbx.productcatalog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.dto.BrandDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Тестирование BrandController")
class BrandControllerTest extends TestContainerConfig {

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
        BrandDTO newBrand = BrandDTO.builder()
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
        BrandDTO updateBrand = BrandDTO.builder()
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
    @DisplayName("DELETE /brands/{name} - получение бренда по имени")
    void deleteBrand() throws Exception {
        BrandDTO toDeleteBrand = BrandDTO.builder()
                .name("toDelete")
                .desc("Бренд для удаления")
                .build();
        mockMvc.perform(post("/brands")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(toDeleteBrand))).andExpect(status().isCreated());
        mockMvc.perform(delete("/brands/toDelete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));
    }
}
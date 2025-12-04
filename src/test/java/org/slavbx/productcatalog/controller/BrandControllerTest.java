package org.slavbx.productcatalog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.dto.BrandDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.shaded.org.hamcrest.Matchers.greaterThan;
import static org.testcontainers.shaded.org.hamcrest.Matchers.hasSize;

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
    @DisplayName("POST /brands/create - создание нового бренда")
    void createBrand() throws Exception {
        BrandDTO newBrand = BrandDTO.builder()
                .name("Corsair")
                .desc("Производитель оперативной памяти и блоков питания")
                .build();

        mockMvc.perform(post("/brands/create")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(newBrand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Corsair"))
                .andExpect(jsonPath("$.desc").value("Производитель оперативной памяти и блоков питания"));

        mockMvc.perform(get("/brands/name/Corsair"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Corsair"));
    }

    @Test
    @DisplayName("PUT /brands/update/{id} - обновление бренда")
    void updateBrand() throws Exception {
        String response = mockMvc.perform(get("/brands/name/Zalman"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Long brandId = objectMapper.readTree(response).get("id").asLong();
        BrandDTO updateBrand = BrandDTO.builder()
                .name("Zalman Updated")
                .desc("Обновленное описание производителя")
                .build();
        mockMvc.perform(put("/brands/update/" + brandId)
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(updateBrand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zalman Updated"))
                .andExpect(jsonPath("$.desc").value("Обновленное описание производителя"));
        mockMvc.perform(get("/brands/" + brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zalman Updated"));
    }

    @Test
    void deleteBrand() {
    }
}
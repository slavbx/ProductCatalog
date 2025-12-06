package org.slavbx.productcatalog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.dto.ProductDto;
import org.slavbx.productcatalog.dto.UserDto;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Тестирование ProductController")
class ProductControllerTest extends TestContainerConfig {

    @Test
    @DisplayName("GET /products - получение всех товаров")
    void getAllProducts() throws Exception {
        UserDto userDTO = UserDto.builder()
                .email("slav@slav.com")
                .password("slav")
                .build();
        mockMvc.perform(post("/auth/signin")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(userDTO)));
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].desc").exists())
                .andExpect(jsonPath("$[0].price").exists())
                .andExpect(jsonPath("$[1].name").exists())
                .andExpect(jsonPath("$[1].desc").exists())
                .andExpect(jsonPath("$[1].price").exists());
    }

    @Test
    @DisplayName("GET /products/{id} - получение товара по id")
    void getProductById() throws Exception {
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Корпус ПК"))
                .andExpect(jsonPath("$.desc").value("Прочный и стильный корпус для сборки ПК"))
                .andExpect(jsonPath("$.price").value(3200.00));
    }

    @Test
    @DisplayName("POST /products - создание нового товара")
    void createProduct() throws Exception {
        ProductDto newProduct = ProductDto.builder()
                .name("NewProduct")
                .desc("New product description")
                .price(BigDecimal.valueOf(200.0))
                .quantity(10)
                .sellerEmail("slav@slav.com")
                .categoryName("Electronics")
                .brandName("Zalman")
                .build();
        mockMvc.perform(post("/products")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("NewProduct"))
                .andExpect(jsonPath("$.desc").value("New product description"))
                .andExpect(jsonPath("$.price").value(200.0));
        mockMvc.perform(get("/products/name/NewProduct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewProduct"));
    }

    @Test
    @DisplayName("PUT /products - обновление товара")
    void updateProduct() throws Exception {
        ProductDto product = ProductDto.builder()
                .name("NewProduct1")
                .desc("New product description")
                .price(BigDecimal.valueOf(200.0))
                .quantity(10)
                .sellerEmail("slav@slav.com")
                .categoryName("Electronics")
                .brandName("Zalman")
                .build();
        mockMvc.perform(post("/products")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(product)));

        ProductDto updateProduct = ProductDto.builder()
                .name("NewProduct1")
                .desc("Updated product description")
                .price(BigDecimal.valueOf(201.0))
                .quantity(101)
                .sellerEmail("slav@slav.com")
                .categoryName("Electronics")
                .brandName("Zalman")
                .build();
        mockMvc.perform(put("/products")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(updateProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewProduct1"))
                .andExpect(jsonPath("$.desc").value("Updated product description"))
                .andExpect(jsonPath("$.price").value(201.0));
        mockMvc.perform(get("/products/name/NewProduct1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewProduct1"));
    }

    @Test
    @DisplayName("DELETE /products/{name} - получение товара по имени")
    void deleteProduct() throws Exception {
        ProductDto toDeleteProduct = ProductDto.builder()
                .name("toDelete")
                .desc("Товар для удаления")
                .price(BigDecimal.valueOf(50.0))
                .quantity(5)
                .sellerEmail("slav@slav.com")
                .categoryName("Electronics")
                .brandName("Zalman")
                .build();
        mockMvc.perform(post("/products")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(toDeleteProduct))).andExpect(status().isCreated());
        mockMvc.perform(delete("/products/toDelete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));
    }
}
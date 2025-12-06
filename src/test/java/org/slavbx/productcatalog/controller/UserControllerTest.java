package org.slavbx.productcatalog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slavbx.productcatalog.TestContainerConfig;
import org.slavbx.productcatalog.dto.UserDto;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Тестирование UserController")
class UserControllerTest extends TestContainerConfig {

    @Test
    @DisplayName("GET /users - получение всех пользователей")
    void getAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].email").exists())
                .andExpect(jsonPath("$[1].name").exists())
                .andExpect(jsonPath("$[1].email").exists());
    }

    @Test
    @DisplayName("GET /users/{id} - получение пользователя по id")
    void getUserById() throws Exception {
        mockMvc.perform(get("/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("slav"))
                .andExpect(jsonPath("$.email").value("slav@slav.com"));
    }

    @Test
    @DisplayName("GET /users/email/{email} - получение пользователя по email")
    void getUserByEmail() throws Exception {
        mockMvc.perform(get("/users/email/slav@slav.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("slav"))
                .andExpect(jsonPath("$.email").value("slav@slav.com"));
    }

    @Test
    @DisplayName("POST /users - создание нового пользователя")
    void createUser() throws Exception {
        UserDto newUser = UserDto.builder()
                .name("NewUser1")
                .email("newuser1@example.com")
                .password("password123")
                .build();
        mockMvc.perform(post("/users")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("NewUser1"))
                .andExpect(jsonPath("$.email").value("newuser1@example.com"));
        mockMvc.perform(get("/users/email/newuser1@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewUser1"));
    }

    @Test
    @DisplayName("PUT /users - обновление пользователя")
    void updateUser() throws Exception {
        UserDto updateUser = UserDto.builder()
                .name("slav")
                .email("slav@slav.com")
                .password("newPassword")
                .build();
        mockMvc.perform(put("/users")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("slav"))
                .andExpect(jsonPath("$.email").value("slav@slav.com"))
                .andExpect(jsonPath("$.password").value("newPassword"));
        mockMvc.perform(get("/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("slav"));
    }

    @Test
    @DisplayName("PUT /users/{email}/reset-password - сброс пароля пользователя")
    void resetPassword() throws Exception {
        UserDto newUser = UserDto.builder()
                .name("NewUser")
                .email("newuser@example.com")
                .password("password123")
                .build();
        mockMvc.perform(post("/users")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated());
        mockMvc.perform(put("/users/newuser@example.com/reset-password"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset successfully"));
    }
}
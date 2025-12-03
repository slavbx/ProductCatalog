package org.slavbx.productcatalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.dto.UserDTO;
import org.slavbx.productcatalog.mapper.UserMapper;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для обработки HTTP-запросов аутентификации.
 * Поддерживает авторизацию и завершение сеанса пользователя.
 */
@Tag(name = "AuthController", description = "API for authentication and authorization")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;
    private final UserMapper userMapper;
    private final ValidationUtil validationUtil;

    @Operation(summary = "User sign in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authorized"),
            @ApiResponse(responseCode = "400", description = "Invalid user data"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/signin")
    @Auditable(action = "Авторизация")
    public ResponseEntity<String> signIn(@RequestBody UserDTO userDTO) {
        validationUtil.validate(userDTO);

        User user = userMapper.userDTOToUser(userDTO);
        authService.signIn(user);

        return ResponseEntity.ok("Successfully authorized");
    }

    @Operation(summary = "User sign out")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully unauthorized"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @PostMapping("/signout")
    @Auditable(action = "Завершение сеанса")
    public ResponseEntity<String> signOut() {
        authService.signOut();
        return ResponseEntity.ok("Successfully unauthorized");
    }
}

package org.slavbx.productcatalog.controller;

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
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authService;
    private final UserMapper userMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public AuthController(AuthenticationService authService,
                          UserMapper userMapper,
                          ValidationUtil validationUtil) {
        this.authService = authService;
        this.userMapper = userMapper;
        this.validationUtil = validationUtil;
    }

    @PostMapping("/signin")
    @Auditable(action = "Авторизация")
    public ResponseEntity<String> signIn(@RequestBody UserDTO userDTO) {
        validationUtil.validate(userDTO);

        User user = userMapper.userDTOToUser(userDTO);
        authService.signIn(user);

        return ResponseEntity.ok("Successfully authorized");
    }

    @PostMapping("/signout")
    @Auditable(action = "Завершение сеанса")
    public ResponseEntity<String> signOut() {
        authService.signOut();
        return ResponseEntity.ok("Successfully unauthorized");
    }
}

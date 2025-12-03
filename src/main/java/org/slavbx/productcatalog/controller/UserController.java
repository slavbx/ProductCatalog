package org.slavbx.productcatalog.controller;

import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.dto.UserDTO;
import org.slavbx.productcatalog.mapper.UserMapper;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для обработки HTTP-запросов к пользователям.
 * Поддерживает получение, создание, обновление и сброс пароля пользователей.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public UserController(UserService userService,
                          UserMapper userMapper,
                          ValidationUtil validationUtil) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.validationUtil = validationUtil;
    }

    @GetMapping
    @Auditable(action = "Получение всех пользователей")
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return userMapper.usersToUserDTOs(users);
    }

    @GetMapping("/{id}")
    @Auditable(action = "Получение пользователя по id")
    public UserDTO getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return userMapper.userToUserDTO(user);
    }

    @GetMapping("/email/{email}")
    @Auditable(action = "Получение пользователя по email")
    public UserDTO getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return userMapper.userToUserDTO(user);
    }

    @PostMapping
    @Auditable(action = "Создание пользователя")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        validationUtil.validate(userDTO);

        User user = userMapper.userDTOToUser(userDTO);
        User createdUser = userService.create(user);
        UserDTO createdUserDTO = userMapper.userToUserDTO(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDTO);
    }

    @PutMapping
    @Auditable(action = "Обновление пользователя")
    public UserDTO updateUser(@RequestBody UserDTO userDTO) {
        validationUtil.validate(userDTO);

        User user = userMapper.userDTOToUser(userDTO);
        User resultUser = userService.save(user);
        return userMapper.userToUserDTO(resultUser);
    }

    @PutMapping("/{email}/reset-password")
    @Auditable(action = "Сброс пароля пользователя")
    public ResponseEntity<String> resetPassword(@PathVariable String email) {
        userService.resetPassword(email);
        return ResponseEntity.ok("Password reset successfully");
    }
}

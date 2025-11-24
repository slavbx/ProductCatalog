package org.slavbx.productcatalog.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slavbx.productcatalog.annotation.Auditable;
import org.slavbx.productcatalog.dto.UserDTO;
import org.slavbx.productcatalog.exception.NotFoundException;
import org.slavbx.productcatalog.mapper.UserMapper;
import org.slavbx.productcatalog.model.User;
import org.slavbx.productcatalog.repository.RepositoryType;
import org.slavbx.productcatalog.service.UserService;
import org.slavbx.productcatalog.service.ServiceFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@WebServlet("/users/*")
public class UserServlet extends BaseHttpServlet {
    RepositoryType repoType = RepositoryType.valueOf(System.getProperty("repository.type"));
    private final UserService userService = ServiceFactory.getUserService(repoType);
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String pathInfo = httpReq.getRequestURI().substring(httpReq.getContextPath().length());

        if ("/users".equals(pathInfo)) {
            doGetAllUsers(httpReq, httpResp);
        } else if (pathInfo.startsWith("/users/")) {
            String endOfPath = pathInfo.substring("/users/".length());
            if (endOfPath.matches("\\d+")) {
                doGetUserById(httpReq, httpResp, Long.parseLong(endOfPath));
            } else {
                doGetUserByEmail(httpReq, httpResp, URLDecoder.decode(endOfPath, StandardCharsets.UTF_8));
            }
        } else {
            throw new NotFoundException("Resource not found");
        }
    }

    @Auditable(action = "Получение пользователя по id")
    protected void doGetUserById(HttpServletRequest httpReq, HttpServletResponse httpResp, Long id) throws IOException {
        User user = userService.getUserById(id);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        sendJsonResponse(httpResp, userDTO, HttpServletResponse.SC_OK);
    }

    @Auditable(action = "Получение пользователя по email")
    protected void doGetUserByEmail(HttpServletRequest httpReq, HttpServletResponse httpResp, String email) throws IOException {
        User user = userService.getUserByEmail(email);
        UserDTO userDTO = userMapper.userToUserDTO(user);
        sendJsonResponse(httpResp, userDTO, HttpServletResponse.SC_OK);
    }

    @Auditable(action = "Получение всех пользователей")
    protected void doGetAllUsers(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        List<User> users = userService.findAllUsers();
        List<UserDTO> userDTOs = userMapper.usersToUserDTOs(users);
        sendJsonResponse(httpResp, userDTOs, HttpServletResponse.SC_OK);
    }

    @Auditable(action = "Создание пользователя")
    protected void doPost(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String jsonReq = new String(httpReq.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        UserDTO userDTO = objectMapper.readValue(jsonReq, UserDTO.class);
        validate(userDTO);
        User user = userMapper.userDTOToUser(userDTO);

        User createdUser = userService.create(user);
        UserDTO createdUserDTO = userMapper.userToUserDTO(createdUser);

        sendJsonResponse(httpResp, createdUserDTO, HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String pathInfo = httpReq.getRequestURI().substring(httpReq.getContextPath().length());

        if ("/users".equals(pathInfo)) {
            doUpdateUser(httpReq, httpResp);
        } else if (pathInfo.startsWith("/users/") && pathInfo.endsWith("/reset-password")) {
            doResetPassword(httpReq, httpResp, pathInfo);
        } else {
            throw new NotFoundException("Resource not found");
        }
    }

    @Auditable(action = "Обновление пользователя")
    protected void doUpdateUser(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String jsonReq = new String(httpReq.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        UserDTO userDTO = objectMapper.readValue(jsonReq, UserDTO.class);
        validate(userDTO);
        User user = userMapper.userDTOToUser(userDTO);

        User resultUser = userService.save(user);

        sendJsonResponse(httpResp, userMapper.userToUserDTO(resultUser), HttpServletResponse.SC_OK);
    }

    @Auditable(action = "Сброс пароля пользователя")
    protected void doResetPassword(HttpServletRequest httpReq, HttpServletResponse httpResp, String pathInfo) throws IOException {
        String email = pathInfo.substring("/users/".length(), pathInfo.length() - "/reset-password".length());
        userService.resetPassword(email);
        sendSuccessResponse(httpResp, "Password reset successfully");
    }
}

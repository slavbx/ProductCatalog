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
import org.slavbx.productcatalog.security.AuthenticationService;
import org.slavbx.productcatalog.service.ServiceFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Сервлет для обработки HTTP-запросов аутентификации.
 * Поддерживает авторизацию и завершение сеанса пользователя.
 * Доступные эндпоинты:
 * POST /auth/signin - авторизация пользователя
 * POST /auth/signout - завершение сеанса
 */
@WebServlet("/auth/*")
public class AuthServlet extends BaseHttpServlet {
    RepositoryType repoType = RepositoryType.valueOf(System.getProperty("repository.type"));
    private final AuthenticationService authService = ServiceFactory.getAuthService(repoType);
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    protected void doPost(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String pathInfo = httpReq.getRequestURI().substring(httpReq.getContextPath().length());

        if ("/auth/signin".equals(pathInfo)) {
            doPostAuthorize(httpReq, httpResp);
        } else if ("/auth/signout".equals(pathInfo)) {
            doPostUnauthorize(httpReq, httpResp);
        } else {
            throw new NotFoundException("Resource not found");
        }
    }

    @Auditable(action = "Авторизация")
    protected void doPostAuthorize(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        String jsonReq = new String(httpReq.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        UserDTO userDTO = objectMapper.readValue(jsonReq, UserDTO.class);
        validate(userDTO);
        User user = userMapper.userDTOToUser(userDTO);
        authService.signIn(user);
        sendSuccessResponse(httpResp, "Successfully authorized");
    }

    @Auditable(action = "Завершение сеанса")
    protected void doPostUnauthorize(HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException {
        authService.signOut();
        sendSuccessResponse(httpResp, "Successfully unauthorized");
    }
}
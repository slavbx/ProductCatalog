package org.slavbx.productcatalog.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.slavbx.productcatalog.dto.ErrorResponseDTO;
import org.slavbx.productcatalog.dto.ResponseDTO;
import org.slavbx.productcatalog.exception.AlreadyExistsException;
import org.slavbx.productcatalog.exception.NotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class BaseHttpServlet extends HttpServlet {
    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            super.service(req, resp);
        } catch (NotFoundException e) {
            handleNotFoundException(e, resp);
        } catch (AlreadyExistsException e) {
            handleAlreadyExistException(e, resp);
        } catch (IllegalArgumentException e) {
            handleValidationException(e, resp);
        } catch (Exception e) {
            handleGenericException(e, resp);
        }
    }

    protected void handleNotFoundException(NotFoundException e, HttpServletResponse resp) throws IOException {
        sendJsonResponse(resp,
                ErrorResponseDTO.builder().message(e.getMessage()).build(),
                HttpServletResponse.SC_NOT_FOUND);
    }

    protected void handleAlreadyExistException(AlreadyExistsException e, HttpServletResponse resp) throws IOException {
        sendJsonResponse(resp,
                ErrorResponseDTO.builder().message(e.getMessage()).build(),
                HttpServletResponse.SC_CONFLICT);
    }

    protected void handleGenericException(Exception e, HttpServletResponse resp) throws IOException {
        sendJsonResponse(resp,
                ErrorResponseDTO.builder().message("Internal server error").build(),
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    protected void sendJsonResponse(HttpServletResponse resp, Object data, int statusCode) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(objectMapper.writeValueAsString(data));
    }

    protected void sendSuccessResponse(HttpServletResponse resp, String message) throws IOException {
        sendJsonResponse(
                resp,
                ResponseDTO.builder().message(message).build(),
                HttpServletResponse.SC_OK);
    }

    protected <T> void validate(T dto) {
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();

        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            String errors = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException(errors);
        }
    }

    protected void handleValidationException(IllegalArgumentException e, HttpServletResponse resp) throws IOException {
        sendJsonResponse(resp,
                ErrorResponseDTO.builder().message(e.getMessage()).build(),
                HttpServletResponse.SC_BAD_REQUEST);
    }
}

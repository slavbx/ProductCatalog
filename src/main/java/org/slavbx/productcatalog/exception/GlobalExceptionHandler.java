package org.slavbx.productcatalog.exception;

import org.slavbx.productcatalog.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Этот класс централизованно обрабатывает исключения, возникающие во всех контроллерах,
 * и преобразует их в структурированные HTTP-ответы с соответствующими статус-кодами.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException e) {
        ErrorResponseDTO error = ErrorResponseDTO.builder().message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleAlreadyExistsException(AlreadyExistsException e) {
        ErrorResponseDTO error = ErrorResponseDTO.builder().message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(IllegalArgumentException e) {
        ErrorResponseDTO error = ErrorResponseDTO.builder().message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception e) {
        ErrorResponseDTO error = ErrorResponseDTO.builder().message("Internal server error").build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
package io.factorialsystems.msscstore21authorization.exception;

import io.factorialsystems.msscstore21authorization.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class CustomErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OAuth2AuthenticationException.class)
    ResponseEntity<ErrorResponseDTO> handleOAuth2AuthenticationException(OAuth2AuthenticationException oae, WebRequest webRequest) {
        log.error("OAuth2AuthenticationException: {}", oae.getMessage());

        return ResponseEntity.badRequest().body(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .dateTime(Instant.now())
                        .message(oae.getMessage())
                        .path(webRequest.getDescription(false))
                        .build()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException rex, WebRequest webRequest) {
        final String message = String.format("caused by %s", rex.getMessage());
        log.error("RuntimeException: {}", message);

        return ResponseEntity.badRequest().body(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .dateTime(Instant.now())
                        .message(message)
                        .path(webRequest.getDescription(false))
                        .build()
        );
    }

    @ExceptionHandler(UserExistsException.class)
    ResponseEntity<ErrorResponseDTO> handleUserExistsException(UserExistsException uee, WebRequest webRequest) {
        log.error("UserExistsException: {}", uee.getMessage());

        return ResponseEntity.badRequest().body(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .dateTime(Instant.now())
                        .message(uee.getMessage())
                        .path(webRequest.getDescription(false))
                        .build()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException iae, WebRequest webRequest) {
        log.error("IllegalArgumentException: {}", iae.getMessage());

        return ResponseEntity.badRequest().body(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .dateTime(Instant.now())
                        .message(iae.getMessage())
                        .path(webRequest.getDescription(false))
                        .build()
        );
    }

    @ExceptionHandler(UserIdNotFoundException.class)
    ResponseEntity<ErrorResponseDTO> handleUserIdNotFoundException(UserIdNotFoundException uinfe, WebRequest webRequest) {
        log.error("UserIdNotFoundException: {}", uinfe.getMessage());

        return ResponseEntity.badRequest().body(
                ErrorResponseDTO.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .dateTime(Instant.now())
                        .message(uinfe.getMessage())
                        .path(webRequest.getDescription(false))
                        .build()
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        final List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        allErrors.forEach(a -> {
            log.error("Validation Error: {}", a.getDefaultMessage());
            validationErrors.put(((FieldError) a).getField(), a.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(validationErrors);
    }
}

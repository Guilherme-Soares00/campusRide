package br.com.fiap.campusride.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            fields.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiError error = new ApiError(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Dados de entrada inválidos",
                request.getRequestURI(),
                fields
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiError> handleNotFound(RecursoNaoEncontradoException exception, HttpServletRequest request) {
        return criarResposta(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ApiError> handleBusinessRule(RegraNegocioException exception, HttpServletRequest request) {
        return criarResposta(HttpStatus.BAD_REQUEST, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableMessage(HttpServletRequest request) {
        return criarResposta(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido", request, Map.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(HttpServletRequest request) {
        return criarResposta(HttpStatus.BAD_REQUEST, "Parâmetro da requisição inválido", request, Map.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResourceFound(HttpServletRequest request) {
        return criarResposta(HttpStatus.NOT_FOUND, "Recurso não encontrado", request, Map.of());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(HttpServletRequest request) {
        return criarResposta(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP não permitido", request, Map.of());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleMediaTypeNotSupported(HttpServletRequest request) {
        return criarResposta(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Tipo de conteúdo não suportado", request, Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(HttpServletRequest request) {
        return criarResposta(HttpStatus.CONFLICT, "Os dados informados entram em conflito com o estado atual", request, Map.of());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiError> handleConcurrentUpdate(HttpServletRequest request) {
        return criarResposta(
                HttpStatus.CONFLICT,
                "A carona foi atualizada por outra reserva. Tente novamente",
                request,
                Map.of()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception exception, HttpServletRequest request) {
        LOGGER.error("Erro inesperado ao processar {} {}", request.getMethod(), request.getRequestURI(), exception);

        return criarResposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", request, Map.of());
    }

    private ResponseEntity<ApiError> criarResposta(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            Map<String, String> fields
    ) {
        ApiError error = new ApiError(
                Instant.now(),
                status.value(),
                message,
                request.getRequestURI(),
                fields
        );

        return ResponseEntity.status(status).body(error);
    }
}

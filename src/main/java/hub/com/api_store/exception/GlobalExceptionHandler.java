package hub.com.api_store.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Jakarta validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            HttpServletRequest req, MethodArgumentNotValidException ex){
        // filter msg
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": "+e.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                errorMsg,
                req.getRequestURI(),
                "Method Argument Not Valid"
        );
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            HttpServletRequest req, ResourceNotFoundException ex){

        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                req.getRequestURI(),
                "Resource Not Found"
        );
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

    }

    // Validate Unique Exception
    @ExceptionHandler(UniqueValidateException.class)
    public ResponseEntity<ErrorResponse> handleUniqueValidateException(
            HttpServletRequest req, UniqueValidateException ex){
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                req.getRequestURI(),
                "Invalid UniqueValidate"
        );
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Invalid Status Exception
    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidStatusException(
            HttpServletRequest req, InvalidStatusException ex){
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                req.getRequestURI(),
                "Invalid Status"
        );
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    // other exception

    // Bad format JSON 400
    @ExceptionHandler (HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "json bad format.",
                req.getRequestURI(),
                "MalformedJsonError"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Type mismatch in path variable {{8080}}/...
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest req) {

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                String.format("Invalid value '%s' for parameter '%s'. Expected type: %s",
                        ex.getValue(),
                        ex.getName(),
                        ex.getRequiredType().getSimpleName()),
                req.getRequestURI(),
                "TypeMismatchError"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

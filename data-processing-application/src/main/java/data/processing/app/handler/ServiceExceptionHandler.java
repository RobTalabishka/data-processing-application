package data.processing.app.handler;

import data.processing.app.exception.LoripsumException;
import data.processing.app.exception.RequestDataException;
import data.processing.app.model.TextParsingErrorResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * The type Service exception handler.
 */
@ControllerAdvice
public class ServiceExceptionHandler {

    @Value("${default.error.message}")
    private String defaultErrorMessage;

    /**
     * Handle request error response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(value = RequestDataException.class)
    public ResponseEntity<TextParsingErrorResponse> handleRequestError(RuntimeException ex,
                                                                       WebRequest request) {
        var errorResponse = baseErrorProcessing(ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

    /**
     * Handle service error response entity.
     *
     * @param ex      the ex
     * @param request the request
     * @return the response entity
     */
    @ExceptionHandler(value = LoripsumException.class)
    public ResponseEntity<TextParsingErrorResponse> handleServiceError(RuntimeException ex,
                                                                       WebRequest request) {
        var errorResponse =
                baseErrorProcessing("Service temporarily unavailable.");

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(errorResponse);
    }

    private TextParsingErrorResponse baseErrorProcessing(String exMessage) {
        exMessage = StringUtils.isBlank(exMessage) ? defaultErrorMessage : exMessage;

        return TextParsingErrorResponse.builder()
                .errorMessage(exMessage)
                .build();
    }
}

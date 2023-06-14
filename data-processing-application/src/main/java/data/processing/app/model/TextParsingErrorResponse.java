package data.processing.app.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Text parsing error response.
 */
@Getter
@Setter
@Builder
public class TextParsingErrorResponse {
    private String errorMessage;
}

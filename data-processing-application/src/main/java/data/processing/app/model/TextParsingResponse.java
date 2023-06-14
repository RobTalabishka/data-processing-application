package data.processing.app.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The type Text parsing response.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TextParsingResponse {
    @JsonProperty("freq_word")
    private String freqWord;

    @JsonProperty("avg_paragraph_size")
    private double avgParagraphSize;

    @JsonProperty("avg_paragraph_processing_time")
    private long avgParagraphProcessingTime;

    @JsonProperty("total_processing_time")
    private long totalProcessingTime;
}

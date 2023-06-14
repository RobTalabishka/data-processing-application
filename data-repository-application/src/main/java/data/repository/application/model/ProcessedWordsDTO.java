package data.repository.application.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * The type Processed words dto.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProcessedWordsDTO {
    @JsonProperty("freq_word")
    private String freqWord;

    @JsonProperty("avg_paragraph_size")
    private double avgParagraphSize;

    @JsonProperty("avg_paragraph_processing_time")
    private long avgParagraphProcessingTime;

    @JsonProperty("total_processing_time")
    private long totalProcessingTime;
}

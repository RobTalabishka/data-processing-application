package data.repository.application.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Processed words entity.
 */
@Getter
@Setter
@Entity
@Table(name = "processed_words")
public class ProcessedWordsEntity {

    @Id
    @SequenceGenerator(
            name = "processed_words_id_seq",
            sequenceName = "processed_words_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "processed_words_id_seq")
    @Column(name = "id")
    private long id;

    @Column(name = "freq_word")
    private String freqWord;

    @Column(name = "avg_paragraph_size")
    private double avgParagraphSize;

    @Column(name = "avg_paragraph_processing_time")
    private long avgParagraphProcessingTime;

    @Column(name = "total_processing_time")
    private long totalProcessingTime;
}

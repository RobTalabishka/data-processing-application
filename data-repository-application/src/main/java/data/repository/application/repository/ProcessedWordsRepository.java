package data.repository.application.repository;

import data.repository.application.model.entity.ProcessedWordsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Processed words repository.
 */
@Repository
public interface ProcessedWordsRepository extends JpaRepository<ProcessedWordsEntity, Long> {
    /**
     * Gets history.
     *
     * @param limit the limit
     * @return the history
     */
    @Query(value = "SELECT * from processed_words pw order by pw.id desc limit :limit",
            nativeQuery = true)
    List<ProcessedWordsEntity> getHistory(@Param("limit") Integer limit);
}

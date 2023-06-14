package data.repository.application.service;

import data.repository.application.mapper.ProcessedWordsMapper;
import data.repository.application.model.ProcessedWordsDTO;
import data.repository.application.model.entity.ProcessedWordsEntity;
import data.repository.application.repository.ProcessedWordsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Processed words service.
 */
@Service
@RequiredArgsConstructor
public class ProcessedWordsService {

    @Value("${default.response.history.records}")
    private int historyRecords;

    private final ProcessedWordsRepository processedWordsRepository;

    /**
     * Process message.
     *
     * @param processedWordsDTO the processed words dto
     */
    public void processMessage(ProcessedWordsDTO processedWordsDTO) {
        ProcessedWordsEntity processedWordsEntity = ProcessedWordsMapper.INSTANCE.copyToEntity(processedWordsDTO);
        processedWordsRepository.save(processedWordsEntity);
    }

    /**
     * Retrieve history list.
     *
     * @return the list
     */
    public List<ProcessedWordsDTO> retrieveHistory() {
        List<ProcessedWordsEntity> historyEntities = processedWordsRepository.getHistory(historyRecords);
        return ProcessedWordsMapper.INSTANCE.copyToDTOs(historyEntities);
    }

}

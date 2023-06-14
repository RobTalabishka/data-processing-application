package data.repository.application.controller;

import data.repository.application.model.ProcessedWordsDTO;
import data.repository.application.service.ProcessedWordsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Processed words controller.
 */
@RestController
@RequiredArgsConstructor
public class ProcessedWordsController {
    private final ProcessedWordsService processedWordsService;

    @GetMapping(value = "/betvictor/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProcessedWordsDTO> retrieveHistory() {
        return processedWordsService.retrieveHistory();
    }
}

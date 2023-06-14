package data.processing.app.service;

import data.processing.app.exception.LoripsumException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * The type Loripsum integration service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoripsumIntegrationService {
    private final RestTemplate restTemplate;

    @Value("${loripsum.base.url}")
    private String loripsumBaseUrl;

    /**
     * Gets generated text.
     *
     * @param paragraphs the paragraphs
     * @param length     the length
     * @return the generated text
     */
    public String getGeneratedText(Integer paragraphs, String length) {
        String url = String.format("%s/%d/%s", loripsumBaseUrl, paragraphs, length);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        boolean xxSuccessful = responseEntity.getStatusCode().is2xxSuccessful();
        if (xxSuccessful) {
            return responseEntity.getBody();
        } else {
            log.error("Failed on response from Loripsum with error {}", responseEntity.getBody());
            throw new LoripsumException();
        }
    }
}

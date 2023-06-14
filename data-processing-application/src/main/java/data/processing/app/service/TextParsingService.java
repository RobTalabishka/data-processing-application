package data.processing.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.processing.app.exception.RequestDataException;
import data.processing.app.model.TextParsingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The type Text parsing service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TextParsingService {
    private final LoripsumIntegrationService loripsumService;
    private final KafkaService kafkaService;
    private final ObjectMapper objectMapper;

    @Value("${supported.paragraphs}")
    private Set<String> supportedParagraphs;
    @Value("${analiz.per.paragraph}")
    private boolean analyzPerParagraph;

    /**
     * Process request text parsing response.
     *
     * @param paragraphs the paragraphs
     * @param length     the length
     * @return the text parsing response
     */
    public TextParsingResponse processRequest(Integer paragraphs, String length) {
        long totalStart = System.currentTimeMillis();
        validateRequestParams(paragraphs, length);
        var response = new TextParsingResponse();

        String text = loripsumService.getGeneratedText(paragraphs, length);
        if (StringUtils.isBlank(text)) {
            return new TextParsingResponse();
        }
        text = text.replaceAll("<.+?>", "");

        long executionStart = System.currentTimeMillis();
        processTextData(response, text);
        long executionFinish = System.currentTimeMillis();

        response.setAvgParagraphProcessingTime(executionFinish - executionStart);

        long totalFinish = System.currentTimeMillis();
        response.setTotalProcessingTime(totalFinish - totalStart);

        sendMessageToKafkaTopic(response);
        return response;
    }

    private void sendMessageToKafkaTopic(TextParsingResponse response) {
        try {
            String jsonResponse = objectMapper.writeValueAsString(response);
            kafkaService.sendMessage(jsonResponse, generateKey(response.getFreqWord()));
        } catch (Exception ex) {
            log.error("Error on sending message to Kafka, payload {}, error {}", response, ex);
        }
    }

    private String generateKey(String freqWord) {
        return Base64.getEncoder().encodeToString(freqWord.getBytes());
    }

    private void processTextData(TextParsingResponse response, String text) {
        HashMap<String, Integer> topWordsMap = new HashMap<>();
        if (analyzPerParagraph) {
            double avgParagraphsSizeSum = 0;
            String[] txtParagraphs = text.split("\n\n");
            for (String txtParagraph : txtParagraphs) {
                double avgParagraphsSize = propagateAvgParagraphsSize(txtParagraph);
                avgParagraphsSizeSum += avgParagraphsSize;
                propagateMostFrequentWord(topWordsMap, response, txtParagraph);
            }
            response.setAvgParagraphSize(avgParagraphsSizeSum / txtParagraphs.length);

            int maxWordCount = 0;
            String topWord = "";
            for (Map.Entry<String, Integer> entry : topWordsMap.entrySet()) {
                if (entry.getValue() > maxWordCount) {
                    topWord = entry.getKey();
                }
            }
            response.setFreqWord(topWord);
        } else {
            double avgParagraphsSize = propagateAvgParagraphsSize(text);
            response.setAvgParagraphSize(avgParagraphsSize);

            propagateMostFrequentWord(topWordsMap, response, text);
            String topWord = topWordsMap.keySet().stream().findFirst().orElse("");
            response.setFreqWord(topWord);
        }
    }

    private void propagateMostFrequentWord(Map<String, Integer> topWordsMap,
                                           TextParsingResponse response, String text) {
        String noSpecCharText = text.replaceAll("[^0-9A-Za-z ]", "");

        String[] words = noSpecCharText.split(" ");

        int freq = 0;
        String topWord = "";

        for (String word : words) {
            int currentWordCount = 0;
            for (String s : words) {
                if (word.equals(s)) {
                    currentWordCount++;
                }
            }

            if (currentWordCount >= freq) {
                freq = currentWordCount;
                topWord = word;
            }
        }
        topWordsMap.put(topWord, freq);
    }

    private double propagateAvgParagraphsSize(String text) {
        String[] txtParagraphs = text.split("\n\n");

        int sum = 0;
        for (String paragraph : txtParagraphs) {
            sum += paragraph.length();
        }

        return (double) sum / txtParagraphs.length;
    }

    private void validateRequestParams(Integer paragraphs, String length) {
        if (StringUtils.isBlank(length) || !supportedParagraphs.contains(length)) {
            log.error("Wrong request param length: {}", length);
            throw new RequestDataException("Wrong value of the length param");
        }

        if (ObjectUtils.isEmpty(paragraphs) || paragraphs < 0) {
            log.error("Wrong request param paragraphs: {}", paragraphs);
            throw new RequestDataException("Wrong value of the paragraphs param");
        }
    }
}

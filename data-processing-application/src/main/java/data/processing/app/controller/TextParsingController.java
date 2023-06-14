package data.processing.app.controller;

import data.processing.app.model.TextParsingResponse;
import data.processing.app.service.TextParsingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Text parsing controller.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TextParsingController {
    private final TextParsingService textParsingService;

    /**
     * Gets parsing result.
     *
     * @param paragraphs indicates the max number of paragraphs
     * @param length     indicates length of each paragraph. Check supported.paragraphs
     *                   in the application.properties to get available values for the length param
     * @return the parsing result
     */
    @GetMapping(value = "/betvictor/text", produces = MediaType.APPLICATION_JSON_VALUE)
    public TextParsingResponse getParsingResult(@RequestParam("p") Integer paragraphs,
                                                @RequestParam("l") String length) {
        return textParsingService.processRequest(paragraphs, length);
    }
}

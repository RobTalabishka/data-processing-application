package data.repository.application.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.repository.application.model.ProcessedWordsDTO;
import data.repository.application.service.ProcessedWordsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * The type Processed words consumer.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "${kafka.topic.name}", groupId = "data",
        concurrency = "${kafka.words.processed.topic.concurrency}"
)
public class ProcessedWordsConsumer {
    private final ObjectMapper objectMapper;
    private final ProcessedWordsService processedWordsService;

    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private BlockingQueue<ProcessedWordsDTO> localQueue;

    @Value("${kafka.consumer.messages.processors.count}")
    private int messageProcessors;

    /**
     * Start up.
     */
    @PostConstruct
    public void startUp() {
        localQueue = new ArrayBlockingQueue<>(1000);
        for (int i = 0; i < messageProcessors; i++) {
            executor.execute(new Processor(localQueue));
        }
    }

    @KafkaHandler
    void integerHandler(String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        try {
            var processedWordsDTO = objectMapper.readValue(message, ProcessedWordsDTO.class);
            boolean added = localQueue.offer(processedWordsDTO);

            if (!added) {
                log.error("Failed to add a message = {} to local queue.", message);
            }
        } catch (Exception ex) {
            log.error("Error on message consuming, payload {}, error {}", message, ex);
        }
    }

    private class Processor implements Runnable {
        private final BlockingQueue<ProcessedWordsDTO> queue;
        private boolean running = true;

        public Processor(BlockingQueue<ProcessedWordsDTO> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (running) {
                try {
                    ProcessedWordsDTO processedWordsDTO = queue.take();
                    processedWordsService.processMessage(processedWordsDTO);
                } catch (InterruptedException e) {
                    log.warn("Processor thread has been asked to interrupt. This is ok"
                            + " if an only if application is shutting down.");
                    Thread.currentThread().interrupt();
                    running = false;
                } catch (Exception e) {
                    log.error("An error occurred in Processor thread", e);
                }
            }
        }
    }
}

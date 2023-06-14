package data.processing.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * The type Kafka service.
 */
@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${kafka.topic.name}")
    private String kafkaTopicName;

    /**
     * Send message.
     *
     * @param message the message
     * @param key     the key
     */
    public void sendMessage(String message, String key) {
        kafkaTemplate.send(kafkaTopicName, key, message);
    }
}

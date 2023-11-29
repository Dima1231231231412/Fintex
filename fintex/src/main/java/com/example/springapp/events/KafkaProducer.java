package com.example.springapp.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendWeatherDataEvent(MyEvent event, String topic){
        try {
            kafkaTemplate.send(topic, event);
            log.info("Producer produced the message {}", event);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class MyEvent {
        private String key;
        private Object object;
    }
}

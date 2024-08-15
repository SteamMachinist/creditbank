package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ru.neoflex.deal.configuration.KafkaTopic;
import ru.neoflex.deal.configuration.KafkaTopicsProperties;
import ru.neoflex.deal.dto.email.EmailMessage;

import java.io.Serializable;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTopicsProperties topicsProperties;

    private final KafkaTemplate<String, Serializable> kafkaTemplate;

    public void send(KafkaTopic topic, EmailMessage message) {
        ListenableFuture<SendResult<String, Serializable>> future =
                kafkaTemplate.send(topicsProperties.get(topic), message);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {

            }

            @Override
            public void onSuccess(SendResult<String, Serializable> result) {

            }
        });
    }

}

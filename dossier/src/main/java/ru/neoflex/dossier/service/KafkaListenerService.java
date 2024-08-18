package ru.neoflex.dossier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.email.EmailMessage;

@Service
@RequiredArgsConstructor
public class KafkaListenerService {

    private final EmailService emailService;

    @KafkaListener(topics = "#{@'dossier-ru.neoflex.dossier.configuration.KafkaTopicsProperties'.getAllTopics}")
    public void listenOnAll(EmailMessage emailMessage) {
        emailService.sendMessage(emailMessage);
    }

}

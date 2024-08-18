package ru.neoflex.dossier.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.neoflex.common.dto.email.KafkaTopic;
import ru.neoflex.common.dto.email.EmailMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendMessage(EmailMessage emailMessage) {
        sendMessage(
                emailMessage.address(),
                generateTheme(emailMessage.theme()),
                generateText(emailMessage));
    }

    public void sendMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("creditbank.roshchupkin.ya@yandex.ru");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    private String generateTheme(KafkaTopic topic) {
        return switch (topic) {
            case FINISH_REGISTRATION -> "Завершите регистрацию";
            case CREATE_DOCUMENTS -> "Заявка на кредит одобрена";
            case SEND_DOCUMENTS -> "Документы на кредит";
            case SEND_SES -> "Электронная подпись  документов кредита";
            case CREDIT_ISSUED -> "Кредит успешно выдан";
            case STATEMENT_DENIED -> "Заявка на кредит отклонена";
        };
    }

    private String generateText(EmailMessage emailMessage) {
        return switch (emailMessage.theme()) {
            case FINISH_REGISTRATION -> "Ваша заявка предварительно одобрена, завершите оформление";
            case CREATE_DOCUMENTS -> String.format("Заявка на кредит одобрена. Сформируйте документы здесь: %s", "ссылка");
            case SEND_DOCUMENTS -> String.format("Документы по кредиту: %s \n" +
                    "Перейдите по ссылке при согласии с условиями кредита для получения " +
                    "кода электронного подписания: %s", emailMessage.additionalInfo(), "ссылка");
            case SEND_SES -> String.format("Код электронной подписи: %s", emailMessage.additionalInfo());
            case CREDIT_ISSUED -> "Кредитный договор подписан, деньги поступят на счёт в ближайшее время";
            case STATEMENT_DENIED -> "К сожалению, вам не одобрен кредит";
        };
    }
}


package ru.neoflex.deal.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import ru.neoflex.common.dto.email.KafkaTopic;

@Configuration
@RequiredArgsConstructor
public class KafkaTopicConfiguration {

    private final KafkaTopicsProperties topicsProperties;

    @Bean
    public NewTopic finishRegistrationTopic() {
        return TopicBuilder.name(topicsProperties.get(KafkaTopic.FINISH_REGISTRATION)).build();
    }

    @Bean
    public NewTopic createDocumentsTopic() {
        return TopicBuilder.name(topicsProperties.get(KafkaTopic.CREATE_DOCUMENTS)).build();
    }

    @Bean
    public NewTopic sendDocumentsTopic() {
        return TopicBuilder.name(topicsProperties.get(KafkaTopic.SEND_DOCUMENTS)).build();
    }

    @Bean
    public NewTopic sendSesTopic() {
        return TopicBuilder.name(topicsProperties.get(KafkaTopic.SEND_SES)).build();
    }

    @Bean
    public NewTopic creditIssuesTopic() {
        return TopicBuilder.name(topicsProperties.get(KafkaTopic.CREDIT_ISSUED)).build();
    }

    @Bean
    public NewTopic statementDeniedTopic() {
        return TopicBuilder.name(topicsProperties.get(KafkaTopic.STATEMENT_DENIED)).build();
    }
}

package ru.neoflex.deal.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "deal")
public class KafkaTopicsProperties {

    private Map<String, String> topics;

    public void setTopics(Map<String, String> topics) {
        this.topics = topics;
    }

    public String get(KafkaTopic kafkaTopic) {
        return topics.get(kafkaTopic.name());
    }
}

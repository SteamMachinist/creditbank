package ru.neoflex.dossier.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.neoflex.common.dto.email.KafkaTopic;

import java.util.Map;

@ConfigurationProperties(prefix = "dossier")
public class KafkaTopicsProperties {

    private Map<String, String> topics;

    public void setTopics(Map<String, String> topics) {
        this.topics = topics;
    }

    public String get(KafkaTopic kafkaTopic) {
        return topics.get(kafkaTopic.name());
    }

    public String[] getAllTopics() {
        return topics.values().toArray(new String[0]);
    }
}

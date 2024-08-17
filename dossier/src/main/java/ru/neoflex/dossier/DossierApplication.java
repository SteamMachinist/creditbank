package ru.neoflex.dossier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.neoflex.dossier.configuration.KafkaTopicsProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = KafkaTopicsProperties.class)
public class DossierApplication {

    public static void main(String[] args) {
        SpringApplication.run(DossierApplication.class, args);
    }
}
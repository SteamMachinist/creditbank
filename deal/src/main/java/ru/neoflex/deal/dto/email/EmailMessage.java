package ru.neoflex.deal.dto.email;

import ru.neoflex.deal.configuration.KafkaTopic;
import ru.neoflex.deal.entity.Statement;

import java.io.Serializable;
import java.util.UUID;

public record EmailMessage(

        String address,
        KafkaTopic theme,
        UUID statementId,
        String info
) implements Serializable {

    public EmailMessage(String address, KafkaTopic theme, UUID statementId, String info) {
        this.address = address;
        this.theme = theme;
        this.statementId = statementId;
        this.info = info;
    }

    public EmailMessage(Statement statement, KafkaTopic theme, String info) {
        this(
                statement.getClient().getEmail(),
                theme,
                statement.getStatementId(),
                info);
    }

    public EmailMessage(Statement statement, KafkaTopic theme) {
        this(
                statement.getClient().getEmail(),
                theme,
                statement.getStatementId(),
                "");
    }
}

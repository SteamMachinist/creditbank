package ru.neoflex.deal.dto.email;

import ru.neoflex.deal.configuration.KafkaTopic;
import ru.neoflex.deal.entity.Statement;

import java.io.Serializable;
import java.util.UUID;

public record EmailMessage(

        String address,
        KafkaTopic theme,
        UUID statementId,
        String additionalInfo
) implements Serializable {

    public EmailMessage(String address, KafkaTopic theme, UUID statementId, String additionalInfo) {
        this.address = address;
        this.theme = theme;
        this.statementId = statementId;
        this.additionalInfo = additionalInfo;
    }

    public EmailMessage(Statement statement, KafkaTopic theme, String additionalInfo) {
        this(
                statement.getClient().getEmail(),
                theme,
                statement.getStatementId(),
                additionalInfo);
    }

    public EmailMessage(Statement statement, KafkaTopic theme) {
        this(
                statement.getClient().getEmail(),
                theme,
                statement.getStatementId(),
                "");
    }
}

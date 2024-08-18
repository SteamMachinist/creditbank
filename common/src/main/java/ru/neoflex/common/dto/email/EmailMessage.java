package ru.neoflex.common.dto.email;

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

    public EmailMessage(String address, KafkaTopic theme, UUID statementId) {
        this(
                address,
                theme,
                statementId,
                "");
    }
}

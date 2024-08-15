package ru.neoflex.deal.dto.email;

import ru.neoflex.deal.entity.ApplicationStatus;
import ru.neoflex.deal.entity.Statement;

import java.io.Serializable;
import java.util.UUID;

public record EmailMessage(

        String address,
        ApplicationStatus theme,
        UUID statementId
) implements Serializable {

    public EmailMessage(String address, ApplicationStatus theme, UUID statementId) {
        this.address = address;
        this.theme = theme;
        this.statementId = statementId;
    }

    public EmailMessage(Statement statement) {
        this(
                statement.getClient().getEmail(),
                statement.getStatus(),
                statement.getStatementId());
    }
}

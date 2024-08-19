package ru.neoflex.common.dto.statement;

import java.sql.Timestamp;

public record StatusHistoryElement(

        ApplicationStatus status,
        Timestamp time,
        ChangeType changeType) {
}

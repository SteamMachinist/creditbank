package ru.neoflex.deal.entity;

import java.sql.Timestamp;

public record StatusHistoryElement(

        ApplicationStatus status,
        Timestamp time,
        ChangeType changeType) {
}

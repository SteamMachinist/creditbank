package ru.neoflex.deal.entity;

import java.sql.Timestamp;

public record StatusHistoryElement(

        String status,
        Timestamp timestamp,
        ChangeType changeType) {
}

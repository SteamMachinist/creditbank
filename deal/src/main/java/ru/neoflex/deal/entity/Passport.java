package ru.neoflex.deal.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.With;

import java.time.LocalDate;
import java.util.UUID;

public record Passport(

        @JsonProperty("passport_uuid")
        UUID passportUUID,
        String series,
        String number,
        @With
        @JsonProperty("issue_branch")
        String issueBranch,
        @With
        @JsonProperty("issue_date")
        LocalDate issueDate) {
}

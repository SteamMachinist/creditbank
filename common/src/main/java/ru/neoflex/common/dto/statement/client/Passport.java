package ru.neoflex.common.dto.statement.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.With;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record Passport(

        @With
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

        public Passport withIssueBranchAndIssueDate(String issueBranch, LocalDate issueDate) {
                return this.withIssueBranch(issueBranch).withIssueDate(issueDate);
        }
}

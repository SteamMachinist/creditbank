package ru.neoflex.common.dto.statement.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.neoflex.common.dto.scoring.request.Gender;
import ru.neoflex.common.dto.scoring.request.MaritalStatus;

import java.time.LocalDate;
import java.util.UUID;

public record StatementClientDto(

        @JsonProperty("client_id")
        UUID clientId,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("middle_name")
        String middleName,
        @JsonProperty("birth_date")
        LocalDate birthDate,
        @JsonProperty("email")
        String email,
        @JsonProperty("gender")
        Gender gender,
        @JsonProperty("marital_status")
        MaritalStatus maritalStatus,
        @JsonProperty("dependent_amount")
        Integer dependentAmount,
        @JsonProperty("passport")
        Passport passport,
        @JsonProperty("employment")
        Employment employment,
        @JsonProperty("account_number")
        String accountNumber
) {
}

package ru.neoflex.deal.entity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.UUID;

public class ClientListener {

    @PrePersist
    @PreUpdate
    public void setInternalUuidsIfAbsent(Client client) {
        Passport passport = client.getPassport();
        if (passport != null && passport.passportUUID() != null) {
            client.setPassport(passport.withPassportUUID(UUID.randomUUID()));
        }

        Employment employment = client.getEmployment();
        if (employment != null && employment.employmentUUID() != null) {
            client.setEmployment(employment.withEmploymentUUID(UUID.randomUUID()));
        }
    }
}

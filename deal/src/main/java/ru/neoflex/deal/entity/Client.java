package ru.neoflex.deal.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.neoflex.calculator.dto.scoring.request.Gender;
import ru.neoflex.calculator.dto.scoring.request.MaritalStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "client")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Passport passport;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private Employment employment;

    private String accountNumber;
}
package ru.neoflex.deal.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "statement")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Statement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "statement_id", nullable = false)
    private UUID statementId;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @Enumerated(EnumType.STRING)
    ApplicationStatus status;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Timestamp creationDate;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private LoanOfferDto appliedOffer;

    private Timestamp signDate;

    String sesCode;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    List<StatusHistoryElement> statusHistory;
}

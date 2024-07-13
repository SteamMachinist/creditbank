package ru.neoflex.deal.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import io.hypersistence.utils.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import ru.neoflex.calculator.dto.offer.response.LoanOfferDto;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "statement")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)})
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

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    private LoanOfferDto appliedOffer;

    private Timestamp signDate;

    String sesCode;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @ColumnTransformer(write = "?::jsonb")
    List<StatusHistoryElement> statusHistory;

    public Statement(Client client) {
        this.client = client;
        this.status = ApplicationStatus.PREAPPROVAL;
        this.statusHistory = new ArrayList<>();
    }
}

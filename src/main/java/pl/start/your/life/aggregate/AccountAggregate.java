package pl.start.your.life.aggregate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Aggregate
@Entity
@Table(name = "ACCOUNT_AGGREGATE")
public class AccountAggregate {
    @AggregateIdentifier
    @Id
    private String id;

    private Integer balance;

}

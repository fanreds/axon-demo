package pl.start.your.life.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.commandhandling.model.AggregateRoot;
import org.axonframework.spring.stereotype.Aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Aggregate
@AggregateRoot
public class Account {
    @Id
    @AggregateIdentifier
    @GeneratedValue
    private Integer id;

    private Integer balance;

}

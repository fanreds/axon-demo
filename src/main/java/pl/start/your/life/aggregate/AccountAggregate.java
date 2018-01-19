package pl.start.your.life.aggregate;

import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Aggregate
public class AccountAggregate implements AggregateSetter<Integer>{
    @AggregateIdentifier
    private String id;

    private Integer balance;

    @Override
    public void setEntityId(Integer id) {
    }
}

package pl.start.your.life.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class IncreasedBalanceAccountEvent extends AbstractBalanceAccountEvent {

    public IncreasedBalanceAccountEvent(Integer accountId, Integer amount) {
        super(accountId, amount);
    }
}

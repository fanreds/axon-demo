package pl.start.your.life.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
public class DecreasedBalanceAccountEvent extends AbstractBalanceAccountEvent {
    public DecreasedBalanceAccountEvent(Integer accountId, Integer amount) {
        super(accountId, amount);
    }
}

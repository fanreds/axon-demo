package pl.start.your.life.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class AbstractBalanceAccountEvent {
    protected Integer accountId;
    protected Integer amount;
}

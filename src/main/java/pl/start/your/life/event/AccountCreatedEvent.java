package pl.start.your.life.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class AccountCreatedEvent {
    private Integer accountId;
    private Integer balance;
}

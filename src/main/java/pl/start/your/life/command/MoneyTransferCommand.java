package pl.start.your.life.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MoneyTransferCommand {
    private Integer accountId;
    private Integer amount;
}

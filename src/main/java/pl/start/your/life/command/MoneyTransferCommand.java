package pl.start.your.life.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MoneyTransferCommand {
    @Getter
    private Integer amount;

}

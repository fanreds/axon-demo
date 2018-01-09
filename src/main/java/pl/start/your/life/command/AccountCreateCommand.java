package pl.start.your.life.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountCreateCommand {
    private Integer accountId;
    private Integer balance;
}

package pl.start.your.life.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import pl.start.your.life.aggregate.AccountAggregate;
import pl.start.your.life.domain.Account;

@Mapper
public interface AccountMapper extends DataModelMapper<AccountAggregate, Account> {
    AccountMapper MAPPER = Mappers.getMapper(AccountMapper.class);
}

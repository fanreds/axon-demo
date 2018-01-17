package pl.start.your.life.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.start.your.life.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}

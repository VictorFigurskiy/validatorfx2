package main.repository;

import main.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The repository using Spring Data to work with MySql DB by Accounts.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByValidIsTrue();

    List<Account> findAllByValidIsTrueAndProxyIsNull();

    List<Account> findAllByValidIsFalse();
}

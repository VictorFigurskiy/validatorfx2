package main.service.dbservice;

import main.model.Account;
import main.repository.AccountRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Service to work with the repository by Accounts.
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public List<Account> accountsFromString(LinkedList<String> accounts) {
        List<Account> accountList = new ArrayList<>();

        accounts.forEach(accountStr -> {
            Account account = new Account();

            String[] elementArray = StringUtils.split(accountStr, ":");

            account.setInstagramUserName(elementArray[0]);
            account.setInstagramPassword(elementArray[1]);
            account.setEmailLogin(elementArray[2]);
            account.setEmailPassword(elementArray[3]);
            account.setValid(true);

            accountList.add(account);
        });

        return accountList;
    }

    @Transactional
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Transactional
    public Account findById(Long id) {
        return accountRepository.findById(id).isPresent() ? accountRepository.findById(id).get() : null;
    }

    @Transactional
    public boolean existsById(Long id) {
        return accountRepository.existsById(id);
    }

    @Transactional
    public long count() {
        return accountRepository.count();
    }

    @Transactional
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    @Transactional
    public void delete(Account account) {
        accountRepository.delete(account);
    }

    @Transactional
    public List<Account> saveAll(List<Account> accounts) {

        return accountRepository.saveAll(accounts);
    }

    @Transactional
    public List<Account> getAll() {

        return accountRepository.findAll();
    }

    @Transactional
    public List<Account> findAllValidAccounts() {
        return accountRepository.findAllByValidIsTrue();
    }

    @Transactional
    public List<Account> findAllValidAccountsAndProxyIsNotConnected() {
        return accountRepository.findAllByValidIsTrueAndProxyIsNull();
    }

    @Transactional
    public List<Account> findAllInvalidAccounts() {
        return accountRepository.findAllByValidIsFalse();
    }

    @Transactional
    public void deleteAllAccounts() {

        accountRepository.deleteAll();
    }

    @Transactional
    public void deleteAllByCollection(Iterable<Account> iterable) {
        accountRepository.deleteAll(iterable);
    }

    @Transactional
    public void deleteAllBadAccounts() {

        List<Account> badAccounts = findAllInvalidAccounts();

        for (Account badAccount : badAccounts) {

            badAccount.setProxy(null);
        }

        saveAll(badAccounts);
        deleteAllByCollection(badAccounts);
    }

    @Transactional
    public void updateValidatedAccounts(List<Account> validatedAccounts) {

        List<Account> badAccounts = findAllInvalidAccounts();

        for (Account validatedAccount : validatedAccounts) {

            if(badAccounts.contains(validatedAccount)){
                validatedAccount.setValid(true);
            }
        }
        saveAll(badAccounts);
    }
}


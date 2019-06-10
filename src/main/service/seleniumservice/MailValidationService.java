package main.service.seleniumservice;

import main.exceptions.ProxyNotFoundException;
import main.model.Account;
import main.model.Proxy;
import main.service.dbservice.AccountService;
import main.service.dbservice.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MailValidationService {

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private AccountService accountService;



    public List<Account> verifyAllAccounts() throws ProxyNotFoundException {

        Proxy proxy = getProxy();
        Account account = getAccounts().get(0);

        boolean isValidated = new SeleniumChromeMailCaptchaService().validateMailAccount(account, proxy);
        List<Account> validatedAccounts = new ArrayList<>();
        return new ArrayList<>();
    }

    public Proxy getProxy() throws ProxyNotFoundException {

        List<Proxy> proxies = proxyService.getAll();

        if (proxies != null) {

            Collections.shuffle(proxies);

            return getNullableProxy(proxies);
        } else {
            throw new ProxyNotFoundException("Proxy was not extracted from DB! Please check available proxy list!!!");
        }
    }

    private Proxy getNullableProxy(List<Proxy> proxies) throws ProxyNotFoundException {

        Proxy proxy = proxies.get(0);

        if (proxy != null) {

            return proxy;
        } else {
            if (proxies.size() > 1) {
                List<Proxy> tempProxyList = proxies.subList(1, proxies.size());
                return getNullableProxy(tempProxyList);
            }
        }

        throw new ProxyNotFoundException("Proxy was not extracted from DB! Please check available proxy list!!!");
    }

    public List<Account> getAccounts() {

        return accountService.getAll();
    }

}

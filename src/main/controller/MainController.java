package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import main.exceptions.ProxyNotFoundException;
import main.model.Account;
import main.model.Proxy;
import main.service.FileService;
import main.service.ValidationService;
import main.service.dbservice.AccountService;
import main.service.dbservice.ProxyService;
import main.service.seleniumservice.MailValidationService;
import main.service.seleniumservice.SeleniumChromeMailCaptchaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class MainController {

    @FXML
    public AnchorPane rootPane;
    public Pane mainPane;
    public HBox mainHBox;
    public Pane mainOperationPane;
    public TextArea mainOuputTextArea;
    public Button setAccountButton;
    public Button getAccountButton;
    public Button validateAccountButton;
    public Button deleteBadAccountButton;
    public Button deleteAllAccountButton;
    public Button setProxiesButton;
    public Button getProxiesButton;
    public Button deleteBadProxiesButton;
    public Button deleteAllProxiesButton;
    public Button chooseProxiesButton;
    public Button chooseAccountsButton;
    public TextField proxyChooserTextField;
    public TextField accountChooserTextField;
    public Button validateFilteredAccountButton;
    public TextField validateAccountFromTextArea;
    public TextField validateAccountToTextArea;

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MailValidationService mailValidationService;

    @Autowired
    private SeleniumChromeMailCaptchaService seleniumChromeMailCaptchaService;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private FileService fileService;

    private File accountsFile;

    private File proxiesFile;


    @FXML
    public void initialize() {

    }

    public void chooseAccountsFile() {
        File file = fileService.chooseFile();

        if (file != null) {
            accountChooserTextField.setText(file.getName());
            accountsFile = file;
        }
    }

    public void chooseProxiesFile() {
        File file = fileService.chooseFile();

        if (file != null) {
            proxyChooserTextField.setText(file.getName());
            proxiesFile = file;
        }
    }

    public void setAccount()
    {
        List<Account> accounts = new ArrayList<>();

        if (accountsFile !=null)
        {
            accounts = fileService.adaptAccountData(accountsFile);
        }

        List <Account> accountsFromDB = accountService.saveAll(accounts);
        mainOuputTextArea.appendText(accountsFromDB.size() + " accounts from " + accounts.size() + " added" + "\n");
        accountChooserTextField.setText("");
        accountsFile = null;
    }

    public void getAccount()
    {
        List <Account> accounts = accountService.getAll();
        accounts.forEach(account -> mainOuputTextArea.appendText(account.toString() + "\n"));
    }

    public void validateAllAccounts() {
        printToTextArea("Start validate accounts");
        List<Account> accounts = mailValidationService.getAccounts();
        validateAccounts(accounts, 0, accounts.size());
    }

    public void validateFilteredAccount() {
        String from = validateAccountFromTextArea.getText();
        String to = validateAccountToTextArea.getText();
        int fromInt = 0;
        int toInt = 0;
        List<Account> accounts = mailValidationService.getAccounts();
        if (StringUtils.isNumeric(from) && StringUtils.isNumeric(to)) {
            int tempFrom = Integer.valueOf(from);
            int tempTo = Integer.valueOf(to);
            if ((tempTo >= 0 && tempTo < accounts.size()) && (tempFrom >= 0 && tempFrom < accounts.size() && tempFrom <= tempTo)) {
                fromInt = tempFrom;
                toInt = tempTo;
            } else {
                printToTextArea("Wrong \"from\" and \"to\" parameters");
            }
        } else {
            printToTextArea("Parameters should be numeric");
        }

        if (fromInt != 0 || toInt != 0) {
            printToTextArea("Start validate accounts");
            validateAccounts(accounts, fromInt, toInt + 1);
        }
    }

    private void validateAccounts(List<Account> accounts, int a, int b) {
        List<Account> validatedAccounts = new ArrayList<>();
        for (int n = a; n < b; n++) {
            try {
                Proxy proxy = mailValidationService.getProxy();
                boolean isValidated = seleniumChromeMailCaptchaService.validateMailAccount(accounts.get(n), proxy);
                if (isValidated) {
                    validatedAccounts.add(accounts.get(n));
                    printToTextArea("Mail account " + accounts.get(n).getEmailLogin() + " successfully validated with proxies " + proxy);
                } else {
                    printToTextArea("Account " + accounts.get(n).getEmailLogin() + " was not validated with proxies " + proxy);
                }
                } catch (ProxyNotFoundException e) {
                printToTextArea("Account " + accounts.get(n).getEmailLogin() + " was not validated");
                printToTextArea(e.getMessage());
            }
        }
        accountService.updateValidatedAccounts(validatedAccounts);
    }

    private void printToTextArea(String message) {
        mainOuputTextArea.appendText(message + "\n");
    }

    public void deleteBadAccount()
    {
        accountService.deleteAllBadAccounts();
        mainOuputTextArea.appendText("***Bad accounts deleted***" + "\n");
    }

    public void deleteAllAccount()
    {
        accountService.deleteAllAccounts();
        mainOuputTextArea.appendText("***All accounts deleted***" + "\n");
    }

    public void setProxies()
    {
        List<Proxy> proxies = new ArrayList<>();

        if (proxiesFile !=null){

            proxies = fileService.adaptProxyData(proxiesFile);
        }

        List <Proxy> proxiesFromDB = proxyService.saveAll(proxies);
        mainOuputTextArea.appendText(proxiesFromDB.size() + " proxies from " + proxies.size() + " added" + "\n");
        proxyChooserTextField.setText("");
        proxiesFile = null;
    }

    public void getProxies()
    {
        List <Proxy> proxies = proxyService.getAll();
        proxies.forEach(proxy -> mainOuputTextArea.appendText(proxy.toString() + "\n"));
    }

    public void deleteBadProxies()
    {
        proxyService.deleteAllBadProxies();
        mainOuputTextArea.appendText("***Bad proxies deleted***" + "\n");
    }

    public void deleteAllProxies()
    {
        proxyService.deleteAllProxies();
        mainOuputTextArea.appendText("***All proxies deleted***" + "\n");
    }
}


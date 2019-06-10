package main.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import main.model.Account;
import main.model.Proxy;
import main.service.FileService;
import main.service.ValidationService;
import main.service.dbservice.AccountService;
import main.service.dbservice.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class MainController {

    @FXML
    public AnchorPane rootPane;

    @FXML
    public Pane mainPane;

    @FXML
    public HBox mainHBox;

    @FXML
    public Pane mainOperationPane;

    @FXML
    public TextArea mainOuputTextArea;

    @FXML
    public Button setAccountButton;

    @FXML
    public Button getAccountButton;

    @FXML
    public Button validateAccountButton;

    @FXML
    public Button deleteBadAccountButton;

    @FXML
    public Button deleteAllAccountButton;

    @FXML
    public Button setProxiesButton;

    @FXML
    public Button getProxiesButton;

    @FXML
    public Button deleteBadProxiesButton;

    @FXML
    public Button deleteAllProxiesButton;

    @FXML
    public Button chooseProxiesButton;

    @FXML
    public Button chooseAccountsButton;

    @FXML
    public TextField proxyChooserTextField;

    @FXML
    public TextField accountChooserTextField;

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private AccountService accountService;

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

    public void validateAccounts()
    {
        validationService.run();
        mainOuputTextArea.appendText("");
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


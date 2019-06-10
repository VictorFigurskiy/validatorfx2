package main.service;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.model.Account;
import main.model.Proxy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    public File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);

        return selectedFile;
    }

    public List<Account> adaptAccountData(File accountsFile) {

        List<String> accountsString = getElements(accountsFile);
        List<Account> accounts = new ArrayList<>();

        for (String accountString : accountsString) {
            String[] accountSplit = accountString.split(",");
            Account account = new Account();
            account.setEmailLogin(accountSplit[0]);
            account.setEmailPassword(accountSplit[1]);
            account.setInstagramUserName(accountSplit[2]);
            account.setInstagramPassword(accountSplit[3]);
            account.setValid(true);
            accounts.add(account);
        }

        return accounts;
    }

    public List<Proxy> adaptProxyData(File proxiesFile) {

        List<String> proxiesString = getElements(proxiesFile);
        List<Proxy> proxies = new ArrayList<>();

        for (String proxyStringRaw : proxiesString) {
            String[] proxySplit = StringUtils.substringBetween(proxyStringRaw, "//", "',").split(":|@");

            Proxy proxy = new Proxy();
            proxy.setLogin(proxySplit[0]);
            proxy.setPassword(proxySplit[1]);
            proxy.setHost(proxySplit[2]);
            proxy.setPort(proxySplit[3]);
            proxy.setProxyBusy(false);
            proxy.setFailRatioStatus(10);
            proxies.add(proxy);
        }

        return proxies;
    }

    private List<String> getElements(File file) {

        List<String> elementsString = new ArrayList<>();

        try {
            elementsString = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return elementsString;
    }
}

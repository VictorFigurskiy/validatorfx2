package main.controller;

import com.google.gson.JsonObject;
import main.service.dbservice.AccountService;
import main.service.dbservice.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

@RestController
@RequestMapping("/api/db")
public class RESTDataBaseController {

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/proxy")
    public ResponseEntity<?> setAllProxies(@RequestBody LinkedList<String> proxies) {

        return new ResponseEntity<>(proxyService.saveAll(proxyService.proxiesFromString(proxies)), HttpStatus.OK);
    }

    @PostMapping("/account")
    public ResponseEntity<?> setAllAccounts(@RequestBody LinkedList<String> accounts){

        return new ResponseEntity<>(accountService.saveAll(accountService.accountsFromString(accounts)), HttpStatus.OK);
    }

    @DeleteMapping("/account/delete")
    public ResponseEntity<?> deleteAllAccounts() {

        accountService.deleteAllAccounts();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message","All accounts was deleted");

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @DeleteMapping("/account/delete/{accountId}")
    public ResponseEntity<?> deleteAccountById(@PathVariable Long accountId) {

        accountService.deleteById(accountId);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message","Account with id '" + accountId + "' was deleted");

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @DeleteMapping("/proxy/delete")
    public ResponseEntity<?> deleteAllProxies() {

        proxyService.deleteAllProxies();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message","All proxies was deleted");

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @DeleteMapping("/proxy/delete/{accountId}")
    public ResponseEntity<?> deleteProxyById(@PathVariable Long proxyId) {

        proxyService.deleteById(proxyId);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message","Proxy with id '" + proxyId + "' was deleted");

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @DeleteMapping("/proxy/delete/bad")
    public ResponseEntity<?> deleteBadProxies() {

        proxyService.deleteAllBadProxies();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message","All bad proxies was deleted");

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @DeleteMapping("/account/delete/bad")
    public ResponseEntity<?> deleteBadAccounts() {

        accountService.deleteAllBadAccounts();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message","All bad accounts was deleted");

        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }
}

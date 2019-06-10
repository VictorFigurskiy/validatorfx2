package main.service.seleniumservice;

import io.github.bonigarcia.wdm.WebDriverManager;
import main.exceptions.SeleniumException;
import main.model.Account;
import main.model.Proxy;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;

import static io.github.bonigarcia.wdm.DriverManagerType.CHROME;

@Service
public class SeleniumChromeMailCaptchaService {

    private static final int TIMEOUT_IN_SECONDS = 10;
    private static final int WAIT_CAPTCHA_MANUAL_INPUT = 40;
    private static final int MAX_SELENIUM_RETRY = 3;


    private int count;

    public SeleniumChromeMailCaptchaService() {
    }

    public boolean validateMailAccount(Account account, Proxy proxy) {

        String portAndProxy = proxy.getHost() + ":" + proxy.getPort();

        WebDriverManager.getInstance(CHROME).setup();
        WebDriver loggedDriver;
        try {
            loggedDriver = loginToMailWithValidatedProxies(account.getEmailLogin(), account.getEmailPassword(), portAndProxy, proxy.getLogin(), proxy.getPassword());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());

            if (count < MAX_SELENIUM_RETRY) {
                count++;
                return validateMailAccount(account, proxy);
            } else {
                System.out.println("Can't validate account: " + account + " !!!");

                return false;
            }
        }

        if (checkIsCaptcha(loggedDriver, account)) {
            loggedDriver.close();
            loggedDriver.quit();
            System.out.println(("Captcha input failed for account with UserName: " + account.getInstagramUserName()));

            return false;
        }

        try {
            return isMailBoxAvailable(loggedDriver);
        } catch (SeleniumException e) {

            System.out.println(e.getMessage());

            if (count < MAX_SELENIUM_RETRY) {
                count++;
                validateMailAccount(account, proxy);
            }

            return false;
        }
    }

    private boolean isMailBoxAvailable(WebDriver loggedDriver) throws SeleniumException {
        try {
            String firstLetterPath = "//*[@id='b-letters']/div/div[2]/div/div[2]/div[1]/div/a/div[4]/div[3]/div[1]/div";
            waitElement(firstLetterPath, TIMEOUT_IN_SECONDS, loggedDriver);

            System.out.println("Success! Mail box opened!");

            return true;

        } catch (TimeoutException e) {

            throw new SeleniumException("Catch TimeoutException during opening mail box");
        } finally {
            loggedDriver.close();
            loggedDriver.quit();
        }
    }

    /*Login to mail with current proxy (oxylab)*/
    private WebDriver loginToMailWithValidatedProxies(String mail, String password, String mailProxy, String proxyUser, String proxyPassword) throws SeleniumException {
        WebDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addExtensions(new File(getClass().getResource("/selenium/chromedriver/Proxy-Auto-Auth_v2.0.crx").toURI().getPath()));
            options.addArguments("start-maximized");
            options.addArguments("--proxy-server=http://" + mailProxy);

            ChromeDriverService service = new ChromeDriverService.Builder().withSilent(true).build();

            driver = new ChromeDriver(service, options);

            driver.findElement(By.xpath("//*[@id='login']")).sendKeys(proxyUser);
            driver.findElement(By.xpath("//*[@id='password']")).sendKeys(proxyPassword);
            driver.findElement(By.xpath("//*[@id='save']")).click();

            return handleMailLoginForm(driver, mail, password);
        } catch (URISyntaxException e) {

            throw new SeleniumException("Caught URISyntaxException while opening mail.ru with oxylab proxies.");
        } catch (TimeoutException e) {
            if (driver != null) {
                driver.close();
                driver.quit();
            }

            throw new SeleniumException("Caught TimeoutException while opening mail.ru with oxylab proxies.");
        }

    }

    private WebDriver handleMailLoginForm(WebDriver driver, String mail, String password) {
        rest(2);
        driver.get("https://mail.ru");
        rest(2);
        /*try {
            driver.switchTo().window("https://mail.ru");
        } catch (Exception ignored) {
        }*/
        driver.get("https://mail.ru");
        waitElement("//*[@id='mailbox:login']", TIMEOUT_IN_SECONDS, driver);
        driver.findElement(By.xpath("//*[@id='mailbox:login']")).sendKeys(mail);
        waitElement("//*[@id='mailbox:password']", TIMEOUT_IN_SECONDS, driver);
        driver.findElement(By.xpath("//*[@id='mailbox:password']")).sendKeys(password);
        driver.findElement(By.xpath("//*[@id='mailbox:submit']/input")).sendKeys(Keys.ENTER);
        return driver;
    }

    private void waitElement(String element, int timeout, WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element)));
    }

    private void rest(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean checkIsCaptcha(WebDriver loggedDriver, Account account) {
        try {
            rest(WAIT_CAPTCHA_MANUAL_INPUT);
            boolean isCaptcha = loggedDriver.findElement(By.className("b-panel__content__desc")).getText().contains("Введите код с картинки, чтобы войти в аккаунт");
            if (isCaptcha) {
                System.out.println("Captcha for "  + account.getEmailLogin() + " !!! Enter captcha in browser, please!");
                try{
                    InputStream inputStream = getClass().getResourceAsStream(getClass().getResource("/resourses/consequence.mp3").toURI().getPath());
                    AudioStream audioStream = new AudioStream(inputStream);
                    AudioPlayer.player.start(audioStream);
                } catch (Exception ignore) {
                }
            }

            return isCaptcha;
        } catch (NoSuchElementException e) {

            return false;
        }
    }
}

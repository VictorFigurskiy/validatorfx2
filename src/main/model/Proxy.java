package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * POJO to store all needed data about Proxies.
 */
@Entity
public class Proxy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Host required")
    private String host;

    @NotNull(message = "Port required")
    private String port;

    @NotNull(message = "Login required")
    private String login;

    @NotNull(message = "Password required")
    private String password;

    private Integer failRatioStatus;

    private Boolean isProxyBusy;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "proxy", cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Account account;

    public Proxy() {
    }

    public Proxy(Long id, String host, String port, String login, String password, Integer failRatioStatus) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
        this.failRatioStatus = failRatioStatus;
    }

    public Proxy(Long id, @NotNull(message = "Host required") String host, @NotNull(message = "Port required") String port, @NotNull(message = "Login required") String login, @NotNull(message = "Password required") String password, Integer failRatioStatus, Boolean isProxyBusy, Account account) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
        this.failRatioStatus = failRatioStatus;
        this.isProxyBusy = isProxyBusy;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getFailRatioStatus() {
        return failRatioStatus;
    }

    public void setFailRatioStatus(Integer failRatioStatus) {
        this.failRatioStatus = failRatioStatus;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        if (account == null) {
            if (this.account != null) {
                this.account.setProxy(null);
            }
        } else {
            account.setProxy(this);
        }

        this.account = account;
    }

    public Boolean getProxyBusy() {
        return isProxyBusy;
    }

    public void setProxyBusy(Boolean proxyBusy) {
        isProxyBusy = proxyBusy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proxy proxy = (Proxy) o;
        return Objects.equals(getId(), proxy.getId()) &&
                Objects.equals(getHost(), proxy.getHost()) &&
                Objects.equals(getPort(), proxy.getPort()) &&
                Objects.equals(getLogin(), proxy.getLogin()) &&
                Objects.equals(getPassword(), proxy.getPassword()) &&
                Objects.equals(getFailRatioStatus(), proxy.getFailRatioStatus()) &&
                Objects.equals(isProxyBusy, proxy.isProxyBusy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getHost(), getPort(), getLogin(), getPassword(), getFailRatioStatus(), isProxyBusy);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", failRatioStatus=" + failRatioStatus +
                ", isProxyBusy=" + isProxyBusy +
                '}';
    }
}

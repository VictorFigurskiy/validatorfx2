package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * POJO to store all needed data about Istagram Accounts.
 */
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Insta login required")
    @Column(unique = true)
    private String instagramUserName;

    @NotNull(message = "Insta password required")
    private String instagramPassword;

    @NotNull(message = "Email login required")
    @Column(unique = true)
    private String emailLogin;

    @NotNull(message = "Email password required")
    private String emailPassword;

    private Boolean valid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proxy_id")
    private Proxy proxy;

    public Account(Long id, @NotNull(message = "Insta login required") String instagramUserName, @NotNull(message = "Insta password required") String instagramPassword, @NotNull(message = "Email login required") String emailLogin, @NotNull(message = "Email password required") String emailPassword, Boolean valid, Proxy proxy) {
        this.id = id;
        this.instagramUserName = instagramUserName;
        this.instagramPassword = instagramPassword;
        this.emailLogin = emailLogin;
        this.emailPassword = emailPassword;
        this.valid = valid;
        this.proxy = proxy;
    }

    public Account() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstagramUserName() {
        return instagramUserName;
    }

    public void setInstagramUserName(String instagramUserName) {
        this.instagramUserName = instagramUserName;
    }

    public String getInstagramPassword() {
        return instagramPassword;
    }

    public void setInstagramPassword(String instagramPassword) {
        this.instagramPassword = instagramPassword;
    }

    public String getEmailLogin() {
        return emailLogin;
    }

    public void setEmailLogin(String emailLogin) {
        this.emailLogin = emailLogin;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(getId(), account.getId()) &&
                Objects.equals(getInstagramUserName(), account.getInstagramUserName()) &&
                Objects.equals(getInstagramPassword(), account.getInstagramPassword()) &&
                Objects.equals(getEmailLogin(), account.getEmailLogin()) &&
                Objects.equals(getEmailPassword(), account.getEmailPassword()) &&
                Objects.equals(getValid(), account.getValid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getInstagramUserName(), getInstagramPassword(), getEmailLogin(), getEmailPassword(), getValid());
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", instagramUserName='" + instagramUserName + '\'' +
                ", instagramPassword='" + instagramPassword + '\'' +
                ", emailLogin='" + emailLogin + '\'' +
                ", emailPassword='" + emailPassword + '\'' +
                ", valid=" + valid +
                '}';
    }
}

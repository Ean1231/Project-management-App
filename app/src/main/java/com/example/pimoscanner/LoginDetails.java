package com.example.pimoscanner;

public class LoginDetails {
    private String emailLogin;
    private  String password;

    public LoginDetails(String emailLogin, String password) {
        this.emailLogin = emailLogin;
        this.password = password;
    }

    public String getEmailLogin() {
        return emailLogin;
    }

    public void setEmailLogin(String emailLogin) {
        this.emailLogin = emailLogin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

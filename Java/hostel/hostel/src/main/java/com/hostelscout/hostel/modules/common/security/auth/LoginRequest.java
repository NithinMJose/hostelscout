package com.hostelscout.hostel.modules.common.security.auth;

public class LoginRequest {
    private String usernameOrEmail;
    private String password;

    // Getters and setters
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }
    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

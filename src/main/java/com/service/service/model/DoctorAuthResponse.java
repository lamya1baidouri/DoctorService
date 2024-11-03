package com.service.service.model;


import java.util.List;

public class DoctorAuthResponse {
    private String email;
    private String password;
    private List<String> roles;

    // Constructeurs, getters et setters
    public DoctorAuthResponse(String email, String password, List<String> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}

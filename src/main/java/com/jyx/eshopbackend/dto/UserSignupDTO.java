package com.jyx.eshopbackend.dto;

import com.jyx.eshopbackend.model.Role;

public class UserSignupDTO extends UserDTO{

    private final String email;
    private final String phoneNumber;
    private final String password;
    private final Role role;

    public UserSignupDTO(String username, String email, String phoneNumber, String password, Role role) {
        super(username);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
    }


    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}

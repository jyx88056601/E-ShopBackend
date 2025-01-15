package com.jyx.eshopbackend.dto;

public class UserLoginDTO extends UserDTO{
    private final String password;

    public UserLoginDTO(String username, String password) {
        super(username);
        this.password = password;
    }

    public String getUsername() {
        return super.getUsername();
    }

    public String getPassword() {
        return this.password;
    }

}

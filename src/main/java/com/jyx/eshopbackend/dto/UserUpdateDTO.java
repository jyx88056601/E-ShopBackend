package com.jyx.eshopbackend.dto;

public class UserUpdateDTO extends UserDTO{

    private final Long id;

    private final String oldPassword;

    private final String newPassword;

    private final String newUsername;

    private final String newEmail;

    private final String newPhoneNumber;

    public UserUpdateDTO(String id,String oldPassword, String newPassword, String newUsername,String newEmail, String newPhoneNumber) {
        super(newUsername);
        this.id = Long.valueOf(id);
        this.newEmail = newEmail;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newUsername = newUsername;
        this.newPhoneNumber = newPhoneNumber;
    }

    public Long getId() {
        return id;
    }
    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public String getNewPhoneNumber() {
        return newPhoneNumber;
    }
}

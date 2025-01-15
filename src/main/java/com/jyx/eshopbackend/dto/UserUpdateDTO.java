package com.jyx.eshopbackend.dto;

public class UserUpdateDTO extends UserDTO{

    private final String oldPassword;

    private final String newPassword;

    private final String newUsername;

    private final String newEmail;

    private final String newPhoneNumber;

    public UserUpdateDTO(String username, String newUsername, String oldPassword, String newPassword, String email, String phoneNumber) {
        super(username);
        this.newEmail = email;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newUsername = newUsername;
        this.newPhoneNumber = phoneNumber;
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

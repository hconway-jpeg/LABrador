package org.launchcode.LABrador.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PwFormDTO {

    @NotNull
    @NotBlank
    @Size(min = 8, max = 64, message = "Invalid Password. Must be between 8 and 64 characters.")
    private String password;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 64, message = "Invalid Password. Must be between 8 and 64 characters.")
    private String newPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String verifyNewPassword;

    public String getNewPassword() { return newPassword; }

    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getVerifyNewPassword() {
        return verifyNewPassword;
    }

    public void setVerifyNewPassword(String verifyPassword) {
        this.verifyNewPassword = verifyPassword;
    }
}

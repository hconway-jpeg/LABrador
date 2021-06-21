package org.launchcode.LABrador.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DeleteFormDTO {

    @NotNull
    @NotBlank
    @Size(min = 8, max = 64, message = "Invalid Password. Must be between 8 and 64 characters.")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

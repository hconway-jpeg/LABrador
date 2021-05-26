package org.launchcode.LABrador.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LabFormDTO {

    @NotBlank
    @Size(min = 10, message = "Please Enter a 10-digit Passcode")
    private String passcode;

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }
}

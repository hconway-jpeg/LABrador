package org.launchcode.LABrador.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LabFormDTO {

    private String pcCheck = "";

    public String getPcCheck() {
        return pcCheck;
    }

    public void setPcCheck(String pcCheck) {
        this.pcCheck = pcCheck;
    }
}

package org.launchcode.LABrador.models.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LabFormDTO {

    private String pcCheck;

    private String labName;

    public String getPcCheck() {
        return pcCheck;
    }

    public void setPcCheck(String pcCheck) {
        this.pcCheck = pcCheck;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }
}

package org.launchcode.LABrador.models.dto;

import org.launchcode.LABrador.models.Lab;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditFormDTO{

    private String email;

    private String firstName;

    private String lastName;

    private Lab lab;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 64, message = "Invalid Password. Must be between 8 and 64 characters.")
    private String password;

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Lab getLab() {
        return lab;
    }

    public void setLab(Lab lab) {
        this.lab = lab;
    }
}

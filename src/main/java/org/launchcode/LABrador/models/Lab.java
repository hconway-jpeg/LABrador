package org.launchcode.LABrador.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class Lab extends AbstractEntity {

    //PI
    @NotBlank
    @Size(min = 3)
    private String principalInvestigator;

    @NotBlank(message = "Please Enter a Valid Email Address")
    private String contactEmail;

    @NotBlank(message = "Please Enter a Lab Name")
    private String labName;

    @ManyToMany(mappedBy = "labs")
    private final List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "lab", cascade = CascadeType.REMOVE)
    private List<Animal> colony = new ArrayList<>();

    @OneToMany(mappedBy = "lab", cascade = CascadeType.REMOVE)
    private List<Genotype> genotypes = new ArrayList<>();

    private String organization;

    private String department;

    @NotBlank
    @Size(min = 10, message = "Please Enter a 10-digit Passcode")
    private String passcode;

    public Lab() { }

    public Lab(String principalInvestigator, String labName, String organization, String department, String contactEmail, String passcode) {
        this.principalInvestigator = principalInvestigator;
        this.labName = labName;
        this.organization = organization;
        this.department = department;
        this.contactEmail = contactEmail;
        this.passcode = passcode;
    }

    @Override
    public String toString() {
        return labName;
    }

    public String getPrincipalInvestigator() {
        return principalInvestigator;
    }
    public void setPrincipalInvestigator(String principalInvestigator) {
        this.principalInvestigator = principalInvestigator;
    }

    public String getLabName() {
        return labName;
    }
    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getOrganization() {
        return organization;
    }
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }

    public String getContactEmail() {
        return contactEmail;
    }
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getPasscode() { return passcode; }
    public void setPasscode(String passcode) { this.passcode = passcode; }

    public List<User> getUsers() {
        return users;
    }
    public void addUser(User user) { this.users.add(user); }

    public List<Animal> getColony() {
        return colony;
    }
    public void setColony(List<Animal> colony) {
        this.colony = colony;
    }

    public List<Genotype> getGenotypes() { return genotypes; }
    public void setGenotypes(List<Genotype> genotypes) { this.genotypes = genotypes; }

}
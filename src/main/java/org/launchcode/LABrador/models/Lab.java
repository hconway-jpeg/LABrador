package org.launchcode.LABrador.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.*;

@Entity
public class Lab extends AbstractEntity {

    //PI
    private String principalInvestigator;

    private String contactEmail;

    private String labName;

    @OneToMany(mappedBy = "lab")
    private final List<User> labMembers = new ArrayList<>();

    @OneToMany(mappedBy = "lab")
    private final List<Animal> colony = new ArrayList<>();

    private String organization;

    private String department;

    public Lab() { }

    public Lab(String principalInvestigator, String labName, String organization, String department, String contactEmail) {
        this.principalInvestigator = principalInvestigator;
        this.labName = labName;
        this.organization = organization;
        this.department = department;
        this.contactEmail = contactEmail;
    }

    @Override
    public String toString() {
        return labName;
    }

    public List<User> getLabMembers() {
        return labMembers;
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
}
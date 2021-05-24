package org.launchcode.LABrador.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity
public class Animal extends AbstractEntity {

    private String tag;
    private String cageNumber;
    private String cageType;
    private String sex;
    private String dateOfBirth;

    @ManyToOne
    private Genotype genotype;

    @ManyToOne
    private Lab lab;

    private String litter;

    @Size(max = 30, message = "Keyword is too long.")
    private String notesKeyword;
    @Size(max = 250, message = "Description is too long.")
    private String notesDescription;

    public Animal() { }

    public Animal(String tag, String cageNumber, String cageType, String sex, String dateOfBirth, Genotype genotype, String litter, String notesKeyword, String notesDescription, Lab lab) {
        super();
        this.tag = tag;
        this.cageNumber = cageNumber;
        this.cageType = cageType;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.genotype = genotype;
        this.litter = litter;
        this.notesKeyword = notesKeyword;
        this.notesDescription = notesDescription;
        this.lab = lab;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCageNumber() {
        return cageNumber;
    }
    public void setCageNumber(String cageNumber) {
        this.cageNumber = cageNumber;
    }

    public String getCageType() {
        return cageType;
    }
    public void setCageType(String cageType) {
        this.cageType = cageType;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Genotype getGenotype() {
        return genotype;
    }
    public void setGenotype(Genotype genotype) {
        this.genotype = genotype;
    }

    public String getLitter() {
        return litter;
    }
    public void setLitter(String litter) {
        this.litter = litter;
    }

    public String getNotesKeyword() {return notesKeyword;}
    public void setNotesKeyword(String notesKeyword) {this.notesKeyword = notesKeyword;}

    public String getNotesDescription() {return notesDescription;}
    public void setNotesDescription(String notesDescription) {this.notesDescription = notesDescription;}

    public Lab getLab() { return lab; }
    public void setLab(Lab lab) { this.lab = lab; }

    @Override
    public String toString() {
        return tag;
    }

}

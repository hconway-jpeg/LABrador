package org.launchcode.LABrador.models;

import javax.persistence.Entity;

@Entity
public class Animal extends AbstractEntity {

    private String tag;
    private String cageNumber;
    private String cageType;
    private String sex;
    private String dateOfBirth;
    private String genotypeOne;
    private String genotypeTwo;
    private String litter;
    private String notesTitle;
    private String notesDescription;

    public Animal() { }

    public Animal(String tag, String cageNumber, String cageType, String sex, String dateOfBirth, String genotypeOne, String genotypeTwo, String litter, String notesTitle, String notesDescription) {
        super();
        this.tag = tag;
        this.cageNumber = cageNumber;
        this.cageType = cageType;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.genotypeOne = genotypeOne;
        this.genotypeTwo = genotypeTwo;
        this.litter = litter;
        this.notesTitle = notesTitle;
        this.notesDescription = notesDescription;
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

    public String getGenotypeOne() {
        return genotypeOne;
    }
    public void setGenotypeOne(String genotypeOne) {
        this.genotypeOne = genotypeOne;
    }

    public String getGenotypeTwo() {
        return genotypeTwo;
    }
    public void setGenotypeTwo(String genotypeTwo) {
        this.genotypeTwo = genotypeTwo;
    }

    public String getLitter() {
        return litter;
    }
    public void setLitter(String litter) {
        this.litter = litter;
    }

    public String getNotesTitle() {return notesTitle;}
    public void setNotesTitle(String notesTitle) {this.notesTitle = notesTitle;}

    public String getNotesDescription() {return notesDescription;}
    public void setNotesDescription(String notesBody) {this.notesDescription = notesBody;}

    @Override
    public String toString() {
        return tag;
    }

}

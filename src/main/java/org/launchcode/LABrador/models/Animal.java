package org.launchcode.LABrador.models;

import javax.persistence.Entity;
import java.util.Date;
import java.util.Objects;

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
    private String notes;

    public Animal() { }

    public Animal(String tag, String cageNumber, String cageType, String sex, String dateOfBirth, String genotypeOne, String genotypeTwo, String litter, String notes) {
        super();
        this.tag = tag;
        this.cageNumber = cageNumber;
        this.cageType = cageType;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.genotypeOne = genotypeOne;
        this.genotypeTwo = genotypeTwo;
        this.litter = litter;
        this.notes = notes;
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

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return tag;
    }

}

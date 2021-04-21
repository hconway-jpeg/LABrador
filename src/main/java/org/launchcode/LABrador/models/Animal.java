package org.launchcode.LABrador.models;

import java.util.Date;
import java.util.Objects;

public class Animal {

    private int id;
    private String tag;
    private String cageNumber;
    private String cageType;
    private char sex;
    private Date dateCageOpen;
    private Date dateOfBirth;
    private String genotypeOne;
    private String genotypeTwo;
    private String litter;
    private String notes;

    public Animal() { }

    public int getId() {
        return id;
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

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public Date getDateCageOpen() {
        return dateCageOpen;
    }

    public void setDateCageOpen(Date dateCageOpen) {
        this.dateCageOpen = dateCageOpen;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

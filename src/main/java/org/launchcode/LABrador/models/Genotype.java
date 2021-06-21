package org.launchcode.LABrador.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Genotype extends AbstractEntity {

    private String name;

    @OneToMany(mappedBy = "genotype")
    private final List<Animal> animals = new ArrayList<>();

    @ManyToOne
    private Lab lab;

    public Genotype() {}

    public Genotype(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public Lab getLab() { return lab; }
    public void setLab(Lab lab) { this.lab = lab; }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Genotype genotype = (Genotype) o;
        return Objects.equals(name, genotype.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}

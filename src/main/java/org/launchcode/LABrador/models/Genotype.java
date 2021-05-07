package org.launchcode.LABrador.models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Genotype extends AbstractEntity {

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "genotype")
    private final List<Animal> animals = new ArrayList<>();

    public Genotype() {}

    public Genotype(@NotBlank String name) {
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

}

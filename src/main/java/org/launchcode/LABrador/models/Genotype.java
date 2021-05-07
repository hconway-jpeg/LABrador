package org.launchcode.LABrador.models;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Genotype extends AbstractEntity {

    @NotBlank
    private String genotype;

    @OneToMany(mappedBy = "genotype")
    private final List<Animal> animalsOne = new ArrayList<>();

    public Genotype() {}

    public Genotype(@NotBlank String genotype) {
        this.genotype = genotype;
    }

    public String getGenotype() {
        return genotype;
    }
    public void setGenotype(String genotype) {
        this.genotype = genotype;
    }

    public List<Animal> getAnimalsOne() {
        return animalsOne;
    }

}

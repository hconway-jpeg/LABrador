package org.launchcode.LABrador.data;

import org.launchcode.LABrador.models.Genotype;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenotypeRepository extends CrudRepository<Genotype, Integer> {

    Genotype findByName(String name);

}

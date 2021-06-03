package org.launchcode.LABrador.data;

import org.launchcode.LABrador.models.Animal;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface AnimalRepository extends CrudRepository<Animal, Integer> {

    Animal findById(int Id);
    List<Animal> findByLabId(int labId, Sort id);
    Object findAll(Sort genotypeTwo);


}

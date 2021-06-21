package org.launchcode.LABrador.data;

import org.launchcode.LABrador.models.Animal;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.ArrayList;

@Repository
public interface AnimalRepository extends CrudRepository<Animal, Integer> {

    Animal findById(int Id);
    List<Animal> findByLabId(int labId, Sort id);
    Object findAll(Sort genotypeTwo);



    ArrayList<Animal> findByNotesKeyword(String searchTerm);




}

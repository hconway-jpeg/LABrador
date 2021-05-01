package org.launchcode.LABrador.data;

import org.launchcode.LABrador.models.Animal;
import org.launchcode.LABrador.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends CrudRepository<Animal, Integer> {
    Animal findById(int Id);
}

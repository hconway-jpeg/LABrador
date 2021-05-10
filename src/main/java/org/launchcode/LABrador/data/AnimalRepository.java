package org.launchcode.LABrador.data;

import org.launchcode.LABrador.models.Animal;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends CrudRepository<Animal, Integer> {

    List<Animal> findByTag(String tag,Sort sort);
    List<Animal> findByCageNumber(String cageNumber,Sort sort);
    List<Animal> findByCageType(String cageType,Sort sort);

    Object findAll(Sort tag);
}

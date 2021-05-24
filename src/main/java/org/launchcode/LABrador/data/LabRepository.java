package org.launchcode.LABrador.data;

import org.launchcode.LABrador.models.Lab;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabRepository extends CrudRepository<Lab, Integer> {
}
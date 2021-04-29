package org.launchcode.LABrador.data;

import org.launchcode.LABrador.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

}

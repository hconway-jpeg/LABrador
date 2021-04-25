package org.launchcode.LABrador.data;

import org.launchcode.LABrador.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<User, Integer> {
}

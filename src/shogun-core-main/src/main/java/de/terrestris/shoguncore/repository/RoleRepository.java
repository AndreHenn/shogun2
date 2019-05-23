package de.terrestris.shoguncore.repository;

import de.terrestris.shoguncore.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByName(String name);
}

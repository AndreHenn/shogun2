package de.terrestris.shoguncore.repository;

import de.terrestris.shoguncore.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByAccountName(String accountName);

}

package de.terrestris.shogun2.repository;

import de.terrestris.shogun2.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicationRepository extends MongoRepository<Application, String> {

    Application findByName(String name);

    Application findByActive(Boolean active);

}

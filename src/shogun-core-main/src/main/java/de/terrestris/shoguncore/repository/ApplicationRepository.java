package de.terrestris.shoguncore.repository;

import de.terrestris.shoguncore.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicationRepository extends MongoRepository<Application, String> {

    Application findByName(String name);

    Application findByActive(Boolean active);

}

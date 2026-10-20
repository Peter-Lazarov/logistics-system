package com.sunny.times.tracking.persistence;

import com.sunny.times.tracking.persistence.entity.VehicleLocationEntity;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class VehicleLocationCoordinateMigration implements ApplicationRunner {

    private final MongoTemplate mongoTemplate;

    public VehicleLocationCoordinateMigration(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        Query legacyLocations = Query.query(new Criteria().orOperator(
                Criteria.where("lat").exists(true),
                Criteria.where("lng").exists(true)
        ));

        mongoTemplate.updateMulti(
                legacyLocations,
                new Update()
                        .rename("lat", "latitude")
                        .rename("lng", "longitude"),
                VehicleLocationEntity.class
        );
    }
}

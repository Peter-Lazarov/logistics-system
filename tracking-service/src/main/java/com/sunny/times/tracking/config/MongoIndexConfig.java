package com.sunny.times.tracking.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

@Configuration
public class MongoIndexConfig {

    @Bean
    public ApplicationRunner initIndexes(MongoTemplate mongoTemplate) {
        return args -> {

            IndexOperations indexOps = mongoTemplate.indexOps("tracking_events");

            Index ttlIndex = new Index()
                    .on("timestamp", Sort.Direction.ASC)
                    .expire(604800); // 7 days = 60 * 60 * 24 * 7

            indexOps.createIndex(ttlIndex);

            Index shipmentIdIndex = new Index()
                    .on("shipmentId", Sort.Direction.ASC);

            indexOps.createIndex(shipmentIdIndex);

            Index orderIdIndex = new Index()
                    .on("orderId", Sort.Direction.ASC);

            indexOps.createIndex(orderIdIndex);
        };
    }
}

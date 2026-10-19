package com.sunny.times.tracking;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@SpringBootApplication
public class TrackingServiceApplication {

	@Value("${spring.data.mongodb.uri}")
	private String mongoUri;

	public static void main(String[] args) {
		SpringApplication.run(TrackingServiceApplication.class, args);
	}

	@Bean
	public MongoClient mongoClient() {
		ConnectionString cs = new ConnectionString(mongoUri);
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString(cs)
				.uuidRepresentation(UuidRepresentation.STANDARD)
				.build();
		return MongoClients.create(settings);
	}

	@Bean
	public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient,
													 @Value("${spring.data.mongodb.database}") String dbName) {
		return new SimpleMongoClientDatabaseFactory(mongoClient, dbName);
	}

	@Bean
	public MongoTemplate mongoTemplate(MongoDatabaseFactory factory) {
		return new MongoTemplate(factory);
	}

}

package com.konsol.core.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class ErMongoConfig {

    @Bean
    public MongoClient erMongoClient() {
        return MongoClients.create("mongodb://localhost:27017"); // Adjust URI as needed
    }

    @Bean
    public MongoDatabaseFactory erDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(erMongoClient(), "er");
    }

    @Bean
    public MongoTemplate erMongoTemplate() {
        return new MongoTemplate(erDatabaseFactory());
    }

    @Bean
    public MongoTransactionManager erTransactionManager() {
        return new MongoTransactionManager(erDatabaseFactory());
    }
}

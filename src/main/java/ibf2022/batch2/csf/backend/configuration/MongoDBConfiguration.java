package ibf2022.batch2.csf.backend.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoDBConfiguration {
    @Value("${mongodb.url}")
    private String mongoDbUrl;

    @Value("${mongodb.database}")
    private String databaseName;
    
    @Bean
    MongoTemplate fnCreateMonoDatabase() {
        MongoClient mongoClient = MongoClients.create(mongoDbUrl);
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, databaseName);
        return mongoTemplate;
    }
}

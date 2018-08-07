package com.assignment;

import java.io.IOException;
import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.assignment.repository.TaskRepository;
import com.assignment.todo.model.Task;
import com.mongodb.MongoClient;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;

@SpringBootApplication
@ComponentScan(useDefaultFilters = false, includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.assignment.*"))
public class DemoApplication {
	private static final String MONGO_DB_URL = "localhost";
	private static final String MONGO_DB_NAME = "taskdb";

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * Bootstrapping with some dummy values
	 * 
	 * @param repository
	 * @return
	 */
	@Bean
	CommandLineRunner init(TaskRepository repository) {
		return args -> {
			repository.deleteAll();
			repository.save(new Task("Task1", "Addition must be for number values greater than 0", new Date(), new Date(),
					"todo"));
			repository.save(new Task("Task2", "Subtraction must be for number values greater than 0", new Date(), new Date(),
					"pending"));
			repository.save(new Task("Task3", "Multiplication must be for number values greater than 0", new Date(), new Date(),
					"completed"));
			
			System.out.println("Tasks found with findAll():");
			System.out.println("-------------------------------");
			for (Task task : repository.findAll()) {
				System.out.println(task);
			}
			System.out.println();

			System.out.println("Task found with findByTitle('Add'):");
			System.out.println("--------------------------------");
			System.out.println(repository.findByTitle("Task1"));
		};
	}

	/**
	 * Preparing embedded Mongo DB
	 * 
	 * @param mongoDbFactory
	 * @param context
	 * @return
	 * @throws IOException
	 */
	@Bean
	public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MongoMappingContext context) throws IOException {
		EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
		mongo.setBindIp(MONGO_DB_URL);
		MongoClient mongoClient = mongo.getObject();
		MongoTemplate mongoTempate = new MongoTemplate(mongoClient, MONGO_DB_NAME);
		return mongoTempate;
	}
}

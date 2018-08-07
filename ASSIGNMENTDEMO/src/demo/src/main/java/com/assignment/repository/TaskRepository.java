package com.assignment.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.assignment.todo.model.Task;

public interface TaskRepository extends MongoRepository<Task, String> {
	Task findByTitle(String title);
	List<Task> findAllByStatus(String status);
}

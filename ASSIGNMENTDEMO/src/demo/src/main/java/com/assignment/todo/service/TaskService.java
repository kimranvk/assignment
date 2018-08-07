package com.assignment.todo.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.repository.TaskRepository;
import com.assignment.todo.model.Task;
@Service
public class TaskService {
	@Autowired
	private TaskRepository repository;

	public String addTask(Task task) {
		task.setId(task.getTitle());
		Task savedtask = repository.insert(task);
		return savedtask == null ? null : savedtask.getId();
	}

	public Task findTaskById(String id) {
		Optional<Task> optionalTask = repository.findById(id);
		return optionalTask == null ? null : optionalTask.get();
	}

	public Task findTaskByTitle(String title) {
		return repository.findByTitle(title);
	}

	public List<Task> findAllByStatus(String status){
		return repository.findAllByStatus(status);
	}
	
	public void deleteTask(String id) {
		repository.deleteById(id);
	}

	public void updateTask(String id, String status) {
		Optional<Task> optionalTask = repository.findById(id);
		if (optionalTask != null) {
			Task task = optionalTask.get();
			if (task != null) {
				task.setStatus(status);
				repository.save(task);
			}
		}
	}
}

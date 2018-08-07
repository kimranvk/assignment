package com.assignment.todo.view;

public class TaskView {
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public TaskView (String id) {
		this.id =id;
	}
	public TaskView() {}
	private String id;
}
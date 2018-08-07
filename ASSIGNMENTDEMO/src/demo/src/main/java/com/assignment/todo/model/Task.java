package com.assignment.todo.model;

import java.util.Date;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "task")
public class Task {
	private String id;
	
	@Indexed(unique = true)
	private String title;
	private String comments;
	private Date startDate;
	private Date endDate;
	private String status;
	
	public Task() {}
	public Task( String title, String comments, Date startDate, Date endDate, String status) {
		this.title=title;
		this.comments=comments;
		this.startDate = startDate;
		this.endDate = endDate;
		this.status = status;
	}
	@Override
	public String toString() {
		return "Task {" + "id=" + id + ", title='" + title + '\'' + ", comments=" + comments + ", startDate='" + startDate+ ", endDate='" + endDate + ", status='" + status+'}';
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Task))
			return false;
		Task other = (Task) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
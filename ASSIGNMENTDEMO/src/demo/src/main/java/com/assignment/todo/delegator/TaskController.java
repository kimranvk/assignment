package com.assignment.todo.delegator;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.assignment.todo.model.Task;
import com.assignment.todo.service.TaskService;
import com.assignment.todo.view.TaskView;

@RestController
@RequestMapping("/taskService")
public class TaskController {
	@Autowired
	TaskService taskService;

	@RequestMapping("/add")
	@PUT
	@Produces("application/json")
	@Consumes("application/json")
	public TaskView addTodo(@RequestBody Task task) {
		return new TaskView(taskService.addTask(task));
	}

	@RequestMapping("/remove/{id}")
	@DELETE
	@Consumes("application/json")
	public void deleteTask(@PathVariable("id") String id) {
		taskService.deleteTask(id);
	}

	@RequestMapping("/update/id/{id}/status/{status}")
	@POST
	@Consumes("application/json")
	public void moveTask(@PathVariable("id") String id, @PathVariable("status") String status) {
		taskService.updateTask(id, status);
	}
	
	@RequestMapping("/title/{title}")
	@GET
	@Produces("application/json")
	public Task find(@PathVariable("title") String title) {
		return taskService.findTaskByTitle(title);
	}
	
	
	@RequestMapping("all/{status}")
	@GET
	@Produces("application/json")
	public List<Task> findAll(@PathVariable("status") String status) {
		return taskService.findAllByStatus(status);
	}
	

	@ControllerAdvice
	public class ExceptionHandlerController {

		public static final String DEFAULT_ERROR_VIEW = "error";

		@ExceptionHandler(value = { Exception.class, RuntimeException.class })
		public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
			ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);

			mav.addObject("datetime", new Date());
			mav.addObject("exception", e.fillInStackTrace());
			mav.addObject("url", request.getRequestURL());

			return mav;
		}
	}
}
$(document).ready(function() {
	publishEvent("loadTasksEvent");
});

function publishEvent(event,context) {
	var scope = angular.element(document.getElementById("mainDiv")).scope();
	scope.publishEvent(event,context);
}

var mainApp=angular.module("common",[]);

mainApp.controller("commonCntrl", function($http){
});



var app = angular.module("appModule", ['common']);

app.service('taskService', function($http){
   this.loadAll = function(scope, status) {
	   $http.get("http://localhost:8181/demo/taskService/all/"+status)
		.then(function successCallback(response) {
	        scope.tasks = response.data;
	    }, function errorCallback(response) {
	        scope.error = response.statusText;
	    });
   }
   
   this.addTask = function(scope, task, status) {
	   $http.put("http://localhost:8181/demo/taskService/add",task)
		.then(function successCallback(response) {
			if(response.data) {
	        	scope.publishEvent(status+'TaskAddedEvent', task);
	        }			
	    }, function errorCallback(response) {
	    	alert("Task ["+task.title+"] is not allowed in system, perhaps using duplicate title.");
	    });
   }
   
   this.removeTask=function(scope, task, index, status) {
	   $http.post("http://localhost:8181/demo/taskService/remove/"+task.id)
		.then(function successCallback(response) {
	        if(response.status=='200') {
	        	publishEvent(status+'TaskRemovedEvent', {"index":index});
	        }			
	    }, function errorCallback(response) {
	    	scope.$emit(status+'TaskNotRemovedEvent', {"task":task, "error":response.statusText});
	    });
   }
   
   this.updateStatus=function(scope,task,index, status) {
	   $http.post("http://localhost:8181/demo/taskService/update/id/"+task.id+"/status/"+status)
		.then(function successCallback(response) {
	        if(response.status=='200') {
	        	scope.$emit(task.status+'TaskRemovedEvent', {"index":index});
	        	publishEvent(status+'TaskAddedEvent', task);
	        }			
	    }, function errorCallback(response) {
	    	scope.$emit(task.status+'TaskStatusNotUpdatedEvent', {"task":task, "toStatus":status, "error":response.statusText});
	    });
   }
});	

app.controller("appCntrl", function($scope,$rootScope,taskService) {
	$scope.publishEvent = function(event, context) {
		$rootScope.$broadcast('loadTasksEvent', context);
	};

	$scope.save = function() {
		if($scope.task) {
			$scope.task.status='todo';
			taskService.addTask($scope, $scope.task, "todo");
		}
	};
	
});

app.controller("todosCtrl", function($scope,$rootScope, taskService) {
	$scope.$on("loadTasksEvent",function (context) {
		taskService.loadAll($scope,'todo');
	});
	
	$scope.$on("todoTaskAddedEvent", function (context){
		if(!$scope.tasks) {
			$scope.tasks=[];
		}
		context.status = 'todo';
		$scope.tasks.push(context);   
	});

	$scope.$on("todoTaskNotAddedEvent", function (context){
		alert(context.error);   
	});
	
	
	$scope.remove = function(index) {
		taskService.removeTask($scope, $scope.tasks[index],index,  "todo");
	}
	
	$scope.$on("todoTaskRemovedEvent",function(context){
		$scope.tasks.splice(context.index, 1);
	});
	
	$scope.$on("todoTaskNotRemovedEvent", function (context){
		alert(context.error);   
	});
	
	$scope.move=function(index,dir) {
		if(dir=='>') {
			taskService.updateStatus($scope,$scope.tasks[index],index, 'pending');
		} else {
			alert('not supported');
		}
	}
});


app.controller("pendingsCtrl", function($scope,taskService) {
	$scope.$on("loadTasksEvent",function (context) {
		taskService.loadAll($scope,'pending');
	});

	$scope.$on("pendingTaskAddedEvent", function (context){
		if(!$scope.tasks) {
			$scope.tasks=[];
		}
		context.status = 'pending';
		$scope.tasks.push(context);   
	});

	$scope.$on("pendingTaskNotAddedEvent", function (context){
		alert(context.error);   
	});
	
	
	$scope.remove = function(index) {
		taskService.removeTask($scope, $scope.tasks[index], index, "pending");
	}
	
	$scope.$on("pendingTaskRemovedEvent",function(context){
		$scope.tasks.splice(context.index, 1);
	});
	
	$scope.$on("pendingTaskNotRemovedEvent", function (context){
		alert(context.error);   
	});
	
	$scope.move=function(index,dir) {
		if(dir=='>') {
			taskService.updateStatus($scope,$scope.tasks[index],index, 'completed');
		} else if(dir=='<') {
			taskService.updateStatus($scope,$scope.tasks[index],index, 'todo');
		}
	}
});

app.controller("completedCtrl", function($scope,taskService) {
	$scope.$on("loadTasksEvent",function (context) {
		taskService.loadAll($scope,'completed');
	});
	
	$scope.$on("completedTaskAddedEvent", function (context){
		if(!$scope.tasks) {
			$scope.tasks=[];
		}
		context.status = 'completed';
		$scope.tasks.push(context);   
	});

	$scope.$on("completedTaskNotAddedEvent", function (context){
		alert(context.error);   
	});
	
	$scope.remove = function(index) {
		taskService.removeTask($scope, $scope.tasks[index], index, "completed");
	}
	
	$scope.$on("completedTaskRemovedEvent",function(context){
		$scope.tasks.splice(context.index, 1);
	});
	
	$scope.$on("completedTaskNotRemovedEvent", function (context){
		alert(context.error);   
	});
	
	$scope.move=function(index,dir) {
		if(dir=='>') {
			alert("not supported");
		} else if(dir=='<') {
			taskService.updateStatus($scope,$scope.tasks[index],index, 'pending');
		}
	}
});

app.directive("task", function($compile) {
   var templateHtml = "<label>{{task.title}}</label><p style=\"vertical-align: top\">	<small><span>{{task.comments}}</span></small></p><span class=\"glyphicon glyphicon-trash\" style=\"cursor: pointer;\" ng-click=\"remove($index)\"></span>&nbsp;<span class=\"glyphicon glyphicon-chevron-left\" aria-hidden=\"true\" style=\"cursor: pointer;\" ng-click=\"move($index,'<')\" ></span><span class=\"glyphicon glyphicon-chevron-right\" aria-hidden=\"true\" style=\"cursor: pointer;\" ng-click=\"move($index,'>')\"></span>";
	return {
	    template: templateHtml
//	    templateUrl:'task.html'
	   };
});
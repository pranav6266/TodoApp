package com.pranav.todoapp.managers;

import com.pranav.todoapp.dto.TaskDTO;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
	private List<TaskDTO> tasks;

	public TaskList(){
		tasks = new ArrayList<>();
	}

	public List<TaskDTO> getTasks() {
		return tasks;
	}

	public void addTask(TaskDTO task) {
		tasks.add(task);
	}

	public void removeTask(TaskDTO task){
		tasks.remove(task);
	}

	public TaskDTO getTaskById(String id){
		for (TaskDTO currentTask: tasks){
			if(currentTask.getId().equals(id)){
				return currentTask;
			}
		}
		return null;
	}

	public void updateTask(TaskDTO updatedTask){
		for(int i = 0; i < tasks.size(); i++){
			TaskDTO currentTask = tasks.get(i);
			if(currentTask.getId().equals(updatedTask.getId())){
				currentTask.setTitle(updatedTask.getTitle());
				currentTask.setDescription(updatedTask.getDescription());
				currentTask.setStatus(updatedTask.getStatus());

				currentTask.setComments(updatedTask.getComments());

				break;

			}
		}
	}
}

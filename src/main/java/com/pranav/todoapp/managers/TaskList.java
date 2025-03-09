package com.pranav.todoapp.managers;

import com.pranav.todoapp.dto.TaskDTO;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskList implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	private static final String FILE_PATH = "src/task.bin";

	private static final TaskList instance = new TaskList();
	private static List<TaskDTO> tasks;

	private TaskList(){
		tasks = new ArrayList<>();
		loadTask();
	}


	public static TaskList getInstance() {
		return instance;
	}

	public List<TaskDTO> getTasks() {
		System.out.println("Fetching Tasks: " + tasks);
		return tasks;
	}

	public void addTask(TaskDTO task) {
		tasks.add(task);
		System.out.println("Task added: " + task);
		System.out.println("Current Tasks: " + tasks);
		sortTaskByStatus();
		saveTasks();
	}

	public void removeTask(TaskDTO task){
		tasks.remove(task);
		saveTasks();
	}


	public TaskDTO getTaskById(String id) {
		System.out.println("Searching for Task ID: " + id);
		System.out.println("Total tasks stored: " + tasks.size());

		for (TaskDTO task : tasks) {
			System.out.println("Checking Task ID: " + task.getId());
			if (task.getId().equals(id)) {
				System.out.println("✅ Found task: " + task.getTitle());
				return task;
			}
		}

		System.out.println("❌ ERROR: No matching task found for ID: " + id);
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
		sortTaskByStatus();
		saveTasks();
	}

	private void sortTaskByStatus(){
		tasks.sort(Comparator.comparingInt(
				task -> {
			switch (task.getStatus()){
				case "ToDo" : return 1;
				case "InProgress" : return 2;
				case "Done" : return 3;
				default: return 4;
			}
		}
		));
	}

	private void saveTasks(){
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))){
			oos.writeObject(tasks);
		}catch (IOException e){
			e.printStackTrace();
		}
	}


	private void loadTask(){
		File file = new File(FILE_PATH);
		if(file.exists()){
			try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
					tasks = (List<TaskDTO>) ois.readObject();

			} catch (IOException | ClassNotFoundException e){
				throw new RuntimeException(e);
			}
		}else {
			saveTasks();
		}
	}
}

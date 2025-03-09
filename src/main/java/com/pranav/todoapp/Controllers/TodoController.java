package com.pranav.todoapp.Controllers;

import com.pranav.todoapp.dto.TaskDTO;
import com.pranav.todoapp.managers.TaskList;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TodoController {
	@FXML
	public MFXTextField taskTitle;
	@FXML
	public MFXComboBox<String> statusComboBox;
	@FXML
	private VBox taskListVBox;
	private TaskList taskList = TaskList.getInstance();

	@FXML
	public void initialize(){
		taskList = TaskList.getInstance();
		statusComboBox.getItems().addAll("All", "ToDo", "InProgress", "Done");
		statusComboBox.setValue("All");
		statusComboBox.valueProperty().addListener(
				new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
						//add filtering logic
						filterTaskByStatus(newValue);
					}
				}
		);

//		addTask("Create a JavaFX Project", "Build a really cool ui.",LocalDateTime.now().minusMinutes(3), "ToDo");
//		addTask("Learn SpringBoot","It is crucial for your career", LocalDateTime.now(), "Done");
//		addTask("Create a ToDo App","Building a ToDo List", LocalDateTime.now().minusMinutes(2), "InProgress");
	redrawTaskList();
	}

	@FXML
	public void handleAddTask(ActionEvent actionEvent) {
		showAddTaskDialog();
	}


	private void filterTaskByStatus(String status){
		taskListVBox.getChildren().clear();

		List<TaskDTO> filteredTasks;

		if ("All".equals(status)) {
			filteredTasks = taskList.getTasks();
		}else{
			filteredTasks = taskList.getTasks().stream().filter(
					task -> task.getStatus().equals(status)).collect(Collectors.toUnmodifiableList());
		}

		for(TaskDTO task : filteredTasks){
			displayTask(task);
		}
	}


	private void showAddTaskDialog(){
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pranav/todoapp/task_add_dialog.fxml"));
			VBox dialogPane = loader.load();

			TaskAddDialogController dialogController = loader.getController();
			dialogController.setMainController(this);

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add new task");

//			Making other objects or buttons unclickable
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			Scene scene = new Scene(dialogPane);

			String css = this.getClass().getResource("/com/pranav/todoapp/addtaskstyles.css").toExternalForm();
			scene.getStylesheets().add(css);

			dialogStage.setScene(scene);
			dialogStage.showAndWait();

		} catch (Exception e){
			e.printStackTrace();
		}

	}

	public void addTaskFromDialog(String title, String description){
		addTask(title, description, LocalDateTime.now(), "ToDo");
	}


	private void addTask(String title, String description,
	                     LocalDateTime dateAdded, String status){
		TaskDTO newTask = new TaskDTO(title, description, dateAdded, status);
			taskList.addTask(newTask);
			redrawTaskList();
	}


	private void displayTask(TaskDTO task){
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pranav/todoapp/task-card.fxml"));

			HBox taskCard = loader.load();
			TaskCardController controller = loader.getController();
			controller.setTaskDetails(task.getTitle(), task.getDateAdded() , task.getStatus(), task.getId(), this);

			taskListVBox.getChildren().add(taskCard);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void redrawTaskList(){
		taskListVBox.getChildren().clear();

		for (TaskDTO task: taskList.getTasks()){
			displayTask(task);
		}

		statusComboBox.setValue("All");
	}
}

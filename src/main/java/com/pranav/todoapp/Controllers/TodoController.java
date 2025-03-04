package com.pranav.todoapp.Controllers;

import com.pranav.todoapp.dto.TaskDTO;
import com.pranav.todoapp.managers.TaskList;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
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

public class TodoController {
	@FXML
	public MFXTextField taskTitle;
	@FXML
	public MFXComboBox<String> statusComboBox;
	@FXML
	public VBox taskListVBox;
	private TaskList taskList;

	@FXML
	public void initialize(){
		taskList = new TaskList();
		statusComboBox.getItems().addAll("All", "ToDo", "InProgress", "Done");
		statusComboBox.setValue("All");

		addTask("Create a JavaFX Project", "Build a really cool ui.",LocalDateTime.now().minusMinutes(3), "ToDo");
		addTask("Learn SpringBoot","It is crucial for your career", LocalDateTime.now(), "Done");
		addTask("Create a ToDo App","Building a ToDo List", LocalDateTime.now().minusMinutes(2), "InProgress");
	}

	@FXML
	public void handleAddTask(ActionEvent actionEvent) {
		showAddTaskDialog();
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
			displayTask(newTask);
	}


	private void displayTask(TaskDTO task){
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pranav/todoapp/task-card.fxml"));

			HBox taskCard = loader.load();
			TaskCardController controller = loader.getController();
			controller.setTaskDetails(task.getTitle(), task.getDateAdded() , task.getStatus(), task.getId());

			taskListVBox.getChildren().add(taskCard);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

package com.pranav.todoapp.Controllers;

import com.pranav.todoapp.dto.TaskDTO;
import com.pranav.todoapp.managers.TaskList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskCardController {
	@FXML
	public Label taskName;
	public Label taskTimeStamp;
	public Label taskStatus;
	public String taskId;
	private final TaskList taskList = TaskList.getInstance();
	private TodoController mainController;

	public void setTaskDetails(String name, LocalDateTime timeStamp, String status, String id, TodoController controller){
		taskName.setText(name);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a, dd/MM/yyyy");
		taskTimeStamp.setText(timeStamp.format(formatter));
		taskStatus.setText(status);
		this.taskId = id;
		applyStatusColor(status);

		mainController = controller;
	}

	private void applyStatusColor(String status){
		switch (status){
			case "ToDo" -> taskStatus.setStyle("-fx-text-fill: grey;") ;
			case "InProgress" -> taskStatus.setStyle("-fx-text-fill: orange;");
			case "Done" -> taskStatus.setStyle("-fx-text-fill: green;");
			default -> taskStatus.setStyle("-fx-text-fill: black;");
		}
	}



	@FXML
	private void handleViewTask() {
		System.out.println("🔄 Opening dialog for Task ID: " + taskId);
		System.out.println("All stored tasks: " + taskList.getTasks());
		// Print all tasks BEFORE fetching the requested task
		System.out.println("🔥 Checking Available Tasks Before Fetch:");
		for (TaskDTO task : taskList.getTasks()) {
			System.out.println("  - " + task.getTitle() + " | ID: " + task.getId());
		}

		TaskDTO task = taskList.getTaskById(taskId);

		if (task == null) {
			System.out.println("❌ ERROR: setTaskDetails received a NULL task!");
			return;
		}

		// Open dialog with the task details
		showViewTaskDialog(task);
	}

	private void showViewTaskDialog(TaskDTO task) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pranav/todoapp/task_view_dialog.fxml"));
			VBox dialogPane = loader.load();

			TaskViewDialogController dialogController = loader.getController();
			dialogController.setTaskDetails(task, this);

			Stage dialogStage = new Stage();
			dialogStage.setTitle(taskName.getText());

			// Set the dialog to be non-resizable
			dialogStage.setResizable(false);

			// Set the size of the dialog
			dialogStage.sizeToScene();

//			Making other objects or buttons unclickable
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			Scene scene = new Scene(dialogPane);

			String css = this.getClass().getResource("/com/pranav/todoapp/viewdialogstyles.css").toExternalForm();
			scene.getStylesheets().add(css);

			dialogStage.setScene(scene);
			dialogStage.showAndWait();

		} catch (Exception e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Something went wrong!");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}
	}


	public void updateTask(TaskDTO task){
		taskList.updateTask(task);
		taskName.setText(task.getTitle());
		taskStatus.setText(task.getStatus());
		applyStatusColor(task.getStatus());

		mainController.redrawTaskList();
	}

	public void deleteTask(TaskDTO task){
		taskList.removeTask(task);
		mainController.redrawTaskList();
	}
}

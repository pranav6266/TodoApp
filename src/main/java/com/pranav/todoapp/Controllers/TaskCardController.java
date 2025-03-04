package com.pranav.todoapp.Controllers;

import com.pranav.todoapp.dto.TaskDTO;
import com.pranav.todoapp.managers.TaskList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
	private final TaskList taskList = new TaskList();




	@FXML
	public void handleViewTask(ActionEvent actionEvent) {
		System.out.println("Viewing task: "+taskName.getText());
		try {
			// Use ClassLoader to get the resource URL
			ClassLoader classLoader = TaskCardController.class.getClassLoader();
			URL fxmlUrl = classLoader.getResource("com/pranav/todoapp/task_view_dialog.fxml");
			
			if (fxmlUrl == null) {
				System.err.println("Error: Could not find task_view_dialog.fxml");
				return;
			}
			
			FXMLLoader loader = new FXMLLoader(fxmlUrl);
			VBox dialogPane = loader.load();
			
			TaskDTO task = taskList.getTaskById(taskId);
			if (task == null) {
				System.err.println("Error: Could not find task with ID: " + taskId);
				return;
			}
			
			TaskViewDialogController dialogController = loader.getController();
			dialogController.setTaskDetails(task, this);

			Stage dialogStage = new Stage();
			dialogStage.setTitle(taskName.getText());

			dialogStage.initModality(Modality.APPLICATION_MODAL);
			Scene scene = new Scene(dialogPane);

			// Load CSS using ClassLoader as well
			URL cssUrl = classLoader.getResource("com/pranav/todoapp/viewdialogstyles.css");
			if (cssUrl == null) {
				System.err.println("Error: Could not find viewdialogstyles.css");
			} else {
				scene.getStylesheets().add(cssUrl.toExternalForm());
			}

			dialogStage.setScene(scene);
			dialogStage.showAndWait();

		} catch (Exception e){
			System.err.println("Error in handleViewTask: " + e.getMessage());
			e.printStackTrace();
		}
	}


	public void setTaskDetails(String name, LocalDateTime timeStamp, String status, String id){
		taskName.setText(name);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a, dd/MM/yyyy");
		taskTimeStamp.setText(timeStamp.format(formatter));
		taskStatus.setText(status);
		this.taskId = id;
		applyStatusColor(status);
	}


	private void applyStatusColor(String status){
		switch (status){
			case "ToDo" -> taskStatus.setStyle("-fx-text-fill: grey;") ;
			case "InProgress" -> taskStatus.setStyle("-fx-text-fill: orange;");
			case "Done" -> taskStatus.setStyle("-fx-text-fill: green;");
			default -> taskStatus.setStyle("-fx-text-fill: black;");
		}
	}

	public void updateTask(TaskDTO task){
		taskList.updateTask(task);
		taskName.setText(task.getTitle());
	}
}

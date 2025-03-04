package com.pranav.todoapp.Controllers;

import com.pranav.todoapp.dto.TaskDTO;
import com.pranav.todoapp.managers.TaskList;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TaskViewDialogController {
	@FXML
	public MFXTextField taskTitleField;
	public TextArea taskDescriptionField;
	public MFXComboBox<String> statusComboBox;
	public VBox commentList;
	public MFXTextField commentField;

	private TaskCardController mainController;
	private TaskDTO task;

	public void setTaskDetails(TaskDTO task, TaskCardController mainController){
		this.task = task;
		this.mainController = mainController;

		taskTitleField.setText(task.getTitle());
		taskDescriptionField.setText(task.getDescription());
		statusComboBox.getItems().clear();
		statusComboBox.getItems().addAll("ToDo", "InProgress", "Done" );


//		Waits for all the elements to load before showing Combo box
//		otherwise sometimes issue arises
		Platform.runLater(() ->{
			statusComboBox.setValue(task.getStatus());
		} );

		task.getComments().forEach(comment -> {
			displayComment(comment);
		});
	}


	private void displayComment(String comment){
		Text commentLabel = new Text(comment);
		commentLabel.setStyle("-fx-padding: 3px;");
		commentList.getChildren().addFirst(commentLabel);
	}
	@FXML
	public void handleAddComment(ActionEvent actionEvent) {
		String comment = commentField.getText();
		if(!comment.isEmpty()){
			task.addComments(comment);
			displayComment(comment);
			commentField.clear();
		}
	}

	@FXML
	public void handleCancel(ActionEvent actionEvent) {
		closeDialog();
	}

	@FXML
	public void handleDelete(ActionEvent actionEvent) {
	}

	@FXML
	public void handleUpdate(ActionEvent actionEvent) {
		task.setTitle(taskTitleField.getText());
		task.setDescription(taskDescriptionField.getText());
		task.setStatus(statusComboBox.getValue());

//		mainController.
	}

	void closeDialog(){
		Stage stage = (Stage) taskTitleField.getScene().getWindow();
		stage.close();
	}


}

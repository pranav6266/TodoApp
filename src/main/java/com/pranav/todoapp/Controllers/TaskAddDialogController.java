package com.pranav.todoapp.Controllers;

import com.pranav.todoapp.dto.TaskDTO;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TaskAddDialogController {
	@FXML
	public TextArea taskDescriptionField;
	public MFXTextField taskTitleField;
	private TodoController mainController;

	public void setMainController(TodoController mainController){
		this.mainController = mainController;
	}

	@FXML
	public void handleCancel(ActionEvent actionEvent) {
		closeDialog();
	}

	public void handleSubmit(ActionEvent actionEvent) {
		String title = taskTitleField.getText();
		String description = taskDescriptionField.getText();

		if(!title.isEmpty()) {
			mainController.addTaskFromDialog(title, description);
			closeDialog();
		}else {
			System.out.println("Title is required");
		}
	}

	private void closeDialog(){
		Stage stage = (Stage) taskTitleField.getScene().getWindow();
		stage.close();
	}

}

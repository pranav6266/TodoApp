module com.pranav.todoapp {
	requires javafx.controls;
	requires javafx.fxml;
	requires MaterialFX;

	opens com.pranav.todoapp.Controllers to javafx.fxml;
	exports com.pranav.todoapp;
	exports com.pranav.todoapp.Controllers;
	exports com.pranav.todoapp.dto;
	exports com.pranav.todoapp.managers;
}
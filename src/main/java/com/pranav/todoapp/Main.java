package com.pranav.todoapp;

import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception{
		UserAgentBuilder.builder().
				themes(JavaFXThemes.MODENA).
				themes(MaterialFXStylesheets.forAssemble(true)).
				setDeploy(true).
				setResolveAssets(true).
				build().setGlobal();

		ClassLoader classLoader = Main.class.getClassLoader();
		URL fxmlUrl = classLoader.getResource("com/pranav/todoapp/todo.fxml");
		if (fxmlUrl == null) {
			throw new RuntimeException("Cannot find todo.fxml");
		}

		FXMLLoader loader = new FXMLLoader(fxmlUrl);
		Parent root = loader.load();
		Scene scene = new Scene(root);

		URL cssUrl = classLoader.getResource("com/pranav/todoapp/style.css");
		if (cssUrl != null) {
			scene.getStylesheets().add(cssUrl.toExternalForm());
		}

		scene.setFill(Color.TRANSPARENT);
		stage.initStyle(StageStyle.DECORATED);
		stage.setTitle("TODO App");
		stage.setMinWidth(600);
		stage.setMinHeight(400);
		stage.setScene(scene);
		stage.setMaximized(true);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

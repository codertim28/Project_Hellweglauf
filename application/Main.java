import java.io.File;

import classes.Data;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Main extends Application {

	@FXML
	private static Label errorLabel;

	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane root = FXMLLoader.load(getClass().getResource("/templates/mainView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/templates/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMinHeight(640);
			primaryStage.setMaxHeight(640);
			primaryStage.setMinWidth(640);
			primaryStage.setMaxWidth(800);
			primaryStage.setTitle("Projekt Hellweglauf");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);

		Data.createDataDirIfNotExists();
		boolean fileCreated = Data.createDataFileIfNotExists();
		if (!fileCreated) {
			// TODO: Benutzer benachrichtigen, da die Software ohne
			// die Data-Datei nicht verwendet werden kann.
		}
	}
}

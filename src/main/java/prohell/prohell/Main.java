package prohell.prohell;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import prohell.prohell.classes.SetupUtils;
import prohell.prohell.classes.view.MainView;

public class Main extends Application {

	@Override 
	public void init() {
		try {
			SetupUtils.createDataDirIfNotExists();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Stage stage = new Stage();
			FXMLLoader templateLoader = new FXMLLoader(getClass().getResource("/templates/activationView.fxml"));
			stage.setScene(new Scene(templateLoader.load()));
			stage.setResizable(false);
			stage.show();
			
			new MainView(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

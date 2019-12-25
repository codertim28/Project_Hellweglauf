package prohell.prohell;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import prohell.prohell.classes.SetupUtils;
import prohell.prohell.classes.view.ActivationView;
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
			boolean shouldStart = false;
			
			if(ActivationService.get().isSoftwareActivated()) {
				shouldStart = true;
			}
			else {
				ActivationView av = new ActivationView();
				av.getStage().showAndWait();
				
				shouldStart = av.isSoftwareActivated();
			}
			
			if(shouldStart) {
				new MainView(primaryStage);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

package client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import client.view.Login;

/**
 * Provides the main method for the client
 * 
 * @Version 05.11.2013
 * 
 */
public final class Client extends Application {

	public static Logger log = LogManager.getLogger("ERROR");

	// test
	private Login view;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		view = new Login();
		try {
			view.start(new Stage());
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
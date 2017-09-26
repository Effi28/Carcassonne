package client.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HelpView extends Application {

	private Scene scene;
	private GridPane grid;

	@Override
	public void start(Stage primaryStage) {
		grid = new GridPane();
		grid.setPrefSize(1000, 592);
		primaryStage.setTitle("Help");
		scene = new Scene(grid);
		scene.getStylesheets().add(shared.Configuration.STYLESHEETHELPERVIEW);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}

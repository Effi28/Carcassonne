package shared.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ErrorView extends Application {
	private Scene scene;
	private Stage primaryStage;
	private GridPane grid;
	private Label lblError;
	private Label lblErrorInformation;
	private Button btnClose;
	private String error;
	
	
	public ErrorView(String error) {
		this.error = error;
		
		primaryStage = new Stage();
		createWidgets();
		addWidgets();
		setLayout();
		setupInteraction();
		start(primaryStage);
	}

	private void setupInteraction() {

		final DropShadow shadow = new DropShadow();

		// Adds shadows if mouse moves towards the button
		btnClose.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				btnClose.setEffect(shadow);
			}
		});
		// Removes shadows if mouse moves away from the button
		btnClose.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent e) {
				btnClose.setEffect(null);
			}
		});

		// Close Button closes the window

		btnClose.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				primaryStage.close();
			}

		});

		// btnClose.addEventHandler(MouseEvent.MOUSE_CLICKED, new
		// EventHandler<MouseEvent>() {
		//
		// public void handle(MouseEvent arg0) {
		//
		// primaryStage.close();
		// }
		// });
	}

	private void createWidgets() {
		lblError = new Label("Error!");
		lblErrorInformation = new Label(null);
		btnClose = new Button("Close");
		grid = new GridPane();
	}

	private void addWidgets() {
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 10, 10));
		grid.add(lblError, 0, 1);
		grid.add(lblErrorInformation, 0, 2);
		grid.add(btnClose, 0, 4);
	}

	private void setLayout() {
		// grid.setPrefSize(300, 100);
		lblError.setTextFill(Color.RED);
		lblError.setFont(Font.font("Amble", FontWeight.BOLD, 13));
		lblErrorInformation.setText(error);
		lblErrorInformation.setTextFill(Color.RED);
		lblErrorInformation.setFont(Font.font("Amble", FontWeight.BOLD, 13));
	}

	public void start(Stage stage) {
		scene = new Scene(grid);
		stage.setScene(scene);
		stage.setTitle("Error");
		stage.setResizable(false);
		primaryStage.show();
	}

}
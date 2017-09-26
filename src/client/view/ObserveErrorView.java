package client.view;

import static shared.Configuration.BTNOKWOOD;

import java.util.Observer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import shared.model.Failure;

/**
 * Provides an information window for the user if he decides to observe a game that has not yet started.
 * 
 * Is an observer -> Observer-Pattern
 * 
 * @version 17.01.2014
 * @see Application
 * @see ChatLobby
 * @see ObserverView
 * @see HostGame
 * @see GameView
 * @see JoinGame
 * @see Login
 * @see PrivateTab
 */
public final class ObserveErrorView extends Application implements Observer {

	private Scene scene;
	private Stage primaryStage;
	private GridPane grid;
	private Label lblGameNotStarted;
	private Label lblHint;
	private Button btnOk;
	private Image imageOk;
	private ImageView imvOk;

	/**
	 * Only constructor of this class takes a reference to the ChatLobby as argument.
	 * 
	 * @param chatLobby
	 */
	public ObserveErrorView(ChatLobby chatLobby) {
		createWidgets();
		addWidgets();
		setLayout();
		setupInteraction();

		chatLobby.getController().getControl().addObserver(this);
	}

	@Override
	public void start(Stage stage) {
		scene = new Scene(grid);
		scene.getStylesheets().add(shared.Configuration.STYLESHEETOBSERVEVIEW);
		stage.setScene(scene);
		stage.setTitle("Observe Game");
		stage.setResizable(false);
		primaryStage = stage;
		primaryStage.show();
	}

	@Override
	public void update(java.util.Observable o, Object arg) {

		if (arg instanceof Failure) {
			final Failure failure = (Failure) arg;
			Platform.runLater(new Runnable() {
				public void run() {
					lblGameNotStarted.setText("");
					lblHint.setText(failure.getName());
					btnOk.setDisable(true);
				}
			});
			Platform.setImplicitExit(true);
		}

	}

	private void setupInteraction() {

		final DropShadow shadow = new DropShadow();

		// Adds shadows if mouse moves towards the button
		btnOk.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent e) {
						btnOk.setEffect(shadow);
					}
				});
		// Removes shadows if mouse moves away from the button
		btnOk.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent e) {
						btnOk.setEffect(null);
					}
				});

		// Ok Button closes the window

		btnOk.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				primaryStage.close();
			}
		});
	}

	private void createWidgets() {
		lblGameNotStarted = new Label(
				"The game you've picked hasn't started yet!");
		lblHint = new Label(
				"You are added as a spectator now.\nAs soon as the game starts it will open automatically.");
		btnOk = new Button("");
		grid = new GridPane();
		imageOk = new Image(BTNOKWOOD, 85, 35, true, true);
		imvOk = new ImageView();
	}

	private void addWidgets() {
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(100, 80, 80, 220));
		grid.add(lblGameNotStarted, 0, 4);
		grid.add(lblHint, 0, 5);
		grid.add(btnOk, 0, 9);
		imvOk.setImage(imageOk);
	}

	private void setLayout() {
		grid.setPrefSize(700, 460);
		btnOk.setGraphic(imvOk);
	}
}

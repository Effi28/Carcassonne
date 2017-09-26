package client.view;

import static shared.Configuration.BTNJOINWOOD;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import shared.enums.JsonType;
import shared.model.Failure;
import shared.view.ErrorView;

/**
 * Provides the popup window when a user wants to join a game.
 * <p>
 * Gives the user the possibility to chose a color in order to join the selected game. If an error occurs the message of
 * the error will be displayed in a red text at this window.
 * 
 * @version 17.01.2014
 * @see Application
 * @see ChatLobby
 * @see ObserverView
 * @see ObserveErrorView
 * @see GameView
 * @see HostGame
 * @see Login
 * @see PrivateTab
 */
public final class JoinGame extends Application implements Observer {

	private Scene scene;
	private Stage primaryStage;
	private GridPane grid;
	private Label lblGreeting;
	private Label lblChooseColor;
	private Label lblColorTaken;
	private Button btnGo;
	private ComboBox<String> colorComboBox;
	private ObservableList<String> colorOptions;
	private int gameID;
	private Image imageJoin;
	private ImageView imvJoin;
	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");

	private final ChatLobby lobby;

	/**
	 * Only constructor of this class.
	 * 
	 * @param chatLobby
	 * @param controller
	 */
	public JoinGame(ChatLobby chatLobby, int gameID) {
		this.lobby = chatLobby;
		lobby.getController().getControl().addObserver(this);
		createWidgets();
		setLayout();
		addWidgets();
		setupInteraction();
		this.gameID = gameID;
	}

	/**
	 * This method is needed in order to display the ChooseColor-Dialog.
	 */
	@Override
	public void start(Stage arg0) throws Exception {
		scene = new Scene(grid);
		scene.getStylesheets().add(shared.Configuration.STYLESHEETJOINGAMEVIEW);
		primaryStage = new Stage();
		primaryStage.setScene(scene);
		primaryStage.setTitle("Choose a Color!");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof JsonType) {
			JsonType jsonMsg = (JsonType) arg;
			switch (jsonMsg) {
			case JOINGAMESUCCESS:
				lobby.getController().getControl().deleteObserver(this);
				Platform.runLater(new Runnable() {
					public void run() {
						primaryStage.close();
					}
				});
				Platform.setImplicitExit(true);
				break;

			default:
				break;
			}
		} else if (arg instanceof Failure) {
			final Failure failure = (Failure) arg;
			Platform.runLater(new Runnable() {
				public void run() {
					lblColorTaken.setText(failure.getName());
				}
			});
			Platform.setImplicitExit(true);
		}
	}

	private void setupInteraction() {

		// ActionHandler for the join Button
		btnGo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					doJoin(gameID);
				} catch (Exception e) {
					log.error(e.getMessage());
					new ErrorView(e.getMessage());
				}
			}

		});

		final DropShadow shadow = new DropShadow();
		// Adds shadows if mouse moves towards the button
		btnGo.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnGo.setEffect(shadow);
					}
				});

		// Removes shadows if mouse moves away from the button
		btnGo.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnGo.setEffect(null);
					}
				});

		//
		colorComboBox.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				if (colorComboBox.getSelectionModel().getSelectedItem() != null) {
					btnGo.setDisable(false);
				} else {
					btnGo.setDisable(true);
				}

			}
		});
	}

	/**
	 * manages the join EventHandler
	 * 
	 * @param gameID
	 */
	private void doJoin(int gameID) {
		try {
			String color = colorComboBox.getSelectionModel().getSelectedItem();
			lobby.getController().setupJoin(gameID, color);
		} catch (Exception e) {
			log.error(e.getMessage());
			new ErrorView(e.getMessage());
		}
	}

	/**
	 * This helper-method creates all widgets that are provided by this view
	 */
	private void createWidgets() {
		imageJoin = new Image(BTNJOINWOOD, 85, 35, true, true);
		imvJoin = new ImageView();
		grid = new GridPane();
		lblGreeting = new Label("Hi!");
		lblChooseColor = new Label("Choose a color!");
		lblColorTaken = new Label("");
		btnGo = new Button("");
		colorOptions = FXCollections.observableArrayList("aqua", "black",
				"blue", "fuchsia", "gray", "green", "lime", "maroon", "navy",
				"olive", "orange", "purple", "red", "silver", "teal", "white",
				"yellow");
		colorComboBox = new ComboBox<String>(colorOptions);

		colorComboBox
				.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
					@Override
					public ListCell<String> call(ListView<String> list) {
						return new ColorRectCell();
					}
				});
	}

	static class ColorRectCell extends ListCell<String> {
		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			Rectangle rect = new Rectangle(120, 18);
			if (item != null) {
				rect.setFill(Color.web(item));
				setGraphic(rect);
			}
		}
	}

	/**
	 * This helper-method adds all created widgets to the scene.
	 */
	private void addWidgets() {
		grid.setPrefSize(516, 280);
		grid.setHgap(0);
		grid.setVgap(5);
		grid.setPadding(new Insets(150, 80, 80, 80));
		grid.add(lblGreeting, 0, 1);
		grid.add(lblChooseColor, 0, 2);
		grid.add(colorComboBox, 1, 2);
		grid.add(lblColorTaken, 0, 3);
		grid.add(btnGo, 0, 4);
		imvJoin.setImage(imageJoin);

	}

	/**
	 * This helper-method sets the layout for the whole view.
	 */
	private void setLayout() {
		lblColorTaken.setId("lblColorTaken");
		btnGo.setGraphic(imvJoin);
		btnGo.setDisable(true);
		lblGreeting.setPrefSize(80, 25);
		lblColorTaken.setPrefSize(220, 25);
		lblChooseColor.setPrefSize(170, 25);

	}
}

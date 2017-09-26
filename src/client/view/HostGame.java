package client.view;

import static shared.Configuration.BTNHOSTWOOD;

import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import shared.enums.CapabilitiesType;
import shared.enums.JsonType;
import shared.model.Failure;
import client.controller.CarcassonneController;

/**
 * This class provides a window that pops up if you choose to host a new game. You can also insert a name for your game.
 * The created game will be shown in the table if you press the host button. Is an observer -> Observer-Pattern
 * 
 * @version 17.01.2014
 * @see Application
 * @see ChatLobby
 * @see ObserverView
 * @see ObserveErrorView
 * @see GameView
 * @see JoinGame
 * @see Login
 * @see PrivateTab
 */
public final class HostGame extends Thread implements Observer {

	private GridPane grid;

	private TextField txtGameName;
	private TextField txtTurnTime;

	private Button btnHost;

	private Label lblGameName;
	private Label lblTurnTime;
	private Label lblGameColor;
	private Label lblFail;
	private Label lblBigMeeple;
	private Label lblBishop;
	private Label lblInn;
	private Image imageHost;
	private ImageView imvHost;

	private Scene scene;
	private Stage primaryStage;

	private ComboBox<String> colorComboBox;

	private CheckBox cBoxBigMeeple;
	private CheckBox cBoxBishop;
	private CheckBox cBoxInns;

	private ObservableList<String> colorOptions;

	public CarcassonneController controller;

	/**
	 * Only constructor takes a reference to the CarcassonneController as argument.
	 * 
	 * @param controller
	 */
	public HostGame(CarcassonneController controller) {
		this.controller = controller;
		controller.getControl().addObserver(this);
		createWidgets();
		setLayout();
		addWidgets();
		setupInteraction();
	}

	/**
	 * This method is needed in order to display the Host-Dialog.
	 * 
	 * @param primaryStage
	 *            Stage which will be set
	 * 
	 */
	public void start(Stage primaryStage) {
		scene = new Scene(grid);
		scene.getStylesheets().add(shared.Configuration.STYLESHEETHOSTGAMEVIEW);
		primaryStage = new Stage();
		this.primaryStage = primaryStage;
		primaryStage.setScene(scene);
		primaryStage.setTitle("Host Game");
		primaryStage.setResizable(true);
		primaryStage.show();
	}

	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof JsonType) {
			JsonType jsonMsg = (JsonType) arg;
			switch (jsonMsg) {
			case NEWGAME:
				controller.getControl().deleteObserver(this);
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
					lblFail.setText(failure.getName());
				}
			});
			Platform.setImplicitExit(true);
		}
	}

	/**
	 * This helper-method adds all eventhandler provided and needed by this view
	 */
	private void setupInteraction() {
		final DropShadow shadow = new DropShadow();
		// Adds shadows if mouse moves towards the button
		btnHost.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnHost.setEffect(shadow);
					}
				});
		// Removes shadows if mouse moves away from the button
		btnHost.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnHost.setEffect(null);
					}
				});

		txtGameName.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent e) {
				// Login only possible when TxtGameName isn't empty
				String gamename = txtGameName.getText();
				gamename = gamename.trim();
				if (gamename != null
						&& !gamename.isEmpty()
						&& colorComboBox.getSelectionModel().getSelectedItem() != null) {
					btnHost.setDisable(false);
					if (e.getCode() == KeyCode.ENTER) {
						host();
					}

				} else {
					btnHost.setDisable(true);
				}
			}

		});

		colorComboBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				String gamename = txtGameName.getText();
				gamename = gamename.trim();
				if (gamename != null
						&& !gamename.isEmpty()
						&& colorComboBox.getSelectionModel().getSelectedItem() != null) {
					btnHost.setDisable(false);
				} else {
					btnHost.setDisable(true);
				}

			}
		});

		btnHost.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				host();

			}
		});
	}

	// Creates a new entry in the table
	private synchronized void host() {
		String gameName = txtGameName.getText();
		String color = colorComboBox.getSelectionModel().getSelectedItem();
		int turntime = Integer.parseInt(txtTurnTime.getText());
		List<CapabilitiesType> extension = new ArrayList<>();

		if (cBoxBigMeeple.isSelected()) {
			extension.add(CapabilitiesType.BIGMEEPLE);
		}
		if (cBoxBishop.isSelected()) {
			extension.add(CapabilitiesType.BISHOP);
		}
		if (cBoxInns.isSelected()) {
			extension.add(CapabilitiesType.INNS);
		}

		controller.getControl().checkNewGameCreation(gameName, color, turntime,
				extension);
	}

	/**
	 * This helper-method creates all widgets that are provided by this view
	 */
	private void createWidgets() {
		grid = new GridPane();
		txtGameName = new TextField();
		txtTurnTime = new TextField();

		lblGameColor = new Label("Gamecolor");
		lblTurnTime = new Label("Turntime");
		lblGameName = new Label("Gamename");
		lblFail = new Label();
		lblBigMeeple = new Label("Big Meeple");
		lblBishop = new Label("Bishop");
		lblInn = new Label("Inns and Cathedrals");
		imageHost = new Image(BTNHOSTWOOD, 85, 35, true, true);
		imvHost = new ImageView();
		btnHost = new Button("");

		colorOptions = FXCollections.observableArrayList("aqua", "black",
				"blue", "fuchsia", "gray", "green", "lime", "maroon", "navy",
				"olive", "orange", "purple", "red", "silver", "teal", "white",
				"yellow");

		colorComboBox = new ComboBox<>(colorOptions);
		colorComboBox = new ComboBox<>(colorOptions);

		cBoxBigMeeple = new CheckBox();
		cBoxBishop = new CheckBox();
		cBoxInns = new CheckBox();

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
		grid.setPrefSize(516, 322);
		grid.setHgap(10);
		grid.setVgap(5);
		grid.setPadding(new Insets(150, 80, 80, 80));

		grid.add(lblGameName, 0, 1);
		grid.add(lblTurnTime, 0, 2);
		grid.add(lblBigMeeple, 0, 3);
		grid.add(lblBishop, 0, 4);
		grid.add(lblInn, 0, 5);
		grid.add(lblGameColor, 0, 7);
		grid.add(lblFail, 0, 8);

		grid.add(txtGameName, 1, 1);
		grid.add(txtTurnTime, 1, 2);
		grid.add(cBoxBigMeeple, 1, 3);
		grid.add(cBoxBishop, 1, 4);
		grid.add(cBoxInns, 1, 5);
		grid.add(colorComboBox, 1, 7);
		grid.add(btnHost, 0, 9);

		imvHost.setImage(imageHost);
	}

	/**
	 * This helper-method sets the layout for the whole view.
	 */
	private void setLayout() {
		btnHost.setGraphic(imvHost);
		btnHost.setDisable(true);
		txtGameName.setPrefSize(150, 25);
		txtTurnTime.setText("180");
		lblGameName.setPrefSize(150, 25);
		lblGameColor.setPrefSize(150, 25);
		lblFail.setPrefSize(300, 50);
		GridPane.setColumnSpan(lblFail, 2);
		lblFail.setId("lblFail");
		txtGameName.setId("gameName");
		txtTurnTime.setId("turnTime");
		lblInn.setPrefSize(240, 25);

	}
}
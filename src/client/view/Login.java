package client.view;

import static shared.Configuration.BTNSTARTWOOD;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.regex.Pattern;

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
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import shared.Configuration;
import shared.enums.JsonType;
import shared.model.Failure;
import client.controller.CarcassonneController;
import client.model.clientCommunication.ClientControl;
import client.model.game.ClientGame;

/**
 * This class provides the first dialog after starting the client.
 * <p>
 * It wants you to enter your nickname then you will be forwarded to the ChatLobby and your nick will be sent to the
 * server and now you are a logged in user. A Label under the textfields informs you if your nickname is already taken.
 * 
 * @version 17.01.2014
 * @see Application
 * @see ChatLobby
 * @see ObserverView
 * @see ObserveErrorView
 * @see GameView
 * @see JoinGame
 * @see HostGame
 * @see PrivateTab
 */
public final class Login extends Application implements Observer {

	private GridPane grid;

	private TextField txtNickname;
	private TextField txtPort;
	private TextField txtAdress;

	private Label lblNick;
	private Label lblPort;
	private Label lblAdress;
	private Label lblCardDesign;
	private Label lblNickTaken;

	private Button btnLogin;
	private Image imageStart;
	private ImageView imvStart;

	private Scene scene;
	private Stage primaryStage;
	private CarcassonneController controller;
	private ComboBox<String> comboBoxCardDesign;
	private ObservableList<String> cardDesignOptions;

	/**
	 * Only constructor.
	 */
	public Login() {

		createWidgets();
		setLayout();
		addWidgets();
		setupInteraction();
	}

	/**
	 * Delegates the login attempt and sets the card design in the configuration file.
	 */
	public void tryLogin() {
		controller.setupClient(txtNickname.getText());
		String cardDesign = comboBoxCardDesign.getSelectionModel()
				.getSelectedItem();
		Configuration.chooseCardDesign(cardDesign);
		Configuration.chooseMeepleDesign(cardDesign);
	}

	/**
	 * This method is needed in order to display the Login-Dialog.
	 */
	@Override
	public void start(Stage primaryStage) {

		scene = new Scene(grid);
		scene.getStylesheets().add(shared.Configuration.STYLESHEETLOGINVIEW);
		this.primaryStage = primaryStage;
		primaryStage.setScene(scene);
		primaryStage.setTitle("Login");
		primaryStage.setResizable(true);
		primaryStage.show();

	}

	/**
	 * This method checks if the username is already taken. If thats the case a red Label will inform the user.
	 */
	@Override
	public void update(Observable o, Object arg) {

		if (arg instanceof JsonType) {
			JsonType jsonMsg = (JsonType) arg;
			switch (jsonMsg) {
			case LOGINSUCCESS:
				final Set<String> users = ((ClientControl) o).getPlayerList();
				final Collection<ClientGame> games = ((ClientControl) o)
						.getGames().values();
				o.deleteObserver(this);

				Platform.runLater(new Runnable() {
					public void run() {
						ChatLobby chatlobby = ChatLobby.getInstance(controller,
								users);
						controller.setChatLobby(chatlobby);
						for (ClientGame game : games) {
							chatlobby
									.getObservableGames()
									.add(new GameEntry(
											game.getGameName(),
											game.getHost(),
											game.getPlayerList().size(),
											game.getGameID(),
											ChatLobby
													.generateExtensionsStringfromGameExtensions(game)));
						}
						primaryStage.close();
						controller.setLoginView(null);
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
					txtNickname.clear();
					lblNickTaken.setText(failure.getName());
				}
			});
			Platform.setImplicitExit(true);
		}
	}

	/**
	 * This helper-method adds all eventhandler provided and needed by this view
	 */
	private void setupInteraction() {

		txtPort.setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				String port = txtPort.getText();
				if (!Pattern.matches("[0-9]+", port)) {
					txtPort.clear();
				}
			}
		});

		final DropShadow shadow = new DropShadow();
		// Adds shadows if mouse moves towards the button
		btnLogin.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnLogin.setEffect(shadow);
					}
				});

		// Removes shadows if mouse moves away from the button
		btnLogin.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnLogin.setEffect(null);
					}
				});

		txtNickname.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				// Login only possible when TxtNickname isn't empty
				String name = txtNickname.getText();
				name = name.trim();
				if (name != null && !name.isEmpty()) {
					btnLogin.setDisable(false);
					if (e.getCode() == KeyCode.ENTER) {
						setController();
						tryLogin();
					}

				} else {
					btnLogin.setDisable(true);
				}

			}

		});
		btnLogin.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				setController();
				tryLogin();
			}
		});
	}

	/**
	 * This helper-method creates all widgets that are provided by this view
	 */
	private void createWidgets() {
		grid = new GridPane();
		txtNickname = new TextField();
		txtPort = new TextField();
		txtAdress = new TextField();

		lblNick = new Label("Nickname");
		lblNickTaken = new Label();
		lblPort = new Label("Port");
		lblAdress = new Label("Adress");
		lblCardDesign = new Label("Carddesign");

		btnLogin = new Button("");
		imageStart = new Image(BTNSTARTWOOD, 85, 35, true, true);
		imvStart = new ImageView();

		cardDesignOptions = FXCollections.observableArrayList(
				"original design", "pirate design", "graveyard design");
		comboBoxCardDesign = new ComboBox<String>(cardDesignOptions);

		comboBoxCardDesign
				.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
					@Override
					public ListCell<String> call(ListView<String> list) {
						return new ExtCell();
					}

				});
	}

	static class ExtCell extends ListCell<String> {
		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null) {
				setText(item);
			}
		}
	}

	/**
	 * This helper-method adds all created widgets to the scene.
	 */
	private void addWidgets() {
		grid.setHgap(10);
		grid.setVgap(5);
		grid.setPadding(new Insets(150, 80, 80, 80));
		grid.add(lblPort, 0, 1);
		grid.add(txtPort, 1, 1);
		grid.add(lblAdress, 0, 2);
		grid.add(txtAdress, 1, 2);
		grid.add(lblNick, 0, 4);
		grid.add(txtNickname, 1, 4);
		grid.add(lblNickTaken, 1, 5);
		grid.add(lblCardDesign, 0, 3);
		grid.add(comboBoxCardDesign, 1, 3);
		grid.add(btnLogin, 0, 7);
		imvStart.setImage(imageStart);
	}

	/**
	 * This helper-method sets the layout for the whole view.
	 */
	private void setLayout() {
		txtNickname.setId("txtNickname");
		txtPort.setId("txtPort");
		txtAdress.setId("txtAdress");
		lblNickTaken.setId("lblNickTaken");

		grid.setPrefSize(516, 200);
		btnLogin.setDisable(true);
		txtNickname.setPrefSize(200, 25);
		lblNickTaken.setPrefSize(250, 10);
		lblNickTaken.setText(null);
		lblNickTaken.setTextFill(Color.RED);
		lblNick.setPrefSize(120, 50);
		txtAdress.setText("localhost");
		txtPort.setText("4455");
		lblCardDesign.setPrefSize(175, 25);
		comboBoxCardDesign.getSelectionModel().select(0);
		btnLogin.setGraphic(imvStart);
	}

	/*
	 * GETTER and SETTER below
	 */

	public void setController() {
		this.controller = CarcassonneController.getInstance(null, this);
		controller.setControl(ClientControl.getInstance(controller,
				txtAdress.getText(), Integer.parseInt(txtPort.getText())));
		this.controller.getControl().addObserver(this);
	}
}
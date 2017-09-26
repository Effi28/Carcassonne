package client.view;

import static shared.Configuration.BTNEXITWOOD;
import static shared.Configuration.BTNHOSTWOOD;
import static shared.Configuration.BTNJOINWOOD;
import static shared.Configuration.BTNOBSERVEWOOD;
import static shared.Configuration.BTNSTARTWOOD;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import shared.enums.CapabilitiesType;
import shared.enums.GameStatus;
import shared.enums.JsonType;
import shared.view.ErrorView;
import client.controller.CarcassonneController;
import client.model.clientCommunication.ClientControl;
import client.model.game.ClientGame;

/**
 * Provides the main GUI for the game lobby.
 * <p>
 * It provides two tabs: Chat-Lobby and Game-Lobby. The Chat-Lobby is a lobby for all users who are currently logged in.
 * The Game-Lobby displays the different games currently hosted. You can join them (as a player and spectator) or host a
 * game by yourself.
 * 
 * Is an Observer (-> Observer-Pattern) and implements the Singleton-Pattern.
 * 
 * @version 17.01.2014
 * 
 * @see Application
 * @see GameView
 * @see ObserverView
 * @see ObserveErrorView
 * @see HostGame
 * @see JoinGame
 * @see Login
 * @see PrivateTab
 * 
 */
public final class ChatLobby extends Application implements Observer {

	/**
	 * Error logger
	 */
	private static Logger log = LogManager.getLogger("ERROR");

	private GridPane gridChatLobby;
	private GridPane gridGameLobby;

	private TabPane tabMenu;
	private Tab tabChat;
	private Tab tabPlay;

	private TextArea chatWindow;

	private TextField insertLine;

	private Button btnObserve;
	private Button btnJoin;
	private Button btnHost;
	private Button btnExitGame;
	private Button btnStartHostedGame;

	private HBox hBoxChat;
	private HBox buttonBox;
	private HBox buttonBoxLeft;
	private HBox buttonBoxRight;

	private Image imageObserve;
	private ImageView imvObserve;

	private Image imageExit;
	private ImageView imvExit;

	private Image imageJoin;
	private ImageView imvJoin;

	private Image imageHost;
	private ImageView imvHost;

	private Image imageStart;
	private ImageView imvStart;

	private Label lblFail;
	private TableView<GameEntry> gamesTable;

	private Scene scene;

	private volatile ArrayList<GameEntry> games;
	private volatile ObservableList<GameEntry> observableGames;

	private TableView<Button> usersTable;
	// private ListView<Button> users;
	private ObservableList<Button> observableNames;

	/**
	 * Manages all tabs for the privateChats
	 */
	private ArrayList<PrivateTab> tabSet;

	private CarcassonneController controller;
	private final Stage primaryStage;

	protected int gameID;

	/**
	 * Instance of this class. -> Singleton-Pattern
	 */
	private static ChatLobby instance;

	/**
	 * Only constructor which is private. Should only be invoked by getInstance() because of the Singleton-Pattern
	 * 
	 * @param controller
	 * @param existingUsers
	 */
	private ChatLobby(CarcassonneController controller,
			Set<String> existingUsers) {

		// temporarily, should be instantiated on first start with already
		// existing games

		tabSet = new ArrayList<PrivateTab>();
		observableNames = FXCollections.observableArrayList();
		// users = new ListView<Button>(observableNames);
		createInitialUserList(existingUsers);

		games = new ArrayList<GameEntry>();
		this.controller = controller;
		getController().getControl().addObserver(this);
		primaryStage = new Stage();
		createWidgets();
		setLayout();
		addWidgets();
		setupInteraction();
		start(primaryStage);
	}

	/**
	 * Gets the instance of the ChatLobby. -> Singleton-Pattern
	 * 
	 * @param controller
	 * @param existingUsers
	 * @return
	 */
	public synchronized static ChatLobby getInstance(
			CarcassonneController controller, Set<String> existingUsers) {
		if (instance == null) {
			instance = new ChatLobby(controller, existingUsers);
		}
		return instance;
	}

	/**
	 * Adds a message to the chat
	 * 
	 * @param message
	 *            message to display
	 */
	public void addText(String message) {
		chatWindow.appendText(message + "\n");
	}

	/**
	 * This method is needed in order to display the ChatLobby.
	 */
	@Override
	public void start(Stage primaryStage) {
		scene = new Scene(tabMenu);
		scene.getStylesheets()
				.add(shared.Configuration.STYLESHEETCHATLOBBYVIEW);
		primaryStage.setMaxHeight(Double.MAX_VALUE);
		primaryStage.setScene(scene);
		primaryStage.setResizable(true);
		primaryStage.setTitle("Chat-Lobby" + " : "
				+ controller.getControl().getNick());
		primaryStage.show();
	}

	@Override
	public synchronized void update(Observable o, Object arg) {

		Collection<ClientGame> games = ((ClientControl) o).getGames().values();

		if (arg instanceof JsonType) {
			JsonType updateMsg = (JsonType) arg;
			switch (updateMsg) {

			case PLAYERADDED:
			case PLAYERREMOVED:

				observableNames.clear();
				Set<String> players = ((ClientControl) o).getPlayerList();
				for (String s : players) {
					observableNames.add(new UserEntry(s, this));
				}

				if (observableGames != null) {
					observableGames.clear();
					for (ClientGame game : games) {
						int playercount = game.getPlayerList().size();
						if (playercount != 0) {
							String extensions = "";
							Iterator<CapabilitiesType> iterator = game
									.getExtensions().iterator();
							while (iterator.hasNext()) {
								extensions += iterator.next().toString();
							}
							observableGames.add(new GameEntry(game
									.getGameName(), game.getHost(),
									playercount, game.getGameID(), extensions));
						}
					}
				}
				break;
			case LEAVEGAME:
			case NEWGAME:
			case GAMEREMOVED:
			case GAMEUPDATE:
				if (observableGames != null) {
					observableGames.clear();
					for (ClientGame game : games) {
						int playercount = game.getPlayerList().size();
						// If there are enough player in the game
						if (playercount != 0) {
							String extensions = generateExtensionsStringfromGameExtensions(game);
							observableGames.add(new GameEntry(game
									.getGameName(), game.getHost(),
									playercount, game.getGameID(), extensions));
						}
					}
				}

				break;

			case PLAYERLEFT:
				observableGames.clear();
				for (ClientGame game : games) {
					int playerCount = game.getPlayerList().size();
					if (playerCount != 0) {
						observableGames
								.add(new GameEntry(
										game.getGameName(),
										game.getHost(),
										playerCount,
										game.getGameID(),
										ChatLobby
												.generateExtensionsStringfromGameExtensions(game)));
					}
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Static helper method which returns a formated String with all extensions of a game
	 * 
	 * @param game
	 *            game with the extensions
	 * @return extensions string from the game
	 */
	static String generateExtensionsStringfromGameExtensions(ClientGame game) {
		String extensions = "";
		Iterator<CapabilitiesType> iterator = game.getExtensions().iterator();
		while (iterator.hasNext()) {
			extensions += iterator.next().toString() + ", ";
		}
		if (!extensions.equals("")) {
			extensions = extensions.substring(0, extensions.length() - 2);
		}
		return extensions;
	}

	/**
	 * Creates a new PrivateChatTab if the user wants to start a private chat or if the user received a private chat
	 * message.
	 * 
	 * @param name
	 * @return
	 */
	public PrivateTab createNewPrivateChatTab(String name) {
		final PrivateTab tab = new PrivateTab(name, this, true);
		tab.setOnClosed(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				tabSet.remove(tab);
			}

		});
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				tabSet.add(tab);
				tabMenu.getTabs().add(tab);

			}

		});
		Platform.setImplicitExit(true);
		return tab;
	}

	/**
	 * Adds the shadows to the buttons
	 */
	private void addShadowToButtons() {
		final DropShadow shadow = new DropShadow();

		// Adds shadows if mouse moves towards the button
		btnJoin.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnJoin.setEffect(shadow);
					}
				});

		btnObserve.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnObserve.setEffect(shadow);
					}
				});

		btnHost.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnHost.setEffect(shadow);
					}
				});

		btnStartHostedGame.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnStartHostedGame.setEffect(shadow);
					}
				});

		btnExitGame.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnExitGame.setEffect(shadow);
					}
				});

		// Removes shadows if mouse moves away from the button
		btnJoin.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnJoin.setEffect(null);
					}
				});

		btnObserve.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnObserve.setEffect(null);
					}
				});

		btnHost.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnHost.setEffect(null);
					}
				});

		btnStartHostedGame.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnStartHostedGame.setEffect(null);
					}
				});

		btnExitGame.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						btnExitGame.setEffect(null);
					}
				});
	}

	/**
	 * Fires an ActionEvent if one User in the active user table is clicked
	 */
	private void setupClickUser() {
		usersTable.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				UserEntry selected = (UserEntry) usersTable.getSelectionModel()
						.getSelectedItem();
				String nick = getController().getControl().getNick();
				if (selected != null
						&& selected.getName().equals(nick) == false) {
					usersTable.getSelectionModel().getSelectedItem()
							.fireEvent(new ActionEvent());
				}
			}
		});
	}

	private void createInitialUserList(Set<String> existingUsers) {
		for (Iterator<String> iterator = existingUsers.iterator(); iterator
				.hasNext();) {
			String user = (String) iterator.next();
			observableNames.add(new UserEntry(user, this));
		}
	}

	/**
	 * Performs the logout of the client by closing the streams and sending the server a logout request
	 */
	private void logout() {
		controller.getControl().doLogout();
	}

	/**
	 * Creates an EventHandler for the close operation, because the client needs to logout
	 */
	private void setLogoutHandler() {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg) {
				logout();
			}
		});
	}

	/**
	 * This is a helper method which starts a new view if a user wants to observe a game
	 */
	private void watchGame() {
		int gameID = getGameIDFromSelectedItem();

		controller.watchGame(gameID);
		ClientGame game = controller.getControl().getGames().get(gameID);

		if (game.getState().equals(GameStatus.NOTSTARTED)) {
			ObserveErrorView observe = new ObserveErrorView(this);
			try {
				observe.start(new Stage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This helper-method adds all EventHandler provided and needed by this view
	 */
	private void setupInteraction() {

		addShadowToButtons();

		btnObserveAddListener();

		insertLineAddListener();

		// ActionHandler for the current selcted item in the Table
		gamesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				try {
					gameID = gamesTable.getSelectionModel().getSelectedItem()
							.getGameID();
				} catch (Exception e) {

				}
			}

		});

		// Insert Text into ChatWindow with Enter
		insertLine.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					sendText();
				}
			}
		});

		// Insert Text into ChatWindow with SendBtn
		btnExitGame.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				try {
					leaveGame(gamesTable.getSelectionModel().getSelectedItem()
							.getGameID());
				} catch (NullPointerException e) {
					log.error("You have to select the game you want to leave!");
					new ErrorView(
							"You have to select the game you want to leave!");
				}
			}
		});

		// added eventHandler for btnHost
		btnHost.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				doHosting();
			}
		});

		// added eventHandler for btnJoin
		btnJoin.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				joinGame();
			}
		});

		tabMenu.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<Tab>() {

					@Override
					public void changed(ObservableValue<? extends Tab> arg0,
							Tab arg1, Tab arg2) {

						if (arg2 instanceof PrivateTab) {
							((PrivateTab) arg2).getThread().interrupt();
						}
					}

				});

		btnStartHostedGameAddListener();
		gamesTableAddListener();
		setLogoutHandler();
		setupClickUser();
	}

	/**
	 * Delegates to the controller that the user wants to leave the game.
	 * 
	 * @param gameID
	 */
	private void leaveGame(int gameID) {
		controller.leaveGame(gameID);
	}

	/**
	 * Helper method for adding EventHandler for insertLine
	 */
	private void insertLineAddListener() {
		insertLine.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				// Sending msgs only possible when InsertLine != null
				String chattext = insertLine.getText();
				chattext = chattext.trim();
				if (chattext != null && !chattext.isEmpty()) {
					if (e.getCode() == KeyCode.ENTER) {
						sendText();
					}
				}
			}
		});
	}

	/**
	 * Helper method for adding EventHandler for btnObserve
	 */
	private void btnObserveAddListener() {
		btnObserve.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				watchGame();
			}
		});
	}

	/**
	 * Helper method for adding EventHandler for btnStartHostedGame
	 */
	private void btnStartHostedGameAddListener() {
		btnStartHostedGame.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				startHostedGame();
			}
		});
	}

	/**
	 * Helper method for adding EventHandler for gamesTable
	 */
	private void gamesTableAddListener() {
		gamesTable.getSelectionModel().getSelectedIndices()
				.addListener(new ListChangeListener<Integer>() {

					@Override
					public void onChanged(
							javafx.collections.ListChangeListener.Change<? extends Integer> arg0) {
						GameEntry selected = gamesTable.getSelectionModel()
								.getSelectedItem();
						if (selected != null) {
							String host = selected.getHost();
							if (host.equals(controller.getControl().getNick())) {
								if (Integer.parseInt(selected.getPlayers()
										.substring(0, 1)) > 1) {
									btnStartHostedGame.setDisable(false);
								}
								btnObserve.setDisable(true);
								btnJoin.setDisable(true);
								btnExitGame.setDisable(true);
							} else {
								btnStartHostedGame.setDisable(true);
								btnExitGame.setDisable(false);
								btnObserve.setDisable(false);
								btnJoin.setDisable(false);
							}
						}
					}
				});
	}

	/**
	 * Clear TextField and send the written text to the server
	 * 
	 */
	private void sendText() {
		String inputText = insertLine.getText();
		controller.sendMessage(inputText);
		insertLine.clear();
	}

	/**
	 * This helper-method manages the hosting of a game
	 */
	private void doHosting() {
		HostGame hostgame = new HostGame(controller);
		hostgame.start(new Stage());

	}

	/**
	 * This helper-method manages the joining of a game
	 */
	private void joinGame() {
		JoinGame chooseColor = new JoinGame(this, gameID);
		try {
			chooseColor.start(new Stage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Returns the gameID of the currently selected game
	 * 
	 * @return gameID
	 */
	private int getGameIDFromSelectedItem() {
		return gamesTable.getSelectionModel().getSelectedItem().getGameID();
	}

	/**
	 * This helper-method manages the starting of a game
	 */
	private void startHostedGame() {

		int gameID = getGameIDFromSelectedItem();

		// Send start message
		controller.setupGame(gameID);

		// // open the view
		// GameView gameView = new GameView(gameID, controller);
		// try {
		// gameView.start(new Stage());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	/**
	 * This helper-method creates all widgets that are provided by this view
	 */
	private void createWidgets() {
		gridChatLobby = new GridPane();
		gridGameLobby = new GridPane();

		imageObserve = new Image(BTNOBSERVEWOOD, 85, 35, true, true);
		imvObserve = new ImageView();
		imageExit = new Image(BTNEXITWOOD, 85, 35, true, true);
		imvExit = new ImageView();
		imageHost = new Image(BTNHOSTWOOD, 85, 35, true, true);
		imvHost = new ImageView();
		imageJoin = new Image(BTNJOINWOOD, 85, 35, true, true);
		imvJoin = new ImageView();
		imageStart = new Image(BTNSTARTWOOD, 85, 35, true, true);
		imvStart = new ImageView();

		buttonBox = new HBox();
		buttonBoxLeft = new HBox();
		buttonBoxRight = new HBox();
		chatWindow = new TextArea();
		hBoxChat = new HBox();
		tabMenu = new TabPane();
		tabChat = new Tab("Chat-Lobby");
		tabPlay = new Tab("Play Carcassonne");
		insertLine = new TextField();
		btnObserve = new Button("");
		btnObserve.setDisable(true);
		btnExitGame = new Button("");
		btnExitGame.setDisable(true);
		btnJoin = new Button("");
		btnJoin.setDisable(true);
		btnHost = new Button("");
		btnStartHostedGame = new Button("");
		btnStartHostedGame.setDisable(true);
		lblFail = new Label();
		setupTableView();
		setupLoginView();
	}

	/**
	 * This method creates a table in which you can see all existing games
	 */
	@SuppressWarnings("unchecked")
	private synchronized void setupTableView() {
		observableGames = FXCollections.observableArrayList(games);
		gamesTable = new TableView<GameEntry>(observableGames);

		TableColumn<GameEntry, String> name = new TableColumn<>("Name");
		name.setCellValueFactory(new PropertyValueFactory<GameEntry, String>(
				"name"));
		name.prefWidthProperty()
				.bind(gamesTable.widthProperty().multiply(0.22));

		TableColumn<GameEntry, String> players = new TableColumn<>("Players");
		players.setCellValueFactory(new PropertyValueFactory<GameEntry, String>(
				"players"));
		players.prefWidthProperty().bind(
				gamesTable.widthProperty().multiply(0.10));

		TableColumn<GameEntry, String> host = new TableColumn<>("Host");
		host.setCellValueFactory(new PropertyValueFactory<GameEntry, String>(
				"host"));
		host.prefWidthProperty()
				.bind(gamesTable.widthProperty().multiply(0.22));

		TableColumn<GameEntry, String> extensions = new TableColumn<>(
				"Extensions");
		extensions
				.setCellValueFactory(new PropertyValueFactory<GameEntry, String>(
						"extensions"));
		extensions.prefWidthProperty().bind(
				gamesTable.widthProperty().multiply(0.48));

		gamesTable.getColumns().setAll(name, players, host, extensions);
	}

	/**
	 * This method creates a table in which you can see all players who are online
	 */
	@SuppressWarnings("unchecked")
	private void setupLoginView() {
		usersTable = new TableView<Button>(observableNames);

		TableColumn<Button, String> name = new TableColumn<>("Player");
		name.setCellValueFactory(new PropertyValueFactory<Button, String>(
				"name"));
		name.prefWidthProperty().bind(usersTable.widthProperty().multiply(1.0));

		usersTable.getColumns().setAll(name);
	}

	/**
	 * This helper-method adds all created widgets to the scene.
	 */
	private void addWidgets() {
		tabMenu.getTabs().add(tabChat);
		tabMenu.getTabs().add(tabPlay);

		gridChatLobby.setHgap(10);
		gridChatLobby.setVgap(10);
		gridChatLobby.setPadding(new Insets(10, 10, 10, 10));
		gridChatLobby.add(chatWindow, 0, 1);
		gridChatLobby.add(usersTable, 1, 1);
		gridChatLobby.add(hBoxChat, 0, 2);
		hBoxChat.getChildren().addAll(insertLine);

		gridGameLobby.setHgap(10);
		gridGameLobby.setVgap(10);
		gridGameLobby.setPadding(new Insets(10, 10, 10, 10));
		gridGameLobby.add(gamesTable, 0, 1);
		gridGameLobby.add(lblFail, 0, 2);
		gridGameLobby.add(buttonBox, 0, 3);
		buttonBoxLeft.getChildren().addAll(btnHost, btnStartHostedGame);
		buttonBoxRight.getChildren().addAll(btnExitGame, btnJoin, btnObserve);
		buttonBox.getChildren().addAll(buttonBoxLeft, buttonBoxRight);
		buttonBox.setSpacing(450);

		tabChat.setContent(gridChatLobby);
		tabPlay.setContent(gridGameLobby);

		imvObserve.setImage(imageObserve);
		imvExit.setImage(imageExit);
		imvHost.setImage(imageHost);
		imvJoin.setImage(imageJoin);
		imvStart.setImage(imageStart);
	}

	/**
	 * This helper-method sets the layout for the whole view.
	 */
	private void setLayout() {
		gridChatLobby.setId("gridChatLobby");
		gridGameLobby.setId("gridGameLobby");
		insertLine.setId("insertLine");

		btnObserve.setId("btnObserve");
		btnExitGame.setId("btnExit");
		btnHost.setId("btnHost");
		btnJoin.setId("btnJoin");
		btnStartHostedGame.setId("btnStart");
		tabChat.setClosable(false);
		tabPlay.setClosable(false);

		chatWindow.setPrefSize(485, 450);
		chatWindow.setEditable(false);
		chatWindow.setWrapText(true);
		chatWindow.setId("chat");

		btnObserve.setGraphic(imvObserve);
		btnExitGame.setGraphic(imvExit);
		btnHost.setGraphic(imvHost);
		btnJoin.setGraphic(imvJoin);
		btnStartHostedGame.setGraphic(imvStart);

		insertLine.setPrefWidth(700);
		insertLine.setPromptText("");
		usersTable.setPrefSize(140, 450);
		gamesTable.setPrefSize(830, 450);
		lblFail.setPrefSize(700, 25);
		lblFail.setTextFill(Color.RED);
		lblFail.setFont(Font.font("Arial", FontWeight.BOLD, 14));
	}

	/*
	 * GETTER and SETTER below
	 */

	public synchronized ObservableList<GameEntry> getObservableGames() {
		return observableGames;
	}

	public CarcassonneController getController() {
		if (controller == null) {
			throw new NullPointerException("CONTROLLER NULL");
		}
		return controller;
	}

	public ArrayList<PrivateTab> getTabSet() {
		return tabSet;
	}

	public TableView<GameEntry> getGamesTable() {
		return gamesTable;
	}
}
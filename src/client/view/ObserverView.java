package client.view;

import client.controller.CarcassonneController;
import client.model.clientCommunication.ClientControl;
import client.model.game.ClientGame;
import client.model.game.ClientMoveMadeStorage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import shared.enums.GameStatus;
import shared.enums.JsonType;
import shared.enums.SpecialMeepleType;
import shared.model.Placement;
import shared.model.Player;
import shared.model.Position;
import shared.model.Spectator;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import static shared.Configuration.*;

/**
 * Provides the view for an user who wants to observe a game.
 * <p/>
 * Gives the user the possibility to follow a game while not actively playing.
 * After joining a game that has already been started, it is possible to step
 * through every move that was made. Furthermore it is possible to go straight
 * to the actual state of the game.
 *
 * @version 09.01.2014
 */
public final class ObserverView extends Application implements Observer {

	private static ObserverView instance;
	private Scene scene;
	private Stage primaryStage;

	// distance between cards
	private static final int GAP = 5;
	private static double CARDSIZE;

	private Spectator currentPlayer;
	private CarcassonneController controller;
	private Placement currentPlacement;

	// so all images are positioned perfectly
	private double sceneWidth;
	private double sceneHeight;
	// panes needed for GameView
	private Pane pane;
	private Group group;
	private Group zoomGroup;
	private ScrollPane scrollPane;
	private Pane gameBoard;

	// background images
	private ImageView imgLeftBackground;
	private ImageView imgRightBackground;

	// card holder
	private ImageView imgCardHolder;
	private Image imgShadow;
	private ImageView imgPreview;
	private ImageView imgPreviewShadow;
	private ImageView imgCardBack;
	private ImageView imgCardBackShadow;
	private Label lblCardsRemaining;

	// player list
	private ImageView imgPlayerList;
	private ObservableList<PlayerEntry> observablePlayerList;
	private TableView<PlayerEntry> playerTable;

	// next move and last move
	private Button btnNext;
	private Button btnLast;

	// imgLeaves
	private ImageView imgLeaves;

	// zoom
	private Button btnZoomIn;
	private Button btnZoomOut;

	// chat
	private ImageView imgChatArea;
	private TextArea txtChatMessages;
	private ImageView imgChatField;
	private TextField txtChatEntry;

	// scale values
	private Scale scaleUp;
	private Scale scaleDown;

	private boolean cardPlaced;

	private int realRotation;
	private int gameID;
	private boolean startHasBeenCalled;
	private Position currentCardPosition;
	private CardPane currentCardPane;
	private ClientMoveMadeStorage moveMadeStorage;

	private ObserverView(final int gameID, CarcassonneController controller, Spectator player, Set<String> playerList, ClientMoveMadeStorage storage) {
		this.gameID = gameID;
		this.controller = controller;
		this.currentPlayer = player;
		this.currentPlacement = new Placement(-1);
		this.moveMadeStorage = storage;

		sceneHeight = Screen.getPrimary().getBounds().getHeight() - 50;
		sceneWidth = (sceneHeight / 10) * 16;

		CARDSIZE = sceneWidth * 0.1007;

		scaleDown = new Scale(0.8, 0.8);
		scaleUp = new Scale(1.2, 1.2);

		createWidgets();
		addWidgets();
		setLayout();
		setupInteraction();
		doInitialPlayerTableInitialisation(playerList);
		controller.getControl().addObserver(this);
	}

	private void setupInteraction() {
		btnNext.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				performMove();
			}
		});

		btnLast.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				performAllMoves();
			}
		});

		btnZoomIn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				double hValue = scrollPane.getHvalue();
				double vValue = scrollPane.getVvalue();
				zoomGroup.getTransforms().add(scaleUp);
				scrollPane.setHvalue(hValue);
				scrollPane.setVvalue(vValue);
			}
		});

		btnZoomOut.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				double hValue = scrollPane.getHvalue();
				double vValue = scrollPane.getVvalue();
				zoomGroup.getTransforms().add(scaleDown);
				scrollPane.setHvalue(hValue);
				scrollPane.setVvalue(vValue);
			}
		});

		txtChatEntry.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				// Sending msgs only possible when InsertLine != null
				String chattext = txtChatEntry.getText();
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
	 * Is called when the first gameUpdate arrives and the the playerTable is set up.
	 *
	 * @param playerList
	 */
	private void doInitialPlayerTableInitialisation(Set<String> playerList) {
		observablePlayerList.clear();
		for (Player p : controller.getControl().getGames().get(gameID).getPlayerList().values()) {
			observablePlayerList.add(new PlayerEntry(p.getNick(), p.getScore(), p.getMeeplesLeft(), p.getColor()));
		}

	}

	/**
	 * Gets the instance of this class because of the Singleton-Pattern.
	 *
	 * @param gameID
	 * @param controller
	 * @param player
	 * @param playerList
	 * @return
	 */
	public static synchronized ObserverView getInstance(int gameID, CarcassonneController controller, Spectator player, Set<String> playerList,
														ClientMoveMadeStorage storage) {
		if (instance == null) {
			instance = new ObserverView(gameID, controller, player, playerList, storage);
		}
		return instance;
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	private void createWidgets() {

		pane = new Pane();
		group = new Group();
		zoomGroup = new Group();
		scrollPane = new ScrollPane();
		gameBoard = new Pane();

		imgLeftBackground = new ImageView(new Image("file:files" + SEPARATOR + "gui" + SEPARATOR + "background_left.png", sceneWidth * 0.75, sceneHeight, false,
				false));
		imgRightBackground = new ImageView(new Image("file:files" + SEPARATOR + "gui" + SEPARATOR + "background_right.png", sceneWidth * 0.25, sceneHeight, false,
				false));

		imgCardHolder = new ImageView(new Image("file:files" + SEPARATOR + "gui" + SEPARATOR + "cardholder_oberver.png", sceneWidth * 0.2358, sceneHeight * 0.235,
				false, false));
		imgShadow = new Image("file:files" + SEPARATOR + "gui" + SEPARATOR + "shadow.png", CARDSIZE, CARDSIZE, false, false);
		imgPreview = new ImageView();
		imgPreviewShadow = new ImageView(imgShadow);
		imgCardBack = new ImageView(new Image("file:files" + SEPARATOR + "gui" + SEPARATOR + "backsideCard.png", sceneWidth * 0.1007, sceneHeight * 0.1611, false,
				false));
		imgCardBackShadow = new ImageView(imgShadow);
		lblCardsRemaining = new Label();
		lblCardsRemaining.setId("remaining");
		InnerShadow is = new InnerShadow();
		is.setOffsetX(2.0f);
		is.setOffsetY(2.0f);
		lblCardsRemaining.setEffect(is);

		imgPlayerList = new ImageView(new Image("file:files" + SEPARATOR + "gui" + SEPARATOR + "players.png", sceneWidth * 0.2319, sceneHeight * 0.3089, false,
				false));
		setupPlayerTable();

		btnNext = new Button();
		btnNext.setId("next-move");

		btnLast = new Button();
		btnLast.setId("last-move");

		imgLeaves = new ImageView(new Image("file:files" + SEPARATOR + "gui" + SEPARATOR + "leaves.png", sceneWidth * 0.75, sceneHeight, false, false));

		btnZoomOut = new Button();
		btnZoomOut.setId("zoom-out");
		btnZoomIn = new Button();
		btnZoomIn.setId("zoom-in");

		imgChatArea = new ImageView(new Image("file:files" + SEPARATOR + "gui" + SEPARATOR + "chatArea.png", sceneWidth * 0.5083, sceneHeight * 0.1756, false,
				false));
		txtChatMessages = new TextArea();
		txtChatMessages.setEditable(false);
		imgChatField = new ImageView(new Image("file:files" + SEPARATOR + "gui" + SEPARATOR + "chatField.png", sceneWidth * 0.5083, sceneHeight * 0.0222, false,
				false));
		txtChatEntry = new TextField();
	}

	private void setupPlayerTable() {

		observablePlayerList = FXCollections.observableArrayList();

		playerTable = new TableView<PlayerEntry>(observablePlayerList);

		TableColumn<PlayerEntry, String> player = new TableColumn<>("Player");
		player.setCellValueFactory(new PropertyValueFactory<PlayerEntry, String>("player"));

		player.prefWidthProperty().bind(playerTable.widthProperty().multiply(0.41));

		TableColumn<PlayerEntry, String> score = new TableColumn<>("Score");
		score.setCellValueFactory(new PropertyValueFactory<PlayerEntry, String>("score"));

		score.prefWidthProperty().bind(playerTable.widthProperty().multiply(0.19));

		TableColumn<PlayerEntry, String> meeples = new TableColumn<>("Meeples");
		meeples.setCellValueFactory(new PropertyValueFactory<PlayerEntry, String>("meeples"));

		meeples.prefWidthProperty().bind(playerTable.widthProperty().multiply(0.19));

		TableColumn<PlayerEntry, String> color = new TableColumn<>("Color");
		color.setCellValueFactory(new PropertyValueFactory<PlayerEntry, String>("color"));

		color.prefWidthProperty().bind(playerTable.widthProperty().multiply(0.20));

		playerTable.getColumns().setAll(player, score, meeples, color);

	}

	private void addWidgets() {
		pane.getChildren().addAll(imgLeftBackground, imgRightBackground, scrollPane, imgLeaves, imgCardHolder, imgPreview, imgPreviewShadow, imgCardBack,
				lblCardsRemaining, imgCardBackShadow, imgPlayerList, playerTable, btnLast, btnNext, btnZoomOut, btnZoomIn, imgChatArea, txtChatMessages,
				imgChatField, txtChatEntry);

		group.getChildren().add(zoomGroup);
		zoomGroup.getChildren().addAll(gameBoard);
		scrollPane.setContent(group);
	}

	private void setLayout() {
		pane.setPrefSize(sceneWidth, sceneHeight);
		scrollPane.setPrefSize(sceneWidth * 0.75, sceneHeight);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		gameBoard.setPrefSize(20020, 20020);

		imgRightBackground.setLayoutX(sceneWidth * 0.75);

		imgCardHolder.setLayoutX(sceneWidth * 0.7572);
		imgCardHolder.setLayoutY(sceneHeight * 0.0228);
		imgPreview.setLayoutX(sceneWidth * 0.7694);
		imgPreview.setLayoutY(sceneHeight * 0.0383);
		imgPreviewShadow.setLayoutX(sceneWidth * 0.7694);
		imgPreviewShadow.setLayoutY(sceneHeight * 0.0383);
		imgCardBack.setLayoutX(sceneWidth * 0.8788);
		imgCardBack.setLayoutY(sceneHeight * 0.0383);
		imgCardBackShadow.setLayoutX(sceneWidth * 0.8788);
		imgCardBackShadow.setLayoutY(sceneHeight * 0.0383);
		lblCardsRemaining.setPrefSize(CARDSIZE, CARDSIZE);
		lblCardsRemaining.setLayoutX(sceneWidth * 0.8788);
		lblCardsRemaining.setLayoutY(sceneHeight * 0.0383);

		imgPlayerList.setLayoutX(sceneWidth * 0.7590);
		imgPlayerList.setLayoutY(sceneHeight * 0.3317);
		playerTable.setPrefSize(sceneWidth * 0.2094, sceneHeight * 0.2694);
		playerTable.setLayoutX(sceneWidth * 0.7698);
		playerTable.setLayoutY(sceneHeight * 0.3533);

		btnLast.setMinSize(sceneWidth * 0.125, sceneHeight * 0.0622);
		btnLast.setMaxSize(sceneWidth * 0.125, sceneHeight * 0.0622);
		btnLast.setLayoutX(sceneWidth * 0.8681);
		btnLast.setLayoutY(sceneHeight * 0.9067);

		btnNext.setMinSize(sceneWidth * 0.125, sceneHeight * 0.0622);
		btnNext.setMaxSize(sceneWidth * 0.125, sceneHeight * 0.0622);
		btnNext.setLayoutX(sceneWidth * 0.7590);
		btnNext.setLayoutY(sceneHeight * 0.9067);

		btnZoomOut.setMinSize(sceneWidth * 0.0458, sceneHeight * 0.0367);
		btnZoomOut.setMaxSize(sceneWidth * 0.0458, sceneHeight * 0.0367);
		btnZoomOut.setLayoutX(sceneWidth * 0.0111);
		btnZoomOut.setLayoutY(sceneHeight * 0.0194);
		btnZoomIn.setMinSize(sceneWidth * 0.0458, sceneHeight * 0.0367);
		btnZoomIn.setMaxSize(sceneWidth * 0.0458, sceneHeight * 0.0367);
		btnZoomIn.setLayoutX(sceneWidth * 0.0621);
		btnZoomIn.setLayoutY(sceneHeight * 0.0194);

		imgChatArea.setLayoutX(sceneWidth * 0.1208);
		imgChatArea.setLayoutY(sceneHeight * 0.7589);
		txtChatMessages.setPrefSize(sceneWidth * 0.5083, sceneHeight * 0.1756);
		txtChatMessages.setLayoutX(sceneWidth * 0.1208);
		txtChatMessages.setLayoutY(sceneHeight * 0.7589);
		txtChatMessages.setStyle("-fx-text-fill: white;");
		imgChatField.setLayoutX(sceneWidth * 0.1208);
		imgChatField.setLayoutY(sceneHeight * 0.9439);
		txtChatEntry.setPrefSize(sceneWidth * 0.5083, sceneHeight * 0.0222);
		txtChatEntry.setLayoutX(sceneWidth * 0.1208);
		txtChatEntry.setLayoutY(sceneHeight * 0.9439);
		txtChatEntry.setStyle("-fx-text-fill: white;");

	}

	private double[] convertIndexPositionToPosition(int x, int y) {
		double[] position = new double[2];
		double xCoords = gameBoard.getWidth() / 2 - CARDSIZE / 2;
		double yCoords = gameBoard.getHeight() / 2 - CARDSIZE / 2;

		if (x <= 0) {
			while (x < 0) {
				xCoords -= CARDSIZE + GAP;
				x++;
			}
		} else {
			while (x > 0) {
				xCoords += CARDSIZE + GAP;
				x--;
			}
		}

		if (y <= 0) {
			while (y < 0) {
				yCoords -= CARDSIZE + GAP;
				y++;
			}
		} else {
			while (y > 0) {
				yCoords += CARDSIZE + GAP;
				y--;
			}
		}

		double bla = gameBoard.getWidth() / 2 - CARDSIZE / 2;
		double bla2 = gameBoard.getHeight() / 2 - CARDSIZE / 2;

		System.out.println("convert to position: " + xCoords + " " + bla + " " + yCoords + " " + bla2);
		position[0] = xCoords;
		position[1] = yCoords;
		return position;
	}

	public void placeMeeple(CardPane card, String color) {

		if (currentPlacement.getPlacement() != -1) {

			double meepleWidth;
			double meepleHeight;

			double offsetX;
			double offsetY;

			if (currentPlacement.getSpecialMeeple() != null && currentPlacement.getSpecialMeeple() == SpecialMeepleType.BIGMEEPLE) {
				meepleWidth = CARDSIZE * 0.34;
				meepleHeight = CARDSIZE * 0.35;

				offsetX = 2.5;
				offsetY = 2.8;
			} else {
				meepleWidth = CARDSIZE * 0.2359;
				meepleHeight = CARDSIZE * 0.24;

				offsetX = 1.9;
				offsetY = 2.2;
			}

			double xPos = 0;
			double yPos = 0;

			switch (currentPlacement.getPlacement()) {

				case 0:
					xPos = CARDSIZE / 3 - meepleWidth;
					yPos = 4;
					break;

				case 1:
					xPos = CARDSIZE / 2 - meepleWidth / 2;
					yPos = 4;
					break;

				case 2:
					xPos = CARDSIZE / 3 * 2;
					yPos = 4;
					break;

				case 3:
					xPos = 4;
					yPos = CARDSIZE / 3 * 2;
					break;

				case 4:
					xPos = 4;
					yPos = CARDSIZE / 2 - meepleHeight / 2;
					break;

				case 5:
					xPos = 4;
					yPos = CARDSIZE / 3 - meepleHeight;
					break;

				case 6:
					xPos = CARDSIZE / 3 * 2;
					yPos = CARDSIZE - meepleHeight - 2;
					break;

				case 7:
					xPos = CARDSIZE / 2 - meepleWidth / 2;
					yPos = CARDSIZE - meepleHeight - 2;
					break;

				case 8:
					xPos = CARDSIZE / 3 - meepleWidth;
					yPos = CARDSIZE - meepleHeight - 2;
					break;

				case 9:
					xPos = CARDSIZE - meepleWidth - 2;
					yPos = CARDSIZE / 3 - meepleHeight;
					break;

				case 10:
					xPos = CARDSIZE - meepleWidth - 2;
					yPos = CARDSIZE / 2 - meepleWidth / 2;
					break;

				case 11:
					xPos = CARDSIZE - meepleWidth - 2;
					yPos = CARDSIZE / 3 * 2;
					break;

				case 12:
					xPos = CARDSIZE / 2 - meepleWidth / 2;
					yPos = CARDSIZE / 2 - meepleHeight / 2;
					break;

				default:

					break;
			}

			ImageView meeple = null;
			ImageView meepleShadow = null;
			SpecialMeepleType type = currentPlacement.getSpecialMeeple();
			if (type != null) {
				if (type != null && type == SpecialMeepleType.BIGMEEPLE) {
					System.out.println("Big Meeple");
					meeple = new ImageView(new Image(MEEPLEIMAGE + color + ".png", CARDSIZE * 0.3, CARDSIZE * 0.3, false, false));
					meepleShadow = new ImageView(new Image(MEEPLESHADOW, meepleWidth, meepleHeight, false, false));
				} else if (type != null && type == SpecialMeepleType.BISHOP) {
					meeple = new ImageView(new Image(BISHOPIMAGE + color + "_bischof.png", CARDSIZE * 0.2069, CARDSIZE * 0.2069, false, false));
					meepleShadow = new ImageView(new Image(MEEPLESHADOW, meepleWidth, meepleHeight, false, false));
				}
			} else {
				meeple = new ImageView(new Image(MEEPLEIMAGE + color + ".png", CARDSIZE * 0.2069, CARDSIZE * 0.2069, false, false));
				meepleShadow = new ImageView(new Image("file:files/gui/meeple_shadow.png", meepleWidth, meepleHeight, false, false));
			}

			if (xPos < 4) {
				xPos = 4;
			}

			if (yPos < 4) {
				yPos = 4;
			}

			meeple.setLayoutX(xPos);
			meeple.setLayoutY(yPos);
			meepleShadow.setLayoutX(xPos - offsetX);
			meepleShadow.setLayoutY(yPos - offsetY);

			card.getChildren().remove(card.getShadow());
			card.setMeeplePlaced(card.getChildren().addAll(meeple, meepleShadow, card.getShadow()));
		}
	}

	public void moveMade(String cardID, Position positionIndex, int rotation, Placement placement, String moveMadeNick) {

		Image image = new Image(CARDIMAGE + cardID + ".png", CARDSIZE, CARDSIZE, false, false);
		CardPane newCard = new CardPane(image, rotation * 90.0, CARDSIZE);
		double[] position = convertIndexPositionToPosition(positionIndex.getX(), positionIndex.getY());
		newCard.setLayoutX(position[0]);
		newCard.setLayoutY(position[1]);
		gameBoard.getChildren().add(newCard);

		String color = "";
		for (Player p : controller.getControl().getGames().get(gameID).getPlayerList().values()) {
			if (p.getNick().equals(moveMadeNick)) {
				color = p.getColor().toUpperCase();
				break;
			}
		}

		currentPlacement = placement;
		placeMeeple(newCard, color);

		imgPreview.setRotate(0);
		cardPlaced = false;

	}

	/**
	 * Performs the 'tile drawn' message. Sets the label which says if it's your turn or not and disabled/enables
	 * buttons. Displays the card that is to be set next.
	 *
	 * @param nick          Nick of the player who's turn it is
	 * @param cardID        ID of the card image that was drawn
	 * @param rotation      Rotation of the card that is received in order to display the card correctly
	 * @param remaining
	 * @param remainingTime
	 */
	public void tileDrawn(String nick, String cardID, int rotation, int remaining, int remainingTime) {


		Image image = new Image(CARDIMAGE + cardID + ".png", CARDSIZE, CARDSIZE, false, true);
		imgPreview.setImage(image);

		currentCardPane = null;

		imgPreview.setRotate(90 * rotation);
		realRotation = rotation;
		lblCardsRemaining.setText(String.valueOf(remaining));

		// currentCard.autosize();
		//
		// if (timeline != null) {
		// timeline.stop();
		// }
		// timeSeconds = remainingTime;
		//
		// // update timerLabel
		// lblTimer.setText(String.valueOf(timeSeconds));
		// timeline = new Timeline();
		// timeline.setCycleCount(Timeline.INDEFINITE);
		// timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
		//
		// new EventHandler() {
		//
		// @Override
		// public void handle(Event event) {
		// timeSeconds--;
		// lblTimer.setText(String.valueOf(timeSeconds));
		// if (timeSeconds <= 0) {
		// timeline.stop();
		// }
		//
		// }
		// }));
		//
		// timeline.playFromStart();
	}

	private void performAllMoves() {
		moveMadeStorage.performAllMoves();
		disableStepNextButtons();
	}

	private void performMove() {
		if (!moveMadeStorage.isMoveMadeQueueEmpty()) {
			moveMadeStorage.performMove();
		} else {
			disableStepNextButtons();
		}
	}

	private void disableStepNextButtons() {
		btnNext.setDisable(true);
		btnLast.setDisable(true);
	}

	public void addText(String message) {
		txtChatMessages.appendText(message + System.getProperty("line.separator"));
	}

	private void move() {
		int gameID = this.gameID;
		Position position = this.currentCardPosition;

		int rotation = (int) imgPreview.getRotate() / 90;
		rotation = rotation - realRotation;
		rotation = rotation - (rotation / 4) * 4;

		int placementCommunication = controller.getControl().getGames().get(gameID).meeplePlaced(currentPlacement.getPlacement(), rotation);
		currentPlacement.setCommunicationPlacement(placementCommunication);

		controller.setCard(gameID, position, rotation);
		controller.delegateMove(gameID, position, rotation, currentPlacement);

		//			possibleMoves.getChildren().removeAll(possibleMoves.getChildren());
	}

	/**
	 * Delegates a gamechat message to the server.
	 */
	private void sendText() {
		String inputText = txtChatEntry.getText();
		controller.sendMessage(inputText, gameID);
		txtChatEntry.clear();
	}

	private void leaveGame(int gameID) {
		controller.leaveGame(gameID);
	}

	private void setLogoutHandler() {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg) {
				leaveGame(gameID);
				instance = null;
			}
		});
	}

	public void start(Stage arg0) throws Exception {

		if (!startHasBeenCalled) {

			scene = new Scene(pane);
			scene.getStylesheets().add(STYLESHEETOBSERVERVIEW);
			primaryStage = new Stage();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Carcassonne" + " Nick: " + currentPlayer.getNick());
			primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight() - 30);
			primaryStage.setResizable(false);
			setLogoutHandler();
			primaryStage.show();

			scrollPane.setHvalue(0.5);
			scrollPane.setVvalue(0.5);

			startHasBeenCalled = true;
		}
	}

	public void update(Observable o, Object arg) {
		final Collection<ClientGame> games = ((ClientControl) o).getGames().values();

		if (arg instanceof JsonType) {
			final JsonType updateMsg = (JsonType) arg;
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					switch (updateMsg) {

						case GAMEUPDATE:
							observablePlayerList.clear();
							for (ClientGame game : games) {
								if (game.getGameID() == gameID) {
									for (Player p : game.getPlayerList().values()) {
										observablePlayerList.add(new PlayerEntry(p.getNick(), p.getScore(), p.getMeeplesLeft(), p.getColor()));
									}
									if (game.getState() == GameStatus.ENDED) {
										gameHasEnded();
									}
								}
							}
							break;
						default:
							break;
					}
				}
			});
			Platform.setImplicitExit(true);
		}
	}

	private void gameHasEnded() {
		pane.getChildren().removeAll(btnLast, btnNext, imgCardHolder, lblCardsRemaining, imgCardBackShadow, imgCardBack, imgPreview, imgPreviewShadow);
	}

	public void deleteMeeple(Position position) {
		double[] coordinates = convertIndexPositionToPosition(position.getX(), position.getY());
		double xPos = coordinates[0];
		double yPos = coordinates[1];
		CardPane cardToRemoveMeepleFrom = null;
		for (javafx.scene.Node node : gameBoard.getChildren()) {
			if (node.getLayoutX() == xPos && node.getLayoutY() == yPos) {
				cardToRemoveMeepleFrom = (CardPane) node;
				break;
			}
		}

		if (cardToRemoveMeepleFrom.getChildren().size() > 2) {
			cardToRemoveMeepleFrom.getChildren().removeAll(cardToRemoveMeepleFrom.getChildren().get(2), cardToRemoveMeepleFrom.getChildren().get(1));
		}
	}
}


package client.view;

import static shared.Configuration.BISHOPIMAGE;
import static shared.Configuration.CARDIMAGE;
import static shared.Configuration.MEEPLEIMAGE;
import static shared.Configuration.MEEPLESHADOW;
import static shared.Configuration.SEPARATOR;
import static shared.Configuration.STYLESHEETGAMEVIEW;

import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import shared.enums.CapabilitiesType;
import shared.enums.GameStatus;
import shared.enums.JsonType;
import shared.enums.SpecialMeepleType;
import shared.model.Placement;
import shared.model.Player;
import shared.model.Position;
import client.controller.CarcassonneController;
import client.model.clientCommunication.ClientControl;
import client.model.game.ClientGame;

public class GameView extends Application implements Observer {

	private static GameView instance;
	private Scene scene;
	private Stage primaryStage;

	// distance between cards
	private static final int GAP = 5;
	private static double CARDSIZE;

	private Player currentPlayer;
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
	private Pane possibleMoves;
	private Set<Position> possibleMovesSet;

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
	private Button btnRotate;

	// player list
	private ImageView imgPlayerList;
	private ObservableList<PlayerEntry> observablePlayerList;
	private TableView<PlayerEntry> playerTable;

	// move
	private Button btnMove;

	// imgLeaves
	private ImageView imgLeaves;

	// zoom
	private Button btnZoomIn;
	private Button btnZoomOut;

	// help
	private Button btnHelp;

	// chat
	private ImageView imgChatArea;
	private TextArea txtChatMessages;
	private ImageView imgChatField;
	private TextField txtChatEntry;

	// scale values
	private Scale scaleUp;
	private Scale scaleDown;

	private boolean yourTurn;
	private boolean cardPlaced;

	private int realRotation;
	private int gameID;
	private boolean startHasBeenCalled;
	private Position currentCardPosition;
	private CardPane currentCardPane;

	private GameView(final int gameID, CarcassonneController controller,
			Player player, Set<String> playerList) {
		this.gameID = gameID;
		this.controller = controller;
		this.currentPlayer = player;
		this.currentPlacement = new Placement(-1);

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

	/**
	 * Is called when the first gameUpdate arrives and the the playerTable is set up.
	 * 
	 * @param playerList
	 */
	private void doInitialPlayerTableInitialisation(Set<String> playerList) {
		observablePlayerList.clear();
		for (Player p : controller.getControl().getGames().get(gameID)
				.getPlayerList().values()) {
			observablePlayerList.add(new PlayerEntry(p.getNick(), p.getScore(),
					p.getMeeplesLeft(), p.getColor()));
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
	public static synchronized GameView getInstance(int gameID,
			CarcassonneController controller, Player player,
			Set<String> playerList) {
		if (instance == null) {
			instance = new GameView(gameID, controller, player, playerList);
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
		possibleMoves = new Pane();

		imgLeftBackground = new ImageView(new Image("file:files" + SEPARATOR
				+ "gui" + SEPARATOR + "background_left.png", sceneWidth * 0.75,
				sceneHeight, false, false));
		imgRightBackground = new ImageView(new Image("file:files" + SEPARATOR
				+ "gui" + SEPARATOR + "background_right.png",
				sceneWidth * 0.25, sceneHeight, false, false));

		imgCardHolder = new ImageView(new Image("file:files" + SEPARATOR
				+ "gui" + SEPARATOR + "cardholder.png", sceneWidth * 0.2358,
				sceneHeight * 0.235, false, false));
		imgShadow = new Image("file:files" + SEPARATOR + "gui" + SEPARATOR
				+ "shadow.png", CARDSIZE, CARDSIZE, false, false);
		imgPreview = new ImageView();
		imgPreviewShadow = new ImageView(imgShadow);
		imgCardBack = new ImageView(new Image("file:files" + SEPARATOR + "gui"
				+ SEPARATOR + "backsideCard.png", sceneWidth * 0.1007,
				sceneHeight * 0.1611, false, false));
		imgCardBackShadow = new ImageView(imgShadow);
		lblCardsRemaining = new Label();
		lblCardsRemaining.setId("remaining");
		InnerShadow is = new InnerShadow();
		is.setOffsetX(2.0f);
		is.setOffsetY(2.0f);
		lblCardsRemaining.setEffect(is);
		btnRotate = new Button();
		btnRotate.setId("rotate-button");

		imgPlayerList = new ImageView(new Image("file:files" + SEPARATOR
				+ "gui" + SEPARATOR + "players.png", sceneWidth * 0.2319,
				sceneHeight * 0.3089, false, false));
		setupPlayerTable();

		btnMove = new Button();
		btnMove.setId("not-your-turn");

		imgLeaves = new ImageView(new Image("file:files" + SEPARATOR + "gui"
				+ SEPARATOR + "leaves.png", sceneWidth * 0.75, sceneHeight,
				false, false));

		btnZoomOut = new Button();
		btnZoomOut.setId("zoom-out");
		btnZoomIn = new Button();
		btnZoomIn.setId("zoom-in");

		btnHelp = new Button();
		btnHelp.setId("help");

		imgChatArea = new ImageView(new Image("file:files" + SEPARATOR + "gui"
				+ SEPARATOR + "chatArea.png", sceneWidth * 0.5083,
				sceneHeight * 0.1756, false, false));
		txtChatMessages = new TextArea();
		txtChatMessages.setEditable(false);
		txtChatMessages.setWrapText(true);
		imgChatField = new ImageView(new Image("file:files" + SEPARATOR + "gui"
				+ SEPARATOR + "chatField.png", sceneWidth * 0.5083,
				sceneHeight * 0.0222, false, false));
		txtChatEntry = new TextField();
	}

	private void setupPlayerTable() {

		observablePlayerList = FXCollections.observableArrayList();

		playerTable = new TableView<PlayerEntry>(observablePlayerList);

		TableColumn<PlayerEntry, String> player = new TableColumn<>("Player");
		player.setCellValueFactory(new PropertyValueFactory<PlayerEntry, String>(
				"player"));

		player.prefWidthProperty().bind(
				playerTable.widthProperty().multiply(0.41));

		TableColumn<PlayerEntry, String> score = new TableColumn<>("Score");
		score.setCellValueFactory(new PropertyValueFactory<PlayerEntry, String>(
				"score"));

		score.prefWidthProperty().bind(
				playerTable.widthProperty().multiply(0.19));

		TableColumn<PlayerEntry, String> meeples = new TableColumn<>("Meeples");
		meeples.setCellValueFactory(new PropertyValueFactory<PlayerEntry, String>(
				"meeples"));

		meeples.prefWidthProperty().bind(
				playerTable.widthProperty().multiply(0.19));

		TableColumn<PlayerEntry, String> color = new TableColumn<>("Color");
		color.setCellValueFactory(new PropertyValueFactory<PlayerEntry, String>(
				"color"));

		color.prefWidthProperty().bind(
				playerTable.widthProperty().multiply(0.20));

		playerTable.getColumns().setAll(player, score, meeples, color);

	}

	private void addWidgets() {
		pane.getChildren().addAll(imgLeftBackground, imgRightBackground,
				scrollPane, imgLeaves, imgCardHolder, imgPreview,
				imgPreviewShadow, imgCardBack, lblCardsRemaining,
				imgCardBackShadow, btnRotate, imgPlayerList, playerTable,
				btnMove, btnZoomOut, btnZoomIn, btnHelp, imgChatArea,
				txtChatMessages, imgChatField, txtChatEntry);

		group.getChildren().add(zoomGroup);
		zoomGroup.getChildren().addAll(possibleMoves, gameBoard);
		scrollPane.setContent(group);
	}

	private void setLayout() {
		pane.setPrefSize(sceneWidth, sceneHeight);
		scrollPane.setPrefSize(sceneWidth * 0.75, sceneHeight);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		gameBoard.setPrefSize(20020, 20020);
		possibleMoves.setPrefSize(20020, 20020);

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

		btnRotate.setMinSize(sceneWidth * 0.0563, sceneHeight * 0.0889);
		btnRotate.setMaxSize(sceneWidth * 0.0563, sceneHeight * 0.0889);
		btnRotate.setLayoutX(sceneWidth * 0.8469);
		btnRotate.setLayoutY(sceneHeight * 0.2115);

		imgPlayerList.setLayoutX(sceneWidth * 0.7590);
		imgPlayerList.setLayoutY(sceneHeight * 0.3317);
		playerTable.setPrefSize(sceneWidth * 0.2094, sceneHeight * 0.2694);
		playerTable.setLayoutX(sceneWidth * 0.7698);
		playerTable.setLayoutY(sceneHeight * 0.3533);

		btnMove.setMinSize(sceneWidth * 0.2316, sceneHeight * 0.0589);
		btnMove.setMaxSize(sceneWidth * 0.2316, sceneHeight * 0.0589);
		btnMove.setLayoutX(sceneWidth * 0.7590);
		btnMove.setLayoutY(sceneHeight * 0.9067);

		btnZoomOut.setMinSize(sceneWidth * 0.0458, sceneHeight * 0.0367);
		btnZoomOut.setMaxSize(sceneWidth * 0.0458, sceneHeight * 0.0367);
		btnZoomOut.setLayoutX(sceneWidth * 0.0111);
		btnZoomOut.setLayoutY(sceneHeight * 0.0194);
		btnZoomIn.setMinSize(sceneWidth * 0.0458, sceneHeight * 0.0367);
		btnZoomIn.setMaxSize(sceneWidth * 0.0458, sceneHeight * 0.0367);
		btnZoomIn.setLayoutX(sceneWidth * 0.0621);
		btnZoomIn.setLayoutY(sceneHeight * 0.0194);

		btnHelp.setMinSize(sceneWidth * 0.0240, sceneHeight * 0.0383);
		btnHelp.setMaxSize(sceneWidth * 0.0240, sceneHeight * 0.0383);
		btnHelp.setLayoutX(sceneWidth * 0.7146);
		btnHelp.setLayoutY(sceneHeight * 0.0194);

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

	private void setupInteraction() {
		gameBoard
				.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
					@Override
					public void handle(javafx.scene.input.MouseEvent click) {
						if (click.getButton() == MouseButton.PRIMARY) {
							javafx.scene.Node node =	 pick(gameBoard, click.getX(), click.getY());
							if (yourTurn && !cardPlaced && !node.isDisabled()) {
								double[] rasterCoordinates = convertClickToRasterCoordinates(
										click.getX(), click.getY());
								Position position = convertClickToPositionIndex(
										click.getX(), click.getY());
								currentCardPosition = position;
								currentCardPane = new CardPane(imgPreview
										.getImage(), imgPreview.getRotate(),
										CARDSIZE);
								setupCardClickListener(currentCardPane);
								currentCardPane
										.setLayoutX(rasterCoordinates[0]);
								currentCardPane
										.setLayoutY(rasterCoordinates[1]);
								gameBoard.getChildren().add(currentCardPane);
								cardPlaced = true;
								btnMove.setDisable(false);
								btnRotate.setDisable(true);
								currentPlacement = new Placement(-1);
							}
						} else if (click.getButton() == MouseButton.SECONDARY
								&& !currentCardPane.isMeeplePlaced()) {
							gameBoard.getChildren().remove(currentCardPane);
							cardPlaced = false;
							btnRotate.setDisable(false);
							btnMove.setDisable(true);
						}
					}
				});

		btnRotate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				imgPreview.setRotate(imgPreview.getRotate() + 90);
			}
		});

		btnMove.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				move();
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

		btnHelp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				HelpView help = new HelpView();
				help.start(new Stage());
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

	private Position convertClickToPositionIndex(double xClick, double yClick) {
		int x = 0;
		int y = 0;

		double xCoords = gameBoard.getWidth() / 2;
		if (xClick < xCoords) {
			xCoords -= CARDSIZE / 2;
			while (xClick < xCoords) {
				xCoords -= CARDSIZE + GAP;
				x--;
			}
		} else if (xClick > xCoords + (CARDSIZE / 2) + GAP) {
			xCoords += (CARDSIZE / 2) + GAP;
			x++;
			while (xClick > xCoords && !(xClick < xCoords + CARDSIZE + GAP)) {
				xCoords += CARDSIZE + GAP;
				x++;
			}
		}

		double yCoords = gameBoard.getHeight() / 2;
		if (yClick < yCoords) {
			yCoords -= CARDSIZE / 2;
			while (yClick < yCoords) {
				yCoords -= CARDSIZE + GAP;
				y--;
			}
		} else if (yClick > yCoords + (CARDSIZE / 2) + GAP) {
			yCoords += (CARDSIZE / 2) + GAP;
			y++;
			while (yClick > yCoords && !(yClick < yCoords + CARDSIZE + GAP)) {
				yCoords += CARDSIZE + GAP;
				y++;
			}
		}

		Position position = new Position(x, y);
		return position;
	}

	private double[] convertClickToRasterCoordinates(double x, double y) {
		double[] position = new double[2];

		double xCoords = gameBoard.getWidth() / 2;
		if (x < gameBoard.getWidth() / 2 + CARDSIZE / 2) {
			xCoords -= CARDSIZE / 2;
			while (x < xCoords) {
				xCoords -= CARDSIZE + GAP;
			}
		} else {
			xCoords += (CARDSIZE / 2) + GAP;
			while (x > xCoords && !(x < xCoords + CARDSIZE + GAP)) {
				xCoords += CARDSIZE + GAP;
			}
		}

		double yCoords = gameBoard.getHeight() / 2;
		if (y < gameBoard.getHeight() / 2 + CARDSIZE / 2) {
			yCoords -= CARDSIZE / 2;
			while (y < yCoords) {
				yCoords -= CARDSIZE + GAP;
			}
		} else {
			yCoords += (CARDSIZE / 2) + GAP;
			while (y > yCoords && !(y < yCoords + CARDSIZE + GAP)) {
				yCoords += CARDSIZE + GAP;
			}
		}

		position[0] = xCoords;
		position[1] = yCoords;

		return position;
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

		position[0] = xCoords;
		position[1] = yCoords;
		return position;
	}

	public void setupCardClickListener(final CardPane card) {
		card.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {

			@Override
			public void handle(javafx.scene.input.MouseEvent click) {

				if (click.getButton() == MouseButton.PRIMARY) {

					double yClick = click.getY();
					double xClick = click.getX();
					int placementInt = -1;
					if (!card.isMeeplePlaced()) {

						if (xClick <= CARDSIZE / 3 && yClick <= xClick) {
							placementInt = 0;

						} else if (xClick > CARDSIZE / 3
								&& xClick <= CARDSIZE / 3 * 2
								&& yClick <= CARDSIZE / 3) {
							placementInt = 1;

						} else if (xClick > CARDSIZE / 3 * 2
								&& yClick <= CARDSIZE / 3
										- (xClick - CARDSIZE / 3 * 2)) {
							placementInt = 2;

						} else if (xClick <= CARDSIZE / 3
								- (yClick - CARDSIZE / 3 * 2)
								&& yClick > CARDSIZE / 3 * 2) {
							placementInt = 3;

						} else if (xClick <= CARDSIZE / 3
								&& yClick > CARDSIZE / 3
								&& yClick <= CARDSIZE / 3 * 2) {
							placementInt = 4;

						} else if (xClick <= yClick && yClick <= CARDSIZE / 3) {
							placementInt = 5;

						} else if (xClick > CARDSIZE / 3 * 2 && yClick > xClick) {
							placementInt = 6;

						} else if (xClick > CARDSIZE / 3
								&& xClick <= CARDSIZE / 3 * 2
								&& yClick > CARDSIZE / 3 * 2) {
							placementInt = 7;

						} else if (xClick > CARDSIZE - yClick
								&& xClick <= CARDSIZE / 3) {
							placementInt = 8;

						} else if (xClick > CARDSIZE / 3 * 2
								&& yClick > CARDSIZE / 3
										- (xClick - CARDSIZE / 3 * 2)
								&& yClick <= CARDSIZE / 3) {
							placementInt = 9;

						} else if (xClick > CARDSIZE / 3 * 2
								&& yClick > CARDSIZE / 3
								&& yClick <= CARDSIZE / 3 * 2) {
							placementInt = 10;

						} else if (xClick > CARDSIZE / 3 * 2
								&& yClick > CARDSIZE / 3 * 2
								&& yClick <= xClick) {
							placementInt = 11;

						} else if (xClick > CARDSIZE / 3
								&& xClick <= CARDSIZE / 3 * 2
								&& yClick > CARDSIZE / 3
								&& yClick <= CARDSIZE / 3 * 2) {
							placementInt = 12;

						}

						card.setMeeplePlaced(true);
						String color = currentPlayer.getColor().toUpperCase();

						currentPlacement.setPlacement(placementInt);
						// Extensions of the game
						Set<CapabilitiesType> extensions = controller
								.getControl().getGames().get(gameID)
								.getExtensions();

						// If Shift + click set Big Meeple if the extension is active
						// If Alt + click set Bishop if the extension is active
						if (click.isShiftDown()
								&& !click.isAltDown()
								&& extensions != null
								&& extensions
										.contains(CapabilitiesType.BIGMEEPLE)) {
							currentPlacement
									.setSpecialMeeple(SpecialMeepleType.BIGMEEPLE);
						} else if (extensions != null && click.isAltDown()
								&& click.isShiftDown()
								&& extensions.contains(CapabilitiesType.BISHOP)) {
							currentPlacement
									.setSpecialMeeple(SpecialMeepleType.BISHOP);
						}

						placeMeeple(card, color);
					}
				} else if (click.getButton() == MouseButton.SECONDARY) {

					if (card.isMeeplePlaced()) {
						card.getChildren().remove(card.getChildren().get(2));
						card.getChildren().remove(card.getChildren().get(1));
						card.setMeeplePlaced(false);
						currentPlacement.setPlacement(-1);
						currentPlacement.setSpecialMeeple(null);
						click.consume();
					}
				}
			}
		});
	}

	public void placeMeeple(CardPane card, String color) {

		if (currentPlacement.getPlacement() != -1) {

			double meepleWidth;
			double meepleHeight;

			double offsetX;
			double offsetY;

			if (currentPlacement.getSpecialMeeple() != null
					&& currentPlacement.getSpecialMeeple() == SpecialMeepleType.BIGMEEPLE) {
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
					meeple = new ImageView(new Image(MEEPLEIMAGE + color
							+ ".png", CARDSIZE * 0.3, CARDSIZE * 0.3, false,
							false));
					meepleShadow = new ImageView(new Image(MEEPLESHADOW,
							meepleWidth, meepleHeight, false, false));
				} else if (type != null && type == SpecialMeepleType.BISHOP) {
					meeple = new ImageView(new Image(BISHOPIMAGE + color
							+ "_bischof.png", CARDSIZE * 0.2069,
							CARDSIZE * 0.2069, false, false));
					meepleShadow = new ImageView(new Image(MEEPLESHADOW,
							meepleWidth, meepleHeight, false, false));
				}
			} else {
				meeple = new ImageView(new Image(MEEPLEIMAGE + color + ".png",
						CARDSIZE * 0.2069, CARDSIZE * 0.2069, false, false));
				meepleShadow = new ImageView(new Image("file:files" + SEPARATOR
						+ "gui" + SEPARATOR + "meeple_shadow.png", meepleWidth,
						meepleHeight, false, false));
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
			card.setMeeplePlaced(card.getChildren().addAll(meeple,
					meepleShadow, card.getShadow()));
		}
	}

	public void moveMade(String cardID, Position positionIndex, int rotation,
			Placement placement, String moveMadeNick) {

		Image image = new Image(CARDIMAGE + cardID + ".png", CARDSIZE,
				CARDSIZE, false, false);
		CardPane newCard = new CardPane(image, rotation * 90.0, CARDSIZE);
		double[] position = convertIndexPositionToPosition(
				positionIndex.getX(), positionIndex.getY());
		newCard.setLayoutX(position[0]);
		newCard.setLayoutY(position[1]);
		gameBoard.getChildren().add(newCard);

		String color = "";
		for (Player p : controller.getControl().getGames().get(gameID)
				.getPlayerList().values()) {
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

	public void moveFailed(String reason) {
		double xPos = currentCardPane.getLayoutX();
		double yPos = currentCardPane.getLayoutY();
		CardPane cardToRemove = null;
		for (javafx.scene.Node node : gameBoard.getChildren()) {
			if (node.getLayoutX() == xPos && node.getLayoutY() == yPos) {
				cardToRemove = (CardPane) node;
				break;
			}
		}
		gameBoard.getChildren().remove(cardToRemove);
		cardPlaced = false;
		btnRotate.setDisable(false);

		for (Position position : possibleMovesSet) {
			double[] coords = convertIndexPositionToPosition(position.getX(),
					position.getY());
			ImageView possibleMove = new ImageView(new Image(
					"file:files/gui/possibleMove.png", CARDSIZE, CARDSIZE,
					false, false));
			possibleMove.setLayoutX(coords[0]);
			possibleMove.setLayoutY(coords[1]);
			possibleMoves.getChildren().add(possibleMove);
		}
	}

	/**
	 * Performs the 'tile drawn' message. Sets the label which says if it's your turn or not and disabled/enables
	 * buttons. Displays the card that is to be set next.
	 * 
	 * @param nick
	 *            Nick of the player who's turn it is
	 * @param cardID
	 *            ID of the card image that was drawn
	 * @param rotation
	 *            Rotation of the card that is received in order to display the card correctly
	 * @param remaining
	 * @param remainingTime
	 */
	public void tileDrawn(String nick, String cardID, int rotation,
			int remaining, int remainingTime, Set<Position> possiblePlacement) {

		if (nick.equals(currentPlayer.getNick())) {
			yourTurn = true;
			btnMove.setId("your-turn");
			btnMove.setDisable(true);
			btnRotate.setDisable(false);

			for (Position position : possiblePlacement) {
				double[] coords = convertIndexPositionToPosition(
						position.getX(), position.getY());
				ImageView possibleMove = new ImageView(new Image(
						"file:files/gui/possibleMove.png", CARDSIZE, CARDSIZE,
						false, false));
				possibleMove.setLayoutX(coords[0]);
				possibleMove.setLayoutY(coords[1]);
				possibleMoves.getChildren().add(possibleMove);
			}
			possibleMovesSet = possiblePlacement;

		} else {
			yourTurn = false;
			btnMove.setId("not-your-turn");
			btnMove.setDisable(true);
			btnRotate.setDisable(true);
		}

		Image image = new Image(CARDIMAGE + cardID + ".png", CARDSIZE,
				CARDSIZE, false, true);
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

	public void addText(String message) {
		txtChatMessages.appendText(message
				+ System.getProperty("line.separator"));
	}

	private void move() {
		int gameID = this.gameID;
		Position position = this.currentCardPosition;

		int rotation = (int) imgPreview.getRotate() / 90;
		rotation = rotation - realRotation;
		rotation = rotation - (rotation / 4) * 4;

		int placementCommunication = controller.getControl().getGames()
				.get(gameID)
				.meeplePlaced(currentPlacement.getPlacement(), rotation);
		currentPlacement.setCommunicationPlacement(placementCommunication);

		controller.setCard(gameID, position, rotation);
		controller.delegateMove(gameID, position, rotation, currentPlacement);

		possibleMoves.getChildren().removeAll(possibleMoves.getChildren());
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
			scene.getStylesheets().add(STYLESHEETGAMEVIEW);
			primaryStage = new Stage();
			primaryStage.setScene(scene);
			primaryStage.setTitle("Carcassonne" + " Nick: "
					+ currentPlayer.getNick());
			primaryStage
					.setHeight(Screen.getPrimary().getBounds().getHeight() - 30);
			primaryStage.setResizable(false);
			setLogoutHandler();
			primaryStage.show();

			scrollPane.setHvalue(0.5);
			scrollPane.setVvalue(0.5);

			startHasBeenCalled = true;
		}
	}

	public void update(final Observable o, Object arg) {
		final Collection<ClientGame> games = ((ClientControl) o).getGames()
				.values();

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
									observablePlayerList.add(new PlayerEntry(p
											.getNick(), p.getScore(), p
											.getMeeplesLeft(), p.getColor()));
								}
								if (game.getState() == GameStatus.ENDED) {
									gameHasEnded();
								}
							}
						}

						ClientControl control = (ClientControl) o;
						for (Position position : control.getDeletedMeeples()) {
							deleteMeeple(position);
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
		pane.getChildren().removeAll(btnRotate, btnMove, imgCardHolder,
				lblCardsRemaining, imgCardBackShadow, imgCardBack, imgPreview,
				imgPreviewShadow);
	}

	public void deleteMeeple(Position position) {
		double[] coordinates = convertIndexPositionToPosition(position.getX(),
				position.getY());
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
			cardToRemoveMeepleFrom.getChildren().removeAll(
					cardToRemoveMeepleFrom.getChildren().get(2),
					cardToRemoveMeepleFrom.getChildren().get(1));
		}
	}

	public static Node pick(Node node, double sceneX, double sceneY) {
		Point2D p = node.sceneToLocal(sceneX, sceneY, true /* rootScene */);

		// check if the given node has the point inside it, or else we drop out
		if (!node.contains(p)) return null;

		// at this point we know that _at least_ the given node is a valid
		// answer to the given point, so we will return that if we don't find
		// a better child option
		if (node instanceof Parent) {
			// we iterate through all children in reverse order, and stop when we find a match.
			// We do this as we know the elements at the end of the list have a higher
			// z-order, and are therefore the better match, compared to children that
			// might also intersect (but that would be underneath the element).
			Node bestMatchingChild = null;
			List<Node> children = ((Parent)node).getChildrenUnmodifiable();
			for (int i = children.size() - 1; i >= 0; i--) {
				Node child = children.get(i);
				p = child.sceneToLocal(sceneX, sceneY, true /* rootScene */);
				if (child.isVisible() && !child.isMouseTransparent() && child.contains(p)) {
					bestMatchingChild = child;
					break;
				}
			}

			if (bestMatchingChild != null) {
				return pick(bestMatchingChild, sceneX, sceneY);
			}
		}

		return node;
	}
}

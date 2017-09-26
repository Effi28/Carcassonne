package client.view;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Displays the tab for the private chat
 * 
 * @version 19.11.2013
 * 
 */
public final class PrivateTab extends Tab {
	// TODO
	// scene.getStylesheets().add(shared.Configuration.STYLESHEETJOINGAMEVIEW); an
	// die richtige Stelle

	public static Logger log = LogManager.getLogger("ERROR");
	private GridPane gridPrivateChat;

	private TextField txtField;
	private TextArea chatArea;
	private HBox hBoxChat;
	private ChatLobby chatLobby;
	private final String name;
	private boolean blink;
	private Thread thread;

	public PrivateTab(String name, ChatLobby chatLobby, boolean blink) {
		super(name);
		this.blink = blink;
		this.name = name;
		createWidgets();
		setLayout();
		addWidgets();
		setupInteraction();
		this.chatLobby = chatLobby;
		startBlinkThread();

	}

	private void startBlinkThread() {
		thread = new Thread() {
			public void run() {
				try {

					boolean coloured = false;

					while (!Thread.interrupted()) {
						Thread.sleep(700);
						if (blink && !Thread.interrupted()) {
							if (coloured) {
								setStyle("");
							} else {
								setStyle("-fx-background-color:#808080");
							}
							coloured = !coloured;
						} else {

						}
					}

				} catch (InterruptedException v) {
					log.error(v.getMessage());
				}
			}
		};
		thread.start();
	}

	private void setLayout() {
		chatArea.setId("chatArea");
		txtField.setId("insertLinePrivateChat");
		gridPrivateChat.setId("gridPrivateChat");
		chatArea.setPrefSize(830, 450);
		hBoxChat.setPrefSize(830, 25);
		txtField.setPrefWidth(830);

	}

	private void createWidgets() {
		gridPrivateChat = new GridPane();
		hBoxChat = new HBox();
		chatArea = new TextArea();
		txtField = new TextField();
		chatArea.setEditable(false);
		chatArea.setWrapText(true);
	}

	private void addWidgets() {
		gridPrivateChat.setHgap(10);
		gridPrivateChat.setVgap(10);
		gridPrivateChat.setPadding(new Insets(10, 10, 10, 10));
		gridPrivateChat.add(chatArea, 0, 1);
		gridPrivateChat.add(hBoxChat, 0, 2);
		hBoxChat.getChildren().addAll(txtField);
		this.setContent(gridPrivateChat);
		;

	}

	public void addText(String msg) {
		chatArea.appendText(msg + "\n");
	}

	private void setupInteraction() {

		// EventHandler for the txtField
		txtField.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				// Sending messages only possible if txtField =! null
				String chattext = txtField.getText();
				chattext = chattext.trim();
				if (chattext != null && !chattext.isEmpty()) {
					if (e.getCode() == KeyCode.ENTER) {

						sendText();

					}
				}
			}
		});

		// Insert Text into ChatWindow with Enter
		txtField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent e) {
				if (e.getCode() == KeyCode.ENTER) {
					sendText();
				}
			}
		});

		gridPrivateChat.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO
			}
		});
	}

	private void sendText() {
		String inputText = txtField.getText();
		chatLobby.getController().sendMessage(inputText, this.name);
		txtField.clear();
	}

	/*
	 * GETTERS AND SETTER BELOW
	 */

	public ChatLobby getChatLobby() {
		if (chatLobby == null) {
			log.error("CHATLOBBY NULL");
			throw new NullPointerException("CHATLOBBY NULL");
		}
		return chatLobby;
	}

	public String getName() {
		return name;
	}

	public Thread getThread() {
		return thread;
	}
}
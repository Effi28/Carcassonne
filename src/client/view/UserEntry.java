package client.view;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * This is a helper class for the table shown in the first tab of the ChatLobby
 * that shows the players
 */
public final class UserEntry extends Button {

	private StringProperty name;
	private final ChatLobby chatlobby;

	public UserEntry(final String name, ChatLobby chatLobby) {
		setName(name);
		this.chatlobby = chatLobby;
		this.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				ArrayList<PrivateTab> openTabs = chatlobby.getTabSet();
				Iterator<PrivateTab> itr = openTabs.iterator();
				boolean openChat = false;
				while (itr.hasNext()) {
					if (itr.next().getName() == name.toString()) {
						openChat = true;
					}
				}
				if (openChat == false) {
					chatlobby.createNewPrivateChatTab(name.toString());
				}
			}
		});
	}
	
	/*
	 * Getter and Setter below
	 */

	private void setName(String value) {
		nameProperty().set(value);
	}

	public String getName() {
		return nameProperty().get();
	}

	private StringProperty nameProperty() {
		if (name == null)
			name = new SimpleStringProperty(this, "name");
		return name;
	}
}
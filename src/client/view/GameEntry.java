package client.view;

import shared.Configuration;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This is a helper class for the table shown in the second tab of the ChatLobby
 */
public final class GameEntry {

	private StringProperty name;
	private StringProperty host;
	private StringProperty players;
	private final int gameID;
	private StringProperty extensions;

	public GameEntry(String name, String host, int players, int gameID,
			String extensions) {
		setName(name);
		setHost(host);
		this.gameID = gameID;
		setPlayers(Integer.toString(players) + "/" + Configuration.MAXUSER);
		setExtensions(extensions);

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

	private void setHost(String value) {
		hostProperty().set(value);
	}

	public String getHost() {
		return hostProperty().get();
	}

	private StringProperty hostProperty() {
		if (host == null)
			host = new SimpleStringProperty(this, "host");
		return host;
	}

	private void setPlayers(String value) {
		playersProperty().set(value);
	}

	public String getPlayers() {
		return playersProperty().get();
	}

	private StringProperty playersProperty() {
		if (players == null)
			players = new SimpleStringProperty(this, "players");
		return players;
	}

	public int getGameID() {
		return this.gameID;
	}

	public String getExtensions() {
		return playersProperty().get();
	}

	private void setExtensions(String value) {
		extensionsProperty().set(value);
	}

	public StringProperty extensionsProperty() {
		if (extensions == null)
			extensions = new SimpleStringProperty(this, "extensions");
		return extensions;
	}
}
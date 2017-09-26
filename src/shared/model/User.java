package shared.model;

import java.io.Serializable;

/**
 * Represents one user which is not interacting in any game.
 * <p>
 * It manages the nickname of each user.
 * Serves as a superclass for player and spectator.
 * 
 * @version 09.12.2013
 * 
 */
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Name of the user.
	 */
	private String nick;

	/**
	 * Constructor is called when a new client connects and a new ClientHandler is created.
	 * 
	 * @param nick name of the user
	 */
	public User(String nick) {
		this.nick = nick;
	}
	
	/*
	 * GETTERS AND SETTERS BELOW
	 */

	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick){
		this.nick = nick;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nick == null) ? 0 : nick.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (nick == null) {
			if (other.nick != null)
				return false;
		} else if (!nick.equals(other.nick))
			return false;
		return true;
	}
}

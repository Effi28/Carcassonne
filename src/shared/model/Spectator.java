package shared.model;

/**
 * This class represents one Spectator.
 * <p>
 * It manages the nickname of each Spectator.
 * 
 * @version 07.12.2013
 * 
 */
public final class Spectator extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constructor that sets the nick of the spectator
	 * @param nick
	 */
	public Spectator(String nick) {
		super(nick);
	}
	
	/**
	 * get-method for the nick
	 */
	public String getNick(){
		return super.getNick();
	}
	
}

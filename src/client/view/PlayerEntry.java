package client.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Helper class for displaying the player in the view.
 * 
 * @version 17.01.2014
 * 
 */
public final class PlayerEntry {

	private StringProperty player;
	private StringProperty score;
	private StringProperty meeples;
	private Rectangle color;

	public PlayerEntry(String player, int score, int meeples, String color) {
		setPlayer(player);
		setScore(Integer.toString(score));
		setMeeples(Integer.toString(meeples));
		this.color = new Rectangle(18, 18);
		this.color.setFill(Color.web(color));
	}

	private void setPlayer(String value) {
		playerProperty().set(value);
	}

	public String getPlayer() {
		return playerProperty().get();
	}

	private StringProperty playerProperty() {
		if (player == null)
			player = new SimpleStringProperty(this, "player");
		return player;
	}

	public void setScore(String value) {
		scoreProperty().set(value);
	}

	public String getScore() {
		return scoreProperty().get();
	}

	private StringProperty scoreProperty() {
		if (score == null)
			score = new SimpleStringProperty(this, "score");
		return score;
	}

	private StringProperty meepleProperty() {
		if (meeples == null)
			meeples = new SimpleStringProperty(this, "meeples");
		return meeples;
	}

	public String getMeeples() {
		return meepleProperty().get();
	}

	public void setMeeples(String value) {
		meepleProperty().set(value);
	}

	public Rectangle getColor(){
        return this.color;
    }

    public void setBtn(String color){
    	this.color.setFill(Color.web(color));
    }
}

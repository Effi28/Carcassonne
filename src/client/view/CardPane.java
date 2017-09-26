package client.view;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public final class CardPane extends Pane {

	private ImageView shadow;
	private boolean meeplePlaced;

	public CardPane(Image cardImage, double rotation, double cardSize) {
		ImageView cardView = new ImageView(cardImage);
		cardView.setRotate(rotation);
		shadow = new ImageView(new Image("file:files/gui/shadow.png", cardSize, cardSize, false, false));
		getChildren().addAll(cardView, shadow);
	}

	public ImageView getShadow() {
		return shadow;
	}

	public void setShadow(ImageView shadow) {
		this.shadow = shadow;
	}

	public boolean isMeeplePlaced() {
		return meeplePlaced;
	}

	public void setMeeplePlaced(boolean placed) {
		meeplePlaced = placed;
	}
}

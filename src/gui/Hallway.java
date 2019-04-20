package gui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Hallway extends StackPane {

	private Rectangle rect;

	private String occupantPlayer;
	private Circle occupantCircle;
	
	public Hallway(int x, int y, int width, int height) {
		super();
		rect = new Rectangle(width, height);
		rect.setFill(Color.WHITESMOKE);
		rect.setStroke(Color.BLACK);

		getChildren().add(rect);

		setLayoutX(x);
		setLayoutY(y);
	}

	public void addOccupant(String occupyingPlayer, Color occupyingPlayerColor) {
		this.occupantPlayer = occupyingPlayer;
		occupantCircle = new Circle(7);
		occupantCircle.setFill(occupyingPlayerColor);
		occupantCircle.setStroke(Color.BLACK);

		getChildren().add(occupantCircle);
	}

	public void removeOccupant() {
		getChildren().remove(occupantCircle);

		this.occupantPlayer = null;
		this.occupantCircle = null;
	}

	public boolean isOccupied() {
		return occupantPlayer != null;
	}
}

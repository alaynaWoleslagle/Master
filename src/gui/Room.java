package gui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

public class Room extends StackPane {

	private Rectangle rect;
	private Text text;

	private Map<String, Occupant> occupantMap = new HashMap<>();

	private String name;
	private boolean[] occupiedSpaces = new boolean[6];

	private static final int[] X_OFFSETS = {-30, 0, 30, -30, 0, 30};
	private static final int[] Y_OFFSETS = {-30, -30, -30, 30, 30, 30};

	private boolean isHighlighted = false;
	private Color defaultColor = Color.WHITE;
	private Color highlightColor = Color.GOLD;
	
	public Room(int x, int y, String name) {
		super();
		rect = new Rectangle(100, 100);
		rect.setFill(defaultColor);
		rect.setStroke(Color.BLACK);
		text = new Text(name);
		this.name = name;

		getChildren().addAll(rect, text);

		setLayoutX(x);
		setLayoutY(y);
	}

	public void addOccupant(String occupyingPlayer, Color occupyingPlayerColor) {
		if (!occupantMap.containsKey(occupyingPlayer)) {
			Circle circle = new Circle(7);
			circle.setFill(occupyingPlayerColor);
			circle.setStroke(Color.BLACK);
			int occupyingSpace = 0;
			for (int i = 0; i < occupiedSpaces.length; i++) {
				if (!occupiedSpaces[i]) {
					occupiedSpaces[i] = true;
					occupyingSpace = i;
					break;
				}
			}
			circle.setTranslateX(X_OFFSETS[occupyingSpace]);
			circle.setTranslateY(Y_OFFSETS[occupyingSpace]);

			occupantMap.put(occupyingPlayer, new Occupant(occupyingPlayer, circle, occupyingSpace));

			getChildren().add(circle);
		}
	}

	public void removeOccupant(String occupyingPlayer) {
		if (occupantMap.containsKey(occupyingPlayer)) {
			Occupant occupant = occupantMap.get(occupyingPlayer);
			getChildren().remove(occupant.circle);
			occupiedSpaces[occupant.occupyingSpace] = false;
			occupantMap.remove(occupyingPlayer);
		}
	}
	
	public void setName(String roomName) {
		this.name = roomName;
	}
	public String getName() {
		return name;
	}

	public void toggleHighlight() {
		if (!isHighlighted) {
			isHighlighted = true;
			rect.setFill(highlightColor);
		}
		else {
			isHighlighted = false;
			rect.setFill(defaultColor);
		}
	}

	private class Occupant {
		private String player;
		private Circle circle;
		private int occupyingSpace;

		private Occupant(String player, Circle circle, int occupyingSpace) {
			this.player = player;
			this.circle = circle;
			this.occupyingSpace = occupyingSpace;
		}
	}

}

package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Hallway extends Rectangle {
	
	Rectangle hallway;
	String startRoom;
	String endRoom;
	
	public Hallway(int x, int y, String type) {
		if (type == "horizontal")
			hallway = new Rectangle(x, y + 35, 200, 30);
		else
			hallway = new Rectangle(x + 35, y, 30, 200);
		
		hallway.setFill(Color.WHITESMOKE);
        hallway.setStroke(Color.BLACK);
		
	}
	
	public Rectangle getHallway() {
		return hallway;
	}

}

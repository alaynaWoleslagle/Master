package gui;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Room extends Rectangle {
	
	static Rectangle room;
	static String roomName;
	
	public Room(int x, int y, String name) {
		room = new Rectangle(x, y, 100, 100);
		room.setFill(Color.WHITE);
        room.setStroke(Color.BLACK);
		roomName = name;
	}
	
	public Rectangle getRoom()
	{
		return room;
	}
	
	public static void setRoomName(String name) {
		roomName = name;
	}
	
	public String getName() {
		return roomName;
	}
	
	public void addHallway(Hallway hallway)
	{
		ArrayList<String> hallwayList = new ArrayList<>();
	}
	

}

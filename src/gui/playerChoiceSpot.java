package gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class playerChoiceSpot extends Rectangle {
	
	String playerName;
	
	public playerChoiceSpot(int x, int y)
	{
		super (x, y, 100, 100);
		
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public void setColor(Color c)
	{
		this.setFill(c);
	}

}

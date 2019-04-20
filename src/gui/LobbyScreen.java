package gui;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class LobbyScreen {
	
    private Button readyButton;
    private int assignedPlayerIndex;
    private Group root = new Group();
    
    playerChoiceSpot[] playerChoiceSpots = new playerChoiceSpot[6];
    Rectangle[] characterChoices = new Rectangle[6];
    ArrayList<String> allPlayerNames;
    
    public LobbyScreen(int assignedPlayerIndex, String[] allPlayerNames) {
    		this.assignedPlayerIndex = assignedPlayerIndex;
    		this.allPlayerNames = new ArrayList<>(Arrays.asList(allPlayerNames));
    }
	
	public Scene createLobby()
	{
		// set up player choice spots
		Group playerGroup = new Group();
		HBox playerHbox = new HBox(50);
		int x = 100;
		int y = 480;
		for (int i = 0; i < playerChoiceSpots.length; ++i)
		{
			playerChoiceSpots[i] = new playerChoiceSpot(x,y);
			playerChoiceSpots[i].setFill(Color.GRAY);
			playerChoiceSpots[i].setStroke(Color.BLACK);
			playerHbox.getChildren().add(playerChoiceSpots[i]);
		}
		playerGroup.getChildren().add(playerHbox);
		playerGroup.setTranslateY(500);
		
		// set up character choices
		Group characterGroup = new Group();
		HBox characterHbox = new HBox(10);
		Color[] characterColors = {Color.DARKRED, Color.YELLOW, Color.WHITE, Color.GREEN, Color.CADETBLUE, Color.PLUM};
		for (int j = 0; j < characterChoices.length; j++)
		{
			characterChoices[j] = new Rectangle(50, 100, 150, 150);
			characterChoices[j].setFill(characterColors[j]);
			characterChoices[j].setStroke(Color.BLACK);
			characterHbox.getChildren().add(characterChoices[j]);
		}
		characterGroup.getChildren().add(characterHbox);
		
		// When player clicks color, update their choice box
		playerChoiceSpot currentPlayerSpot = playerChoiceSpots[assignedPlayerIndex];
		for (Rectangle character : characterChoices)
		{
			character.setOnMouseClicked(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent t) {
	            		Color charColor = (Color) character.getFill();
	               currentPlayerSpot.setColor(charColor);
	            }
	        });
		}
		
		// Ready Button
		readyButton = new Button("READY");
		readyButton.setLayoutY(currentPlayerSpot.getLayoutY() + 150);
		readyButton.setLayoutX(currentPlayerSpot.getLayoutX());
		playerGroup.getChildren().add(readyButton);
		readyButton.setTranslateX(assignedPlayerIndex * 150);
		
		EventHandler<ActionEvent> readyClicked = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            		// TODO
            }
        };
        
        readyButton.setOnAction(readyClicked);
		
        // Player Names
        HBox playerNamesHbox = new HBox(120);
        Group playerNamesGroup = new Group();
		for (int i = 0; i < allPlayerNames.size(); ++i)
		{
			playerChoiceSpot currentSpot = playerChoiceSpots[i];
			Text playerName = new Text(allPlayerNames.get(i));
			playerName.setLayoutY(currentSpot.getLayoutY() - 50);
			playerNamesHbox.getChildren().add(playerName);
		}
		playerNamesGroup.getChildren().add(playerNamesHbox);
		playerNamesGroup.setTranslateY(450);
		
		root.getChildren().addAll(playerGroup, characterGroup, playerNamesGroup);
		return new Scene(root, 1000, 800);
	}
	
	public void disableReady()
	{
		readyButton.setDisable(true);
	}
	
	public void enableReady()
	{
		readyButton.setDisable(false);
	}
	
	public void addPlayer(String playerName)
	{
		allPlayerNames.add(playerName);
		
        HBox playerNamesHbox = new HBox(120);
        Group playerNamesGroup = new Group();
		for (int i = 0; i < allPlayerNames.size(); ++i)
		{
			playerChoiceSpot currentSpot = playerChoiceSpots[i];
			Text playerNameText = new Text(allPlayerNames.get(i));
			playerNameText.setLayoutY(currentSpot.getLayoutY() - 50);
			playerNamesHbox.getChildren().add(playerNameText);
		}
		playerNamesGroup.getChildren().add(playerNamesHbox);
		playerNamesGroup.setTranslateY(450);
		root.getChildren().add(playerNamesGroup);
	}

}

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
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import logic.GameProcessor;
import utils.Player;

public class LobbyScreen 
{
	private ArrayList<Player> players = new ArrayList<Player>();
    private Button readyButton;
    private int assignedPlayerIndex;
    private Group root = new Group();

	private Color[] colors = {Color.DARKRED, Color.YELLOW, Color.WHITE, Color.GREEN, Color.CADETBLUE, Color.PLUM};
    
    /** StackPanes Array */
    StackPane[] stackPanes = new StackPane[6];

    /** PlayerChoiceSpot Array */
    PlayerChoiceSpot[] playerChoiceSpots = new PlayerChoiceSpot[6];
	Text[] playerNameTexts = new Text[6];
    
    /** Rectangle Array */
    Rectangle[] characterChoices = new Rectangle[6];
    
    ArrayList<String> allPlayerNames;
    
	/**
	* LobbyScreen Test Constructor.
	* For testing purposes 
	*/
    public LobbyScreen(int assignedPlayerIndex, String[] allPlayerNames) 
    {
		this.allPlayerNames = new ArrayList<>(Arrays.asList(allPlayerNames));
    }
    
    /**
     * LobbyScreen Constructor.
     * @param assignedPlayerIndex
     * @param name
     */
    public LobbyScreen(int assignedPlayerIndex, String name)
    {
		this.assignedPlayerIndex = assignedPlayerIndex;
    	initializeArray();
    	this.allPlayerNames.set(assignedPlayerIndex, name);
    }
    
    /**
     * Initialize Player names Array.
     * Every entry in 'allPlayerNames' ArrayList should contain empty string
     * This is done because ArrayLists don't allow for entry at specific indexes if there are null indexes.
     */
    private void initializeArray()
    {
    	allPlayerNames = new ArrayList<String>();
    	for(int i = 0; i < 6; i++)
    	{
    		allPlayerNames.add("");
    	}
    }
	
    /**
     * Creates Lobby screen scene
     * @return
     */
	public Scene createScene()
	{
		// set up player choice spots
		Group playerGroup = new Group();
		HBox playerHbox = new HBox(50);
		int x = 100;
		int y = 480;
		for (int i = 0; i < playerChoiceSpots.length; ++i)
		{
			
			playerChoiceSpots[i] = new PlayerChoiceSpot(x,y);
			playerChoiceSpots[i].setFill(Color.GRAY);
			playerChoiceSpots[i].setStroke(Color.BLACK);
			playerNameTexts[i] = new Text(allPlayerNames.get(i));
			stackPanes[i] = new StackPane();
			stackPanes[i].getChildren().addAll(playerChoiceSpots[i], playerNameTexts[i]);
			playerHbox.getChildren().add(stackPanes[i]);
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
		PlayerChoiceSpot currentPlayerSpot = playerChoiceSpots[assignedPlayerIndex];
		for (Rectangle character : characterChoices)
		{
			character.setOnMouseClicked(new EventHandler<MouseEvent>() 
			{
	            @Override
	            public void handle(MouseEvent t) 
	            {
	            	Color charColor = (Color) character.getFill();
	            	currentPlayerSpot.setColor(charColor);
	            	GameProcessor.registerCharacterSelection(assignedPlayerIndex,  colorConverter(charColor));
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
		

		
		root.getChildren().addAll(playerGroup, characterGroup);
		return new Scene(root, 1000, 800);
	}
	
	/**
	 * Disable Ready button.
	 */
	public void disableReady()
	{
		readyButton.setDisable(true);
	}
	
	/**
	 * Enable Ready button.
	 */
	public void enableReady()
	{
		readyButton.setDisable(false);
	}
	
	/**
	 * This function adds a new player to the lobby
	 * @param playerName - Name of player added to scene
	 * @param isOwner - Controls whether player is root player or other player
	 * @param id - Id is the unique id of player
	 * @param color - Color representing character selected
	 */
	public void addPlayer(String playerName, boolean isOwner ,int id, int color)
	{
		// Adds player to array list of players
		//TODO: returns boolean, should check if player actually joined
		addPlayer( playerName, id, color);
		
		// If new player is the root player for this application, set id to assignedPlayerIndex
		if(isOwner)
		{
			assignedPlayerIndex = id;
			System.out.println("Made it 8: " + assignedPlayerIndex);
		}
		
		allPlayerNames.set(id, playerName);

		playerNameTexts[id].setText(playerName);
		if (color != -1)
		{
			playerChoiceSpots[id].setColor(colors[color]);
		}
	}
	
	private boolean addPlayer(String name, int id, int character)
	{
		Player temp = new Player();;
		temp.setName(name);
		temp.setPlayerId(id);
		temp.setCharacter(character);
		
		return players.add(temp);
	}

	/**
	 * Converts JavaFX Color to Integer in order to send serialize data to send Message
	 * @param color
	 * @return int value representing color
	 */
	private int colorConverter(Color color)
	{
		int value = -1;
		
		if(color.equals(Color.DARKRED))
		{
			value = 0;
		}
		else if(color.equals(Color.YELLOW))
		{
			value = 1;
		}
		else if(color.equals(Color.WHITE))
		{
			value = 2;
		}
		else if(color.equals(Color.GREEN))
		{
			value = 3;
		}
		else if(color.equals(Color.CADETBLUE))
		{
			value = 4;
		}
		else if(color.equals(Color.PLUM))
		{
			value = 5;
		}
		else
		{
			value = -1;
		}

		return value;
	}

	

}

package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import logic.GameProcessor;

/**
 * This class handles the creation of Connection Screen for the GUI 
 *
 */
public class ConnectionScreen 
{
	
	boolean isNewGame = false;
	private static int port = -1;
	private UserInterface ui;

    
    PlayerChoiceSpot[] playerChoiceSpots = new PlayerChoiceSpot[6];
    Rectangle[] characterChoices = new Rectangle[6];

	
	public ConnectionScreen(UserInterface ui, boolean isNewGame)
	{
        this.ui = ui;
        this.isNewGame = isNewGame;
	}
	
	public Scene createScene() 
	{
		
		StackPane root = new StackPane();
		
        /**
         * Text Field for Game Port Number.
         */
        TextField gameIdField = new TextField();
        gameIdField.setMaxWidth(200);
        
        /**
         * Set the Text for Port Number.
         */
        Text gameIdText = new Text("Enter Game Port Number.");
        gameIdText.setTranslateY(gameIdField.getTranslateY() - 50);
        
        /**
         * Text Field for Player Name.
         */
        TextField playerNameField = new TextField();
        playerNameField.setMaxWidth(200);
        playerNameField.setTranslateY(gameIdField.getTranslateY() + 100);
        
       
        /**
         * Set the Text for Player Name.
         */
        Text playerNameText = new Text("Enter your player name");
        playerNameText.setTranslateY(playerNameField.getTranslateY() - 50);
        
        /**
         * Create Continue Button.
         */
        Button continueButton = new Button("Continue");
        continueButton.setTranslateY(playerNameField.getTranslateY() + 100);

        
        /**
         * Create Blank Warning Text
         */
        Text warningText = new Text("");
        warningText.setTranslateY(playerNameField.getTranslateY() + 75);
        
        /**
         *  Add All objects to stack pane
         */
        root.getChildren().addAll(gameIdText, gameIdField, playerNameField, playerNameText, warningText, continueButton);
        Scene connectionScene = new Scene(root, 250, 500);
        
        /**
         * Create Event Handler for Continue Button.
         */
        EventHandler<ActionEvent> continueClicked = new EventHandler<ActionEvent>() 
        { 
            public void handle(ActionEvent e) 
            {	
            	/** Validate Name */
            	String name = playerNameField.getText();
            	
            	if(validPlayerName(name) && validPortNumber(gameIdField.getText()))
            	{
                	GameProcessor.getInstance();
            		GameProcessor.initializeClientServer(name, port, isNewGame, ui);
            	}
            	else
            	{
            		// TODO: Make Text Field Box Highlighted in Red. Give notice to user to re-enter valid name
                        if (!validPlayerName(name) && !validPortNumber(gameIdField.getText()))
                        {
                            warningText.setText("Invalid Name and Game ID");
                        }
                        else if (!validPlayerName(name))
                        {
                            warningText.setText("Invalid Name");
                        }
                        else
                        {
                            warningText.setText("Invalid Game ID");
                        }
            		// TODO: Figure out if every player needs a unique name
            	}
            
            } 
        };
        
        continueButton.setOnAction(continueClicked);
        

        
        return connectionScene;
	}
	
	/**
	 * Check if port number is valid
	 * @param val Port value
	 * @return true if port is valid
	 */
	private boolean validPortNumber(String val)
	{
		// Check to see if there are any spaces in string
		if(val.matches("^\\s*$"))
		{
			// Remove all white spaces
			 val.replaceAll("\\s+","");
		}
		
		// Check if port contains only numbers
		if(val.matches("[0-9]+"))
		{
			// Parse user input to integer
			port = Integer.parseInt(val);
			//Check if port is within valid range
			if( port > 1024 && port < 65200)
			{
				return true;
			}
			else
			{
				port = -1;
				return false;
			}
		}
		else
		{
			return false;
		}		
	}
	
	/**
	 * Returns whether the Player name is valid or not.
	 * Name shouldn't contain spaces, numbers, or can't be empty
	 * @param name
	 * @return true if name is valid
	 */
	private boolean validPlayerName(String name)
	{
		return !name.contains(" ") && !name.matches(".*\\d.*") && !name.isEmpty();
	}

}

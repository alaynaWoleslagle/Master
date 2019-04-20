package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ConnectionScreen {
	
	// For testing, ignore these
	// have to be global for some reason to do assignment in event handler (weird scoping with local variables)
//    boolean newGameIdOk = false;
//    boolean existingGameIdOk = false;
//    boolean playerNameOK = false;
    
    playerChoiceSpot[] playerChoiceSpots = new playerChoiceSpot[6];
    Rectangle[] characterChoices = new Rectangle[6];

	
	public ConnectionScreen()
	{
        	
	}
	
	public Scene createScene(String type) {
		
		StackPane root = new StackPane();
		
        TextField gameIdField = new TextField();
        gameIdField.setMaxWidth(400);
        Text gameIdText = new Text();
        if (type == "join")
        {
        		gameIdText.setText("Enter game id to join game");
        }
        else
        {
        		gameIdText.setText("Enter new game id");
        }
        gameIdText.setTranslateY(gameIdField.getTranslateY() - 50);
        
        TextField playerNameField = new TextField();
        playerNameField.setMaxWidth(400);
        playerNameField.setTranslateY(gameIdField.getTranslateY() + 100);
        Text playerNameText = new Text("Enter your player name");
        playerNameText.setTranslateY(playerNameField.getTranslateY() - 50);
        
        Button continueButton = new Button("Continue");
        continueButton.setTranslateY(playerNameField.getTranslateY() + 100);

        EventHandler<ActionEvent> continueClicked = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	// For testing only, ignore this
//                existingGameIdOk = validateExistingGameId(gameIdField.getText());
//                newGameIdOk = validateNewGameId(gameIdField.getText());
//                playerNameOK = validatePlayerName(playerNameField.getText());
//                
//                if (playerNameOK)
//                {
//                		if (type == "join" && existingGameIdOk)
//                		{
//                			createLobby(stage);
//                		}
//                		else if (type == "new" && newGameIdOk)
//                		{
//                			createLobby(stage);
//                		}
//                		
//                }
//                if (!newGameIdOk && type == "new")
//                {
//	                	Text gameIdErrorText = new Text("New game id not valid!");
//	            		gameIdErrorText.setTranslateY(continueButton.getTranslateY() + 35);
//	            		gameIdErrorText.setFill(Color.RED);
//	            		root.getChildren().add(gameIdErrorText);
//                }
//                if (!existingGameIdOk && type == "join")
//                {
//                		Text gameIdErrorText = new Text("Game id not found!");
//                		gameIdErrorText.setTranslateY(continueButton.getTranslateY() + 35);
//                		gameIdErrorText.setFill(Color.RED);
//                		root.getChildren().add(gameIdErrorText);
//                }
//                if(!playerNameOK)
//                {
//                		Text playerNameErrorText = new Text("Player name already exists or is invalid!");
//                		playerNameErrorText.setTranslateY(continueButton.getTranslateY() + 55);
//                		playerNameErrorText.setFill(Color.RED);
//                		root.getChildren().add(playerNameErrorText);
//                }
            } 
        };
        continueButton.setOnAction(continueClicked);
        
        root.getChildren().addAll(gameIdText, gameIdField, playerNameField, playerNameText, continueButton);
        Scene connectionScene = new Scene(root, 0, 0);
        
        return connectionScene;
	}
	
	// all for testing only, ignore
//	private boolean validateExistingGameId(String gameId)
//	{
//		
//		return true;
//	}
//	
//	private boolean validateNewGameId(String gameId)
//	{
//		
//		return true;
//	}
//	
//	private boolean validatePlayerName(String playerName)
//	{
//		
//		return true;
//	}

}

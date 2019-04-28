package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/** 
 * This class is responsible for Generating the start screen.
 *
 */
public class StartScreen 
{

	private UserInterface ui;
	
	/**
	 * Start Screen Constructor
	 * @param ui UserInterface
	 */
	public StartScreen(UserInterface ui)
	{
		this.ui = ui;
	}
	
	/**
	 * Creates the start screen
	 * @return Scene Object
	 */
	public Scene createStartScene() 
	{
		
		StackPane root = new StackPane();
        
		/**
         * Set the Text for Welcome screen.
         */
		Text welcomeText = new Text("Welcome to Clue-Less!");
        welcomeText.setStyle("-fx-font: 24 arial;");
        welcomeText.setTextAlignment(TextAlignment.CENTER);
        StackPane.setAlignment(welcomeText, Pos.CENTER);

        /**
         * Create Join Game Button
         */
        Button joinButton = new Button("Join Game");
        joinButton.setMinWidth(150);
        joinButton.setTranslateY(welcomeText.getTranslateY() + 100);

        /**
         * Create Start New Game Button
         */
        Button startButton = new Button("Start a New Game");
        startButton.setMinWidth(150);
        startButton.setTranslateY(welcomeText.getTranslateY() + 150);

        /**
         * Add Text and Button elements to the Screen.
         */
        root.getChildren().addAll(welcomeText, joinButton, startButton);
		Scene startScene = new Scene(root, 350, 500);

        /**
         * Create Event Handler for Join button.
         */
        EventHandler<ActionEvent> joinGameClicked = new EventHandler<ActionEvent>() 
        { 
            public void handle(ActionEvent e) 
            {
				ConnectionScreen connectionScreen = new ConnectionScreen(ui, false);
            	ui.setScene(connectionScreen.createScene());
            } 
        }; 
        
        /**
         * Create Event Handler for New Game button.
         */
        EventHandler<ActionEvent> startNewGameClicked = new EventHandler<ActionEvent>() 
        { 
            public void handle(ActionEvent e) 
            {
				ConnectionScreen connectionScreen = new ConnectionScreen(ui, true);
				ui.setScene(connectionScreen.createScene());
            }
        }; 
  
        joinButton.setOnAction(joinGameClicked); 
        startButton.setOnAction(startNewGameClicked);
        
		
		return startScene;
	}
	
}

package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class StartScreen {

	private UserInterface ui;
	
	public StartScreen(UserInterface ui)
	{
		this.ui = ui;
	}
	
	public Scene createStartScene() {
		
		StackPane root = new StackPane();
        Text welcomeText = new Text("Welcome to Clue-Less!");
        welcomeText.setStyle("-fx-font: 48 arial;");
        welcomeText.setTextAlignment(TextAlignment.CENTER);
        Button joinButton = new Button("Join Game");
        Button startButton = new Button("Start a New Game");
        root.getChildren().addAll(welcomeText, joinButton, startButton);
        StackPane.setAlignment(welcomeText, Pos.CENTER);
        joinButton.setMinWidth(150);
        startButton.setMinWidth(150);
        joinButton.setTranslateY(welcomeText.getTranslateY() + 100);
        startButton.setTranslateY(welcomeText.getTranslateY() + 150);
        
        EventHandler<ActionEvent> joinGameClicked = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            {
				ConnectionScreen connectionScreen = new ConnectionScreen();
            	ui.setScene(connectionScreen.createScene("join"));
            } 
        }; 
        
        EventHandler<ActionEvent> startNewGameClicked = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            {
				ConnectionScreen connectionScreen = new ConnectionScreen();
				ui.setScene(connectionScreen.createScene("new"));
            }
        }; 
  
        joinButton.setOnAction(joinGameClicked); 
        startButton.setOnAction(startNewGameClicked);
        
		Scene startScene = new Scene(root, 700, 900);
		
		return startScene;
	}
}

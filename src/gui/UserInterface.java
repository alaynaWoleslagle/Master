package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UserInterface extends Application{

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception {
		
		//Group root = new Group();
		
		Scene startScene = createStartScreen();
		
		// call this after character selection
		//createGameBoard(root);
		
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		
		stage.setScene(startScene);
		
		stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth() * .8);
        stage.setHeight(primaryScreenBounds.getHeight());

		
		stage.show();
		
		
	}
	
	private void createGameBoard(Group root) {
		
		// Build horizontal hallways
        for (int x = 0 ; x < 400 ; x+=200) {
            for (int y = 0 ; y < 500 ; y+=200) {
            		Hallway hallway = new Hallway(x + 35, y, "horizontal");
                
                root.getChildren().addAll(hallway.getHallway());
            }
        }
        
		// Build vertical hallways
        for (int x = 0 ; x < 500 ; x+=200) {
            for (int y = 0 ; y < 400 ; y+=200) {
            		Hallway hallway = new Hallway(x, y + 60, "vertical");
                
                root.getChildren().addAll(hallway.getHallway());
            }
        }
        
        // Build rooms
		String[] roomNames = {"Study", "Library", "Conservatory", "Hall", "Billiard Room", "Ballroom", "Lounge",
				"Dining Room", "Kitchen"};
		int i = 0;
		
        for (int x = 0 ; x < 600 ; x+=200) {
            for (int y = 0 ; y < 600 ; y+=200) {
            		Room room = new Room(x, y, roomNames[i]);
                Text roomName = new Text(room.getName());
                roomName.setTranslateY(y + 50);
                roomName.setTranslateX(x + 10);
                root.getChildren().addAll(room.getRoom(), roomName);
                
                ++i;
            }
        }
        
        // create rooms
        // create hallways and pass in connecting rooms
        // manually assign hallways for each room
        
        // shift board slightly, so it isn't right on the left edge of window
        root.setTranslateX(root.getTranslateX() + 50);
        root.setTranslateY(root.getTranslateY() + 50);
	}
	
	private Scene createStartScreen()
	{
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
        
		Scene startScene = new Scene(root, 0, 0);
		return startScene;
		
	}

}

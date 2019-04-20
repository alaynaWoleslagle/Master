package gui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UserInterface extends Application{

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage stage) throws Exception {
		
		
//		String[] names = {"player1", "player2"};
//		LobbyScreen lobby = new LobbyScreen(2, names);
//		Scene lobbyScene = lobby.createLobby();
//		lobby.addPlayer("EMMA");
		
		StartScreen start = new StartScreen();
		Scene startScene = start.createStartScene();
		
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		
		stage.setScene(startScene);
		
		stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(primaryScreenBounds.getWidth() * .8);
        stage.setHeight(primaryScreenBounds.getHeight());

		
		stage.show();
		
		
	}
}

package gui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UserInterface extends Application{

	private Stage stage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		// call this after character selection
//		String[] players = {"1", "2", "3", "4"};
//		String[] cards = {"rope", "plum", "billiard room", "pipe"};
//		int assignedTurnIndex = 2;
//		GameScreen gameScreen = new GameScreen(players, cards, assignedTurnIndex);
//		gameScreen.setUi(this);
//		Scene gameScene = gameScreen.createScene();
		
		
//		String[] names = {"player1", "player2"};
//		LobbyScreen lobby = new LobbyScreen(2, names);
//		Scene lobbyScene = lobby.createLobby();
//		lobby.addPlayer("EMMA");
		
		StartScreen start = new StartScreen();
		Scene startScene = start.createStartScene();
		
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		setScene(startScene);
//		setScene(gameScene);
		
		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());
		stage.setWidth(primaryScreenBounds.getWidth() * .8);
		stage.setHeight(primaryScreenBounds.getHeight());

		
		stage.show();
		
		
	}

	public void setScene(Scene scene) {
		stage.setScene(scene);
		stage.setWidth(scene.getWidth());
		stage.setHeight(scene.getHeight());
		stage.show();
	}
}

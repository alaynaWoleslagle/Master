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

		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		stage.setX(primaryScreenBounds.getMinX());
		stage.setY(primaryScreenBounds.getMinY());

		//example start screen setup
		StartScreen start = new StartScreen(this);
		Scene startScene = start.createStartScene();
		setScene(startScene);

		//example lobby screen setup
//		String[] names = {"player1", "player2"};
//		LobbyScreen lobby = new LobbyScreen(2, names);
//		Scene lobbyScene = lobby.createLobby();
//		lobby.addPlayer("EMMA");
//		setScene(lobbyScene);

		//example game screen setup
//		String[] players = {"1", "2", "3", "4"};
//		String[] cards = {"rope", "plum", "billiard room", "pipe"};
//		int assignedTurnIndex = 2;
//		GameScreen gameScreen = new GameScreen(players, cards, assignedTurnIndex);
//		gameScreen.setUi(this);
//		Scene gameScene = gameScreen.createScene();
//		setScene(gameScene);

	}

	public void setScene(Scene scene) {
		//order is important: width and height must be set before scene is set
		stage.setWidth(scene.getWidth());
		stage.setHeight(scene.getHeight());
		stage.setScene(scene);
		stage.show();
	}
}

package gui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UserInterface extends Application
{

	private Stage stage;

	/*public static void main(String[] args)
	{
		launch(args);
	}*/

	@Override
	public void start(Stage stage) throws Exception 
	{
		stage.getIcons().add(new Image("file:resources/wildcards_icon.png"));
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
//		Scene lobbyScene = lobby.createScene();
//		lobby.addPlayer("EMMA", true, 0, 0);
//		setScene(lobbyScene);

		//example game screen setup
//		String[] players = {"1", "2", "3", "4"};
//		String[] cards = {"rope", "plum", "billiard room", "pipe"};
//		int assignedTurnIndex = 2;
//		GameScreen gameScreen = new GameScreen(players, cards, assignedTurnIndex);
//		gameScreen.setUi(this);
//		Scene gameScene = gameScreen.createScene();
//		setScene(gameScene);

		//example suggestion screen setup
//		SuggestAccuseScreen suggestAccuseScreen = new SuggestAccuseScreen("Study");
		//example accusation screen setup
//		SuggestAccuseScreen suggestAccuseScreen = new SuggestAccuseScreen();
//		Scene suggestAccuseScene = suggestAccuseScreen.createScene();
//		setScene(suggestAccuseScene);
	}

	public void setScene(Scene scene) 
	{
		//order is important: width and height must be set before scene is set
		stage.setWidth(scene.getWidth());
		stage.setHeight(scene.getHeight());
		stage.setScene(scene);
		stage.show();
	}
	
	public UserInterface getUI()
	{
		return this;
	}
}

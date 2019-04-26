package controller;

import logic.GameProcessor;
import view.UserInterface;

public class ViewController 
{

	private static volatile ViewController instance = null;


	/**
	 * Creates the Singleton instance of the ViewController class
	 * @return ViewController Singleton
	 */
	public static ViewController getInstance()
	{
		if( instance == null)
		{
			synchronized (ViewController.class)
			{
				if( instance == null)
				{
					instance = new ViewController();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Private Client constructor.
	 */
	private ViewController()
	{
        /**
         *  This checks to ensure no other instance is created using Reflection API
         */
		if (instance != null){}
		else
		{
			// Initialize Controller. 
		}
	}
	
	/**
	 * This method accepts new player entry data from the GUI and forwards results to the Game Controller.
	 * 		- If new game, a server is created.
	 * 		- A client connection for player is created.
	 * 		- Player Information is stored.
	 * @param name - Name of player
	 * @param port - Port used for connection to game or port use to create new game.
	 */
	public static void joinNewPlayer(String name, int port, boolean isNewGame, UserInterface ui)
	{
		// Create Server if new game.
		// Create Client.
		// Game Logic Method call to register player.
		GameProcessor.getInstance();
		System.out.println("Name: " + name + " Port: " + port + " New Game? " + isNewGame);
		GameProcessor.initializeClientServer(name, port, isNewGame, ui);
	}
	
	public static void registerCharacterSelection(int playerId, int color)
	{
		GameProcessor.registerCharacterSelection(playerId, color);
	}
	public static void addNewPlayer()
	{
		
	}
}

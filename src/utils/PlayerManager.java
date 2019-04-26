package utils;

import java.util.ArrayList;

import messages.PlayerStatusMessage;
import messages.BaseMessage.Action;
import socket.ClientMessageReceiver;

/**
 * This class is responsible to maintaining the status and information of each player in the game.
 * Notable objects in the class include an ArrayList
 * @author Trae
 * @version 1.0 4/10/19
 */
public class PlayerManager 
{
    private static volatile PlayerManager instance = null;
    private static volatile ArrayList<Player> playerList = null;
    private static volatile Player player = null;

	
    /**
     * Private PlayerManager constructor.
     */
	private PlayerManager()
	{
        /**
         *  This checks to ensure no other instance is created using Reflection API
         */
        if (instance != null)
        {
        	System.out.println("Exception Handled: Attempt to create new Instance using Reflection API");
        }
        else
        {
        	playerList = new ArrayList<Player>();
        	player = new Player();
        	System.out.println("[INFO]: Player Manager Initialized.");
        }
	}
	

	/**
	 * Returns the singleton instance of PlayerManager class.
	 * @return PlayerManager instance.
	 */
    public static PlayerManager getInstance() 
    {
        if (instance == null) 
        { 
        	/**
        	 *  This is a thread-safe check to ensure another thread can't initialize another MessageReceiver class.
        	 */
            synchronized (PlayerManager.class) 
            {
                if (instance == null)
                {
                	instance = new PlayerManager();
                }
                
            }
        }

        return instance;
    }
    
	/**
	 * Initializes the player object.
	 * This function sends a notification message to the server and will in return
	 * receive a unique player ID.
	 * @param name Name of player.
	 */
    public static void initializePlayer(String name)
    {
    	PlayerStatusMessage message = new PlayerStatusMessage();
    	message.setName(name);
    	message.setType(Action.INIT);
    	System.out.println("Client Action: Initializing new Client: " + name);
    	
    	ClientMessageReceiver.sendMessage(message);
    }
    
    /**
     * Sets the Player Object.
     * This function is called when the server sends back a message containing the players unique ID.
     * @param obj Player Object received by server.
     */
    public static void storInitPlayer(Player obj)
    {
    	if (obj instanceof Player)
    	{
    		PlayerManager.player = obj;
    	}
    }


	public static Player getPlayer() 
	{
		return player;
	}
	
	public static void setPlayer(Player obj)
	{
		player = obj;
	}
	
	public synchronized static void addNewPlayer(Player player)
	{
		playerList.add(player);
		System.out.println("New Player: " + player);
	}
	
	public synchronized static void removePlayer(Player player)
	{
		playerList.remove(player);
	}
	
	public static int playerCount()
	{
		return playerList.size();
	}
        
}



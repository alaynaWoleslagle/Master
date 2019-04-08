package utils;

import java.util.ArrayList;

import messages.Player;
import messages.BaseMessage.MessageType;
import socket.Client;

public class PlayerManager 
{
    private static volatile PlayerManager instance = null;
    private static volatile ArrayList<Player> playerList = null;
    private static Player player = null;

	
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
        	System.out.println("Player Manager Started");
        }
	}
	

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
    
	
    
    public static void initializePlayer(String name)
    {
    	Player message = new Player(name);
    	message.setType(MessageType.INIT);
    	System.out.println("Client Action: Initializing new Client: " + name);
    	
    	Client.send(message);
    }
    
    public static void storeInitPlayer(Player obj)
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
	
	public synchronized static void addPlayer(Player player)
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



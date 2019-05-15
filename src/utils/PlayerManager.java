package utils;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
    //private static volatile ArrayList<Player> playerList = null;
    private static volatile Map<Integer,Player> playerList = null;

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
    		playerList = new HashMap<Integer, Player>();
        	//playerList = new ArrayList<Player>();
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
    	player.setName(name);

    	PlayerStatusMessage message = new PlayerStatusMessage();
    	message.setName(name);
    	message.setType(Action.PLAYER_INIT);
    	System.out.println("Client Action: Initializing new Client: " + name);
    	ClientMessageReceiver.sendMessage(message);
    }
    
    /**
     * Sets the Player Object.
     * This function is called when the server sends back a message containing the players unique ID.
     * @param obj Player Object received by server.
     */
    public static void storIniPlayer(Player obj)
    {
    	if (obj instanceof Player)
    	{
    		player = obj;
    	}
    }

	public static Player getPlayer() 
	{
		return player;
	}
	
	
	public synchronized static void addNewPlayer(Player player)
	{       
                //sort the list by ID
		playerList.put(player.getPlayerId(), player);
		//playerList.add(player);               
		System.out.println("New Player: " + player);          
	}
	
	public synchronized static void removePlayer(Player player)
	{
		playerList.remove(player.getPlayerId());
		//playerList.remove(player);
	}
	
	public static int playerCount()
	{
		return playerList.size() + 1;                
	}
	
	public synchronized static Player getOtherPlayer(int playerId)
	{
		return playerList.get(playerId);
	
     /*   for (Player currentPlayer : playerList)
        {
        	if (currentPlayer.getPlayerId() == playerId)
        	{
        		return currentPlayer;
        	}
        }
                //Player not found, return Null
                return null;*/
	}
	
	/**
	 * This function updates the Player status.
	 * @param isReady current status of player
	 * @return true: if player is ready to start game.
	 */
	public synchronized static boolean updateReadyStatus(boolean isReady)
	{
		player.setReady(isReady);
		
		//TODO: Check if player status is really being checked
		return true;
	}
	
	public synchronized static int getPlayerId(String name)
	{
		int playerId = -1;
        for (Entry<Integer, Player> entry : playerList.entrySet())
        { 
        	if(entry.getValue().getName().equals(name))
        	{
        		playerId = entry.getValue().getPlayerId();
        	}
        }
        return playerId;
	}
	
	
	/**
	 * Returns an String[] of Player names.
	 * @return String[] of names
	 */
    public synchronized static String[] GetPlayerNames()
    {
    	String[] players = new String[playerCount()];
        for (Entry<Integer, Player> entry : playerList.entrySet())
        { 
        	players[entry.getValue().getPlayerId()] = entry.getValue().getName();
        }
        players[player.getPlayerId()] = player.getName();
        
        return players;
    }
    
    public synchronized static String getCharacterName(int index)
    {
    	Player player = null;
    	player = playerList.get(index);
    	String name = "";
    	if(player != null)
    	{
    		name = player.getName(); 
    	}
    	return name;
    }
    
    
    
    public synchronized static String[] getHandAsArray()
    {
    	String[] cards = new String[3];
    	
    	cards[0] = player.getPlayerCard().getValue();
    	cards[1] = player.getRoomCard().getValue();
    	cards[2] = player.getWeaponCard().getValue();
    	
    	System.out.println(cards[0] + " " + cards[1] + " " + cards[2]);

    	return cards;
    }
	public static void updatePlayerColor(int id, int color)
	{
		System.out.println("ID: " + id + " Color: " + color);
		if(playerList.containsKey(id))
		{
			playerList.computeIfPresent(id, (k, c) -> updateColor(c, color) );
		}
		
		System.out.println("Color: " + playerList.get(id).getColor() + " Name: " + playerList.get(id).getName());
		
	}

	private static Player updateColor(Player player, int color)
	{
		player.setColor(color);
		return player;
	}

}



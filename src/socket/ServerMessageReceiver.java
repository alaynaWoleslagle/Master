package socket;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import messages.BaseMessage.Action;
import messages.PlayerStatusMessage;
import utils.Player;

/**
 * Singleton Class
 * This class is responsible for receiving Messages from the Queue.
 * Messages are added to a Queue which is will then be processed on a separate thread.
 * 
 * This class acts as an interface between the Server socket and Client Socket through the receipt of messages.
 * 
 * @author Trae
 *
 * @version 1.0 (4/5/2019)
 * 
 */
public class ServerMessageReceiver extends MessageReceiver
{
    private static volatile ServerMessageReceiver msgReceiver = null;
    private static volatile Map<Integer,Player> playerTruth = null;

	
	private ServerMessageReceiver()
	{
        /**
         *  This checks to ensure no other instance is created using Reflection API
         */
        if (msgReceiver != null)
        {
        	System.out.println("[WARNING]: Attempt to create new Instance using Reflection API");
        }
        else
        {
        	if(initialize())
        	{
        		playerTruth = new HashMap<Integer, Player>();
        		System.out.println("[INFO]: Server Message Receiver Started.");

        	}
        }
	}
	
    public static ServerMessageReceiver getInstance() 
    {
        if (msgReceiver == null) 
        { 
        	/**
        	 *  This is a thread-safe check to ensure another thread can't initialize another MessageReceiver class.
        	 */
            synchronized (ServerMessageReceiver.class) 
            {
                if (msgReceiver == null)
                {
                	msgReceiver = new ServerMessageReceiver();
                }
                
            }
        }

        return msgReceiver;
    }
    
	
    /**
     * This function updates the player start status 
     * @param id
     * @param isReady
     * @return true: is all players in game are ready
     */
	protected synchronized boolean updatePlayerStartStatus(int id, boolean isReady)
	{
		if(playerTruth.containsKey(id))
		{
			System.out.println("[TEST]: Player Start Status: " + playerTruth.get(id).isReady());

			playerTruth.computeIfPresent(id, (k, c) -> updateStart(c, isReady) );
			System.out.println("[TEST]: Player Start Status: " + playerTruth.get(id).isReady());
		}
		
		return allPlayersReady();
	}
    
    private Player updateStart(Player player, boolean ready)
    {
    	player.setReady(ready);
		return player;
    }
    
    /**
     * TODO: This function routes incoming messages to for main processing.
     * TODO: Game logic function should be called here. Function that receives Message and processes it.
     * @param msg
     */
    protected synchronized void processIncomingMessage(Object object)
    {
    	if( object instanceof PlayerStatusMessage )
    	{
    		PlayerStatusMessage msg = (PlayerStatusMessage) object;
    		
    		if(msg.getType() == Action.PLAYER_START && playerTruth.containsKey(msg.getPlayerId()))
    		{
    			playerTruth.computeIfPresent(msg.getPlayerId(), (k, c) -> updateStart(c, true) );
    		}
    		else
    		{
    			System.out.println("No player in game");
    		}
    	}
    }
    


	protected synchronized static void addPlayer(PlayerStatusMessage object) 
	{
		Player player = new Player();
		player.setName(object.getName());
		player.setPlayerId(object.getPlayerId());
		
		playerTruth.put(object.getPlayerId(), player);
		System.out.println("[INFO]: Player added to game. " + player.getName());
	}
	
	protected synchronized void updatePlayerCharacter(PlayerStatusMessage object)
	{
		playerTruth.computeIfPresent(object.getPlayerId(), (k,c) -> updateCharacter(c, object.getVarField1()));
	}
    
    private Player updateCharacter(Player player, int color)
    {
    	player.setCharacter(color);
    	return player;
    }
    
    /**
     * Returns true if all players are in ready status
     * @return
     */
    private synchronized boolean allPlayersReady()
    {
        for (Entry<Integer, Player> entry : playerTruth.entrySet())
        { 
        	if(entry.getValue().isReady() == false)
        	{
        		return false;
        	}
        }
        return true;
    }


}

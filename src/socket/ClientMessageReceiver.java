package socket;

import messages.BaseMessage.MessageType;
import messages.ClientInitMessage;
import messages.Player;
import utils.PlayerManager;

/**
 * 
 * @author Trae
 *
 * @version 2.0 (4/4/2019)
 * 
 * Singleton Class
 * This class is responsible for receiving Messages from the Queue.
 * Messages are added to a Queue which is will then be processed on a separate thread.
 * 
 * This class acts as an interface between the Client socket and main Game logic through the receipt of messages.
 */
public class ClientMessageReceiver extends MessageReceiver
{
    private static volatile ClientMessageReceiver msgReceiver = null;
	
	private ClientMessageReceiver()
	{
        /**
         *  This checks to ensure no other instance is created using Reflection API
         */
        if (msgReceiver != null)
        {
        	System.out.println("Exception Handled: Attempt to create new Instance using Reflection API");
        }
        else
        {
        	if(initialize())
        	{
        		System.out.println("Client Message Receiver Started");
        	}
        }
	}
	
    public static ClientMessageReceiver getInstance() 
    {
        if (msgReceiver == null) 
        { 
        	/**
        	 *  This is a thread-safe check to ensure another thread can't initialize another MessageReceiver class.
        	 */
            synchronized (ClientMessageReceiver.class) 
            {
                if (msgReceiver == null)
                {
                	msgReceiver = new ClientMessageReceiver();
                }
                
            }
        }

        return msgReceiver;
    }
    

   
    
    /**
     * TODO: This function routes incoming messages to for main processing.
     * TODO: Game logic function should be called here. Function that receives Message and processes it.
     * @param msg
     */
    public void processIncomingMessage(Object msg)
    {
    	if( msg instanceof Player )
    	{
    		Player player = (Player) msg;
    		MessageType type = player.getType();
    		
    		if(type == MessageType.INIT)
    		{
        		PlayerManager.storeInitPlayer(player);
    		}
    		else if(type == MessageType.PLAYER_JOIN)
    		{
        		PlayerManager.addPlayer(player);
    		}
        	//System.out.println("--> Receiving Object:   "+player);
    	}
    }
    
    
}

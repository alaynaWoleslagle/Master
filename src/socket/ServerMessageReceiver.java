package socket;


import messages.PlayerStatusMessage;

/**
 * 
 * @author Trae
 *
 * @version 1.0 (4/5/2019)
 * 
 * Singleton Class
 * This class is responsible for receiving Messages from the Queue.
 * Messages are added to a Queue which is will then be processed on a separate thread.
 * 
 * This class acts as an interface between the Server socket and Client Socket through the receipt of messages.
 */
public class ServerMessageReceiver extends MessageReceiver
{
    private static volatile ServerMessageReceiver msgReceiver = null;

	
	private ServerMessageReceiver()
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
        		System.out.println("Server Message Receiver Started");

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
     * TODO: This function routes incoming messages to for main processing.
     * TODO: Game logic function should be called here. Function that receives Message and processes it.
     * @param msg
     */
    public void processIncomingMessage(Object msg)
    {
    	//int playerId = -1;
    	if( msg instanceof PlayerStatusMessage )
    	{
    		//Player player = (Player) msg;
    		
        	//System.out.println("--> Receiving Object:   "+player);
    	}
    	
    	
    }
    
    


}

package logic;

import messages.PlayerStatusMessage;
import messages.BaseMessage.Action;
import utils.Player;
import utils.PlayerManager;

public class GameProcessor 
{
    private static volatile GameProcessor instance = null;

	private GameProcessor()
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
        	System.out.println("Player Manager Started");
        }
	}
	

    public static GameProcessor getInstance() 
    {
        if (instance == null) 
        { 
        	/**
        	 *  This is a thread-safe check to ensure another thread can't initialize another MessageReceiver class.
        	 */
            synchronized (GameProcessor.class) 
            {
                if (instance == null)
                {
                	instance = new GameProcessor();
                }
                
            }
        }

        return instance;
    }
    
    
    public static void  processMessage(Object msg)
    {
    	if( msg instanceof PlayerStatusMessage )
    	{
        	PlayerStatusMessage tmpMsg = (PlayerStatusMessage)msg;
        	Player tmp = new Player();
        	tmp.setName(tmpMsg.getName());
        	tmp.setPlayerId(tmpMsg.getPlayerId());
        	
    		if(tmpMsg.getType() == Action.INIT)
    		{
    			PlayerManager.setPlayer(tmp);
    		}
    		else if(tmpMsg.getType() == Action.PLAYER_JOIN)
    		{
        		PlayerManager.addNewPlayer(tmp);
    		}        	
    	}
    }
    
    

}

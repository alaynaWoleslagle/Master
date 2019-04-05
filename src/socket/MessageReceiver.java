package socket;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author Trae
 *
 * @version 1.0 (4/4/2019)
 * 
 * Singleton Class
 * This class is responsible for receiving Messages from the Queue.
 * Messages are added to a Queue which is will then be processed on a separate thread.
 * 
 * This class acts as an interface between the Client socket and main Game logic through the receipt of messages.
 */
public class MessageReceiver 
{
    private static volatile MessageReceiver msgReceiver = null;
    private Queue<Object> queue = null;
    private boolean running = false;
    Thread processThread;
	
	private MessageReceiver()
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
        		System.out.println("Message Receiver Started");
        	}
        }
	}
	
    public static MessageReceiver getInstance() 
    {
        if (msgReceiver == null) 
        { 
        	/**
        	 *  This is a thread-safe check to ensure another thread can't initialize another MessageReceiver class.
        	 */
            synchronized (MessageReceiver.class) 
            {
                if (msgReceiver == null)
                {
                	msgReceiver = new MessageReceiver();
                }
                
            }
        }

        return msgReceiver;
    }
    
    /**
     * @author Trae
     * 
     * This function clears out Message Queue and 
     */
    public void close()
    {
    	/**
    	 * Setting Running flag to false exits loop for threaded process functions.
    	 * Daemon threads will exit if they are the only ones left.
    	 */
    	running = false;
    	queue.clear();
    }
    
    /**
     * @author Trae
     * 
     * Initializes Message Queue and Starts Message Processor Thread.
     * 
     * @return true: Message Processor Thread started.
     */
    private boolean initialize()
    {
    	queue = new LinkedList<Object>();
    	running = true;
    	processThread();
    	
    	return true;
    }
    
    /**
     * @author Trae
     * 
     * Receives message from Client Socket and adds it to Message Queue to be processed.
     * 
     * @param msg: Message to be added to Message Queue.
     */
    public synchronized void receiveIncomingMessage(Object msg)
    {
    	queue.add(msg);
    }
    
    /**
     * @author Trae
     * 
     * Synchronized function that removes and returns the first element in queue.
     * 
     * @return 
     */
    private synchronized Object pop()
    {
    	return queue.poll();
    }
    
    /**
     * TODO: This function routes incoming messages to for main processing.
     * TODO: Game logic function should be called here. Function that receives Message and processes it.
     * @param msg
     */
    private void processIncomingMessage(Object msg)
    {
    	System.out.println("--> Receiving Object:   "+msg);
    }
    

	private void processThread()
	{

    	Runnable listener = new Runnable()
    	{
            @Override
        	public void run()
        	{
            	while(running)
            	{
         		   try 
        		   {
        			   Thread.sleep(10);
        		   } 
        		   catch (InterruptedException e) 
        		   {
        			   // TODO Auto-generated catch block
        			   e.printStackTrace();
        		   }
            		if(queue.size() > 0)
            		{
            			Object tmp = pop();
            			if(tmp != null)
            			{
            				processIncomingMessage(tmp);
            			}
            			else
            			{
            				System.out.println("Exception Handled: Received NULL Message");
            			}
            		}
            	}
            	
        	}
        };
        processThread = new Thread(listener);
        processThread.setPriority(1);
        processThread.setDaemon(true);
        processThread.start();
	}
    
}

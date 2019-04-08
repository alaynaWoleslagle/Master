package socket;

import java.util.LinkedList;
import java.util.Queue;

public abstract class MessageReceiver 
{
	
    protected static Queue<Object> queue = null;
    protected static boolean running = false;
    protected Thread processThread;

    
    
    
    /**
     * @author Trae
     * 
     * Initializes Message Queue and Starts Message Processor Thread.
     * 
     * @return true: Message Processor Thread started.
     */
    protected boolean initialize()
    {
    	queue = new LinkedList<Object>();
    	running = true;
    	processThread();
    	
    	return true;
    }
    
    /**
     * @author Trae
     * 
     * This function clears out Message Queue and 
     */
    public static void close()
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
     * Receives message from Client Socket and adds it to Message Queue to be processed.
     * 
     * @param msg: Message to be added to Message Queue.
     */
    protected synchronized static void receiveIncomingMessage(Object msg)
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
    protected synchronized Object pop()
    {
    	return queue.poll();
    }
    
	protected void processThread()
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

	protected abstract void processIncomingMessage(Object tmp);
    
}

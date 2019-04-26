package socket;

import java.util.LinkedList;
import java.util.Queue;

public abstract class MessageReceiver 
{
	
    protected static volatile Queue<Object> queue = null;
    protected static volatile boolean running = false;
    protected volatile Thread processThread;

    
    
    
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
    	
    	return true;
    }
    
    /**
     * This function clears out Message Queue and 
     *
     * @author Trae
     * 
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
     * Receives message from Client Socket and adds it to Message Queue to be processed.
     * @param  
     * 
     * @param msg: Message to be added to Message Queue.
     */
    protected synchronized void receiveIncomingMessage(Object msg)
    {
    	queue.add(msg);
    	System.out.println("Queue Size: " + queue.size());
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
    

    /**
     * Abstract function for processing incoming messages.
     * @param tmp Object being received from the socket.
     */
	protected abstract void processIncomingMessage(Object tmp);
    
}

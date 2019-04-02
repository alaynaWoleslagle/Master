package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * 
 * @author Trae, Alayna
 * 
 * @version 1.0 (4/1/2019)
 * 
 * Singleton class that handles client (player) socket connection.
 * Responsible for receiving Message Objects from other players.
 * Responsible for sending Message Objects to other players. 
 * 
 *
 */
public class Client
{
	/**
	 * Private static variables.
	 */
	private static Client instance = null;
	private static Socket socket;
	private static ObjectInputStream in;		// from server
	private static ObjectOutputStream out;		// to server

	/**
	 * Creates the Singleton instance of the Client class
	 * @return Client Singleton
	 */
	public static Client getInstance()
	{
		if( instance == null)
		{
			instance = new Client();
		}
		
		return instance;
	}
	
	/**
	 * Private Client constructor.
	 */
	private Client()
	{
		initialize();
	}

	/**
	 * Initializes the Client Socket connection.
	 * Calls threaded listener function.
	 */
	private void initialize()
	{
		try
		{
			socket = new Socket("localhost", 4242);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());


			listenerThread();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 *  Receives socket Object sent over the server.
	 * @param msg  Message Object received by the server.
	 * 
	 * @TODO: This method should add the received Object to a Threaded Queue to relieve processing 
	 *        pressure from the main thread.
	 */
	private void receiveMessage(Object msg)
	{
		System.out.println(msg);
		// Do Something.
		// Add Messages to A Queue for processing.
		// Create A thread that processes messages.
	}

	/**
	 *  Responsible for sending Message Objects over the socket to be received by other clients.
	 * @param msg Message Object to be sent to the server/clients.
	 */
	public static void send(Object msg)
	{
		try
		{
			out.writeObject(msg);
		}
		catch (IOException e){}
	}

	/**
	 *  Threaded method that is responsible for monitoring socket connection for incoming messages
	 *  from other clients.
	 *  Responsible for maintaining socket connection.
	 */
	private void listenerThread()
	{

    	Runnable listener = new Runnable()
    	{
            @Override
        	public void run()
        	{
        		// Get lines from server; print to console
        		try
        		{
        			while (!socket.isClosed())
        			{
        				receiveMessage(in.readObject());
        			}
        		}
        		catch (IOException | ClassNotFoundException e){}
        		finally
        		{
        			System.out.println("Server Disconnected.");
        			try 
        			{
        				System.out.println("Closing socket connection.");
        				out.close();
        				in.close();
        				socket.close();
        			} 
        			catch (IOException e1) 
        			{
        			}
        		}

        	}

        };
        Thread clientListener = new Thread(listener);
        clientListener.start();
	}
}

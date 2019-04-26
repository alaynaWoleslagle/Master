package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utils.PlayerManager;


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
	private static volatile Client instance = null;
	private static Socket socket;
	private static ObjectInputStream in;		// from server
	private static ObjectOutputStream out;		// to server
	private static boolean connected = false;
	private static ClientMessageReceiver msgReceiver = null;


	

	/**
	 * Creates the Singleton instance of the Client class
	 * @return Client Singleton
	 */
	public static Client getInstance()
	{
		if( instance == null)
		{
			synchronized (Client.class)
			{
				if( instance == null)
				{
					instance = new Client();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Private Client constructor.
	 */
	private Client()
	{
        /**
         *  This checks to ensure no other instance is created using Reflection API
         */
		if (instance != null)
		{
        	System.out.println("[WARNING]: Attempt to create new Instance using Reflection API");
		}
		else
		{
        	System.out.println("[INFO]: Initializing Client Connection...");        	

			if(initialize())
			{
	        	System.out.println("[INFO]: Client Connection Established.");        	
			}
		}
	}


	/**
	 * Initializes the Client Socket connection.
	 * Calls threaded listener function.
	 */
	private boolean initialize()
	{
		try
		{
			PlayerManager.getInstance();
			msgReceiver = ClientMessageReceiver.getInstance();
			socket = new Socket("localhost", 4242);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		    connected = true;

			listenerThread();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	

	/**
	 *  Responsible for sending Message Objects over the socket to be received by other clients.
	 * @param msg Message Object to be sent to the server/clients.
	 * @return sent Boolean value whether message was sent or not
	 */
	protected static boolean send(Object msg)
	{
		boolean sent = false;
		try
		{
			if(!socket.isClosed()) 
			{
				System.out.println("--> Sending Object:   " + msg);
				out.writeObject(msg);
				sent = true;
			}
		}
		catch (IOException e){}
		return sent;
	}
	
	/**
	 * Responsible for closing client socket connection. 
	 * @return closed: True if socket was closed. false if it wasnt.
	 */
	public static boolean closeConnection()
	{
		boolean closed = false;
		
		if (connected)
		{
			try 
			{
	        	System.out.println("[INFO] Client Disconnected...");        	
				out.close();
				in.close();		
				socket.close();
				msgReceiver.close();
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}		
		}
		connected = false;
		return closed;
		
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
        			while (connected)
        			{
        				msgReceiver.receiveIncomingMessage(in.readObject());
        			}
        		}
        		catch (IOException | ClassNotFoundException e)
        		{
        			try 
        			{
        				if(connected)
        				{
        		        	System.out.println("[INFO] Server Disconnected...");        	
        					out.close();
        					in.close();
        					socket.close();
        					msgReceiver.close();
        					connected = false;
        				}
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

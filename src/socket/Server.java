package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import messages.BaseMessage.MessageType;
import messages.Player;

/**
 * 
 * @author trae, Alayna
 * 
 * @version 1.0 (4/1/2019)
 * 
 * This class is responsible for creating and maintaining the socket server and its functionality.
 * Maintains server, keeps track of current socket connections, routes Message Objects to clients.
 *
 */
public class Server
{
	private static volatile Server instance = null;
	private ServerSocket socketServer;			// for accepting connections
	private static ArrayList<ClientHandler> clients;	// all the connections with clients
	

	/**
	 * Public Server constructor.
	 */
	private Server()
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
        	initialize();
        	
        	System.out.println("Message Receiver Started");
        	
        }
	}
	
    public static Server getInstance() 
    {
        if (instance == null) 
        { 
        	/**
        	 *  This is a thread-safe check to ensure another thread can't initialize another MessageReceiver class.
        	 */
            synchronized (Server.class) 
            {
                if (instance == null)
                {
                	instance = new Server();
                }
                
            }
        }

        return instance;
    }

	/**
	 * This method manages Client socket connections.
	 * 
	 * @functionality: Creates Server Socket.
	 * @functionality: Initializes clients data structure.
	 * @functionality: Creates Client Connection Threads.
	 * 
	 * @TODO: Change the data structure responsible for maintaining list of clients.
	 */
	private void initialize()
	{
		int index = 0;
		try
		{
			socketServer = new ServerSocket(4242);
			clients = new ArrayList<ClientHandler>();
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		while (true)
		{
			ClientHandler client = null;
			try
			{
				client = new ClientHandler(socketServer.accept());
				client.setIndex(index);
				client.setDaemon(true);
				client.start();
				addClient(client);
				index++;
				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}		
	}
	

	/**
	 * Adds Client connection thread to Data structure.
	 * 
	 * @param client: ClientHandler thread added to data structure
	 */
	private synchronized void addClient(ClientHandler client)
	{
		clients.add(client);
	}

	/**
	 * Removes the Client connection from the list of current client connections.
	 * 
	 * @param client: ClientHandler thread to be removed from data structure.
	 */
	protected synchronized static void removeClient(ClientHandler client)
	{
		clients.remove(client);
	}

	/**
	 * Sends the message from the one client handler to all the others (but not echoing back to the originator)
	 * 
	 * @param incomingClient: ClientHandler responsible for sending Message Object.
	 * @param object:		  Message Object being sent over socket to other clients.
	 * 
	 * @TODO: Add functionality to see Message Object to individual Client.
	 */
	protected static void broadcast(ClientHandler incomingClient, Object object)
	{
		for (ClientHandler outgoingClient : clients)
		{
			if (outgoingClient != incomingClient)
			{
				outgoingClient.send(object);
			}
		}
	}
	
	protected static void servercast(Object object)
	{
		for (ClientHandler outgoingClient : clients)
		{
			outgoingClient.send(object);
		}
	}
	
	protected static void unicast(ClientHandler outgoingClient, Object object, SendType type)
	{
		if( type == SendType.PLAYER_INIT)
		{
			Player player = (Player)object;	
			player.setType(MessageType.INIT);
			outgoingClient.send(player);
	
			for (ClientHandler client : clients)
			{
				Player msg = new Player(client.getPlayerName(), client.getIndex());
				msg.setType(MessageType.PLAYER_JOIN);
				if(client != outgoingClient)
				{
					outgoingClient.send(msg);
				}
			}
		}
	}
		

}

/**
 * 
 * @author trae, Alayna
 * 
 * @version 1.0 (4/1/2019)
 * 
 * This class is a thread extension class designed to monitor incoming Message Objects sent by clients on a separate thread.
 * This class is threaded to prevent blocking of the main class.
 *
 */
class ClientHandler extends Thread
{
	private Socket socket;					// each instance is in a different thread and has its own socket
	ObjectOutputStream out = null;
	ObjectInputStream in = null;
	private int playerId = -1;
	private String playerName = "";

	/**
	 * Public constructor for ClientHandler.
	 * 
	 * @param sock:	 Newly connected client socket.
	 * @param server Server class instance.
	 */
	public ClientHandler(Socket sock)
	{
		this.socket = sock;
	}
	

	/**
	 * Override thread function responsible for monitoring socket connection for incoming Message Objects.
	 */
	public void run()
	{
		try
		{
			// Communication channel
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

			Object object;
			while (true)
			{
				object = in.readObject();
				
				if( object instanceof Player )
		    	{
		    		playerInitialize(object);
		    	}
				else
				{
					//ServerMessageReceiver.receiveIncomingMessage(object);
				}
			}
		}
		catch (IOException | ClassNotFoundException e)
		{
			try 
			{
				System.out.println("Closing socket connection. When");
				Server.removeClient(this);
				out.close();
				in.close();
				ServerMessageReceiver.close();
				socket.close();
			} 
			catch (IOException e1) {}	
		}
	}
	
	private void playerInitialize(Object obj)
	{
		
		if(obj instanceof Player)
		{
			Player player = (Player)obj;
			if(player.getType() == MessageType.INIT)
			{
				setPlayerName(player.getName());
				player.setPlayerId(playerId);
				Server.unicast(this, player, SendType.PLAYER_INIT);
				
				player.setType(MessageType.PLAYER_JOIN);
				Server.broadcast(this, player);
			}
		}
	}
	
	public void setIndex(int index)
	{
		this.playerId = index;
	}
	
	public int getIndex()
	{
		return this.playerId;
	}
	
	/**
	 * Sends a message to the client
	 * @param object Message Object to be sent to clients.
	 */
	public void send(Object object)
	{
		try 
		{
			out.writeObject(object);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}


	public String getPlayerName() 
	{
		return playerName;
	}


	public void setPlayerName(String playerName) 
	{
		this.playerName = playerName;
	}
	
}

enum SendType 
{
		PLAYER_INIT, NORMAL;
}

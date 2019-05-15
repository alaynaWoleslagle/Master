package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import messages.BaseMessage.Action;
import utils.PlayerManager;
import messages.PlayerStatusMessage;

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
	private volatile ServerSocket socketServer;			// for accepting connections
	private volatile static ArrayList<ClientHandler> clients;	// all the connections with clients
	

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
        	System.out.println("[WARNING]: Attempt to create new Instance using Reflection API");
        }
        else
        {
        	System.out.println("[INFO]: Initializing Server...");        	

        	initialize();
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
        	System.out.println("[INFO]: Server Established.");        	

			
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
				client = new ClientHandler(socketServer.accept(), index);
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
	
	/**
	 * Sends message from server to all clients connected.
	 * @param object
	 */
	protected static void servercast(Object object)
	{
		for (ClientHandler outgoingClient : clients)
		{
			System.out.println("Sending Start");
			outgoingClient.send(object);
		}
	}
	
	/**
	 * Sends loop back message to client.
	 * @param outgoingClient 
	 * @param object
	 * @param type
	 */
	protected static void unicast(ClientHandler outgoingClient, Object object, SendType type)
	{
		if( type == SendType.PLAYER_INIT)
		{
			PlayerStatusMessage player = (PlayerStatusMessage)object;	
			player.setType(Action.PLAYER_INIT);
			outgoingClient.send(player);
	
			for (ClientHandler client : clients)
			{
				PlayerStatusMessage msg = new PlayerStatusMessage();
				msg.setName(client.getPlayerName());
				msg.setPlayerId(client.getIndex());
				msg.setType(Action.PLAYER_JOIN);
				
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
class ClientHandler
{
	private volatile Socket socket;					// each instance is in a different thread and has its own socket
	private volatile ObjectOutputStream out = null;
	private volatile ObjectInputStream in = null;
	private volatile int playerId = -1;
	private volatile boolean isInit = false;
	private volatile String playerName = "";
	private ServerMessageReceiver msgReceiver;

	/**
	 * Public constructor for ClientHandler.
	 * 
	 * @param sock:	 Newly connected client socket.
	 * @param server Server class instance.
	 */
	public ClientHandler(Socket sock, int index)
	{
		this.socket = sock;
		this.playerId = index;
		this.msgReceiver = ServerMessageReceiver.getInstance();
		run(this);
	}
	

	/**
	 * Returns the name of Client associated with Client Handler.
	 * @return
	 */
	public String getPlayerName() 
	{
		return playerName;
	}

	
	/**
	 * Returns the index of the Client associated with Client Handler.
	 */
	public int getIndex()
	{
		return this.playerId;
	}

	/**
	 * Override thread function responsible for monitoring socket connection for incoming Message Objects.
	 */
	public void run(ClientHandler handler)
	{
		Runnable listener = new Runnable()
    	{
            @Override
        	public void run()
        	{
            	try
        		{
        			out = new ObjectOutputStream(socket.getOutputStream());
        			in = new ObjectInputStream(socket.getInputStream());

        			Object object;
        			while (true)
        			{
        				object = in.readObject();
        				
        				if(object instanceof PlayerStatusMessage )
        		    	{
        					PlayerStatusMessage msg = (PlayerStatusMessage) object;

        					if(isInit == false && msg.getType() == Action.PLAYER_INIT)
        					{
        						playerInitialize(msg, msgReceiver.generateCardDeck(false));
        					}
        					else if(msg.getType() == Action.PLAYER_START)
        		    		{
        		    			if (msgReceiver.updatePlayerStartStatus(playerId, msg.getVarField2()))
        		    			{
        		    				initializeGame();
        		    			}
        		    		}
            				else if(msg.getType() == Action.PLAYER_SELECTION)
            				{
                				updateCharacterSelection(msg);
            				}
            				else if (msg.getType()==Action.SUGGESTION){
            					sendSuggestion(msg);
							}
        		    	}

        			}
        		}
        		catch (IOException | ClassNotFoundException e)
        		{
       			 	e.printStackTrace();

        			try 
        			{
        				System.out.println("[INFO]: Closing Server Connection...");
        				Server.removeClient(handler);
        				out.close();
        				in.close();
        				msgReceiver.close();
        				socket.close();
        			} 
        			catch (IOException e1) 
        			{
	        			 e1.printStackTrace();
        			}	
        		}
        	}
        };
        Thread clientListener = new Thread(listener);
        clientListener.setDaemon(true);
        clientListener.start();
		
	}
	
	/**
	 * Sends Player initialization messages to other players and server.
	 * @param player
	 */
	private void playerInitialize(PlayerStatusMessage player, Object[] arr)
	{
		this.playerName = player.getName();
		player.setPlayerId(playerId);
		player.setVarField3(arr);
		
		// Update Server
		ServerMessageReceiver.addPlayer(player);
		
		// Update New Player
		Server.unicast(this, player, SendType.PLAYER_INIT);
				
		// Update Other Players
		player.setType(Action.PLAYER_JOIN);
		Server.broadcast(this, player);
	}
	
	private void initializeGame()
	{
		System.out.println("STARTING GAME");
		PlayerStatusMessage msg = new PlayerStatusMessage();
		msg.setType(Action.GAME_START);
		Server.servercast(msg);
	}
	private void updateCharacterSelection(PlayerStatusMessage msg)
	{
		if(msg.getPlayerId() == this.playerId)
		{
			msgReceiver.updatePlayerCharacter(msg);
		}
		System.out.println("Made it 11");
		Server.broadcast(this, msg);
	}

	private void sendSuggestion(PlayerStatusMessage msg){
		Server.broadcast(this, msg);
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

	
}

enum SendType 
{
		PLAYER_INIT, NORMAL, SUGGESTION;
}

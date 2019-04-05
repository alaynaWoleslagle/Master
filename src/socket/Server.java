package socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
	private ArrayList<ClientHandler> clients;	// all the connections with clients
	

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
				client = new ClientHandler(socketServer.accept(), this);
				client.setDaemon(true);
				client.start();
				addClient(client);
				
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
	protected synchronized void removeClient(ClientHandler client)
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
	public synchronized void broadcast(ClientHandler incomingClient, Object object)
	{
		for (ClientHandler outgoingClient : clients)
		{
			if (outgoingClient != incomingClient)
			{
				outgoingClient.send(object);
			}
		}
	}
	
	public synchronized void servercast(Object object)
	{
		for (ClientHandler outgoingClient : clients)
		{
			outgoingClient.send(object);
		}
	}
	
	public synchronized void unicast(ClientHandler outgoingClient, Object object)
	{
		for (ClientHandler Client : clients)
		{
			if (outgoingClient == Client)
			{
				outgoingClient.send(object);
			}
		}
	}
	
	/**
	 * TODO: Create function that recognizes who should receive a message
	 */
	public synchronized void messageRouter()
	{
		
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
	private Server server;				// the main server instance
	ObjectOutputStream out = null;
	ObjectInputStream in = null;
	private boolean testMode = false;
	private String testName = "";

	/**
	 * Public constructor for ClientHandler.
	 * 
	 * @param sock:	 Newly connected client socket.
	 * @param server Server class instance.
	 */
	public ClientHandler(Socket sock, Server server)
	{
		this.socket = sock;
		this.server = server;
	}
	
	/**
	 * Public constructor for ClientHandler Testing.
	 * 
	 * @param sock:	 Newly connected client socket.
	 * @param server Server class instance.
	 */
	public ClientHandler(Socket sock, Server server, String testName)
	{
		this.socket = sock;
		this.server = server;
		this.testName = testName;
		this.testMode = true;
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

			if(testMode)
			{
				System.out.println("<Server Test Mode>: Client Connected: " + testName);
				server.unicast(this, testName);
			}

			Object object;
			while (!socket.isClosed())
			{
				object = in.readObject();
			
				server.broadcast(this, object);
			}
			

		}
		catch (IOException | ClassNotFoundException e)
		{
			try 
			{
				System.out.println("Closing socket connection.");
				server.removeClient(this);
				out.close();
				in.close();
				socket.close();
			} 
			catch (IOException e1) 
			{
			}
			
		}

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

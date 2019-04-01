package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server
{
	private ServerSocket listen;						// for accepting connections
	private ArrayList<ServerHandler> comms;	// all the connections with clients

	public Server()
	{
		try 
		{
			this.listen = new ServerSocket(4242);
			comms = new ArrayList<ServerHandler>();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * The usual loop of accepting connections and firing off new threads to handle them
	 */
	public void getConnections() 
	{
		while (true)
		{
			ServerHandler comm;
			try 
			{
				comm = new ServerHandler(listen.accept(), this);
				comm.setDaemon(true);
				comm.start();
				addCommunicator(comm);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}

		}
	}

	/**
	 * Adds the handler to the list of current client handlers
	 */
	private synchronized void addCommunicator(ServerHandler comm)
	{
		comms.add(comm);
	}

	/**
	 * Removes the handler from the list of current client handlers
	 */
	protected synchronized void removeCommunicator(ServerHandler comm)
	{
		comms.remove(comm);
	}

	/**
	 * Sends the message from the one client handler to all the others (but not echoing back to the originator)
	 */
	public synchronized void broadcast(ServerHandler from, String msg)
	{
		for (ServerHandler c : comms)
		{
			if (c != from)
			{
				c.send(msg);
			}
		}
	}


}

class ServerHandler extends Thread
{
	private Socket sock;					// each instance is in a different thread and has its own socket
	private Server server;				// the main server instance
	private String name;					// client's name (first interaction with server)
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client

	public ServerHandler(Socket sock, Server server)
	{
		this.sock = sock;
		this.server = server;
	}

	public void run()
	{
		try
		{
			System.out.println("someone connected");

			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Identify -- first message is the name
			name = in.readLine();
			System.out.println("it's "+name);
			out.println("welcome "+name);
			server.broadcast(this, name + " entered the room");

			// Chat away
			String line;
			while ((line = in.readLine()) != null)
			{
				String msg = name + ":" + line;
				System.out.println(msg);
				server.broadcast(this, msg);
			}

			// Done
			System.out.println(name + " hung up");
			server.broadcast(this, name + " left the room");

			// Clean up -- note that also remove self from server's list of handlers so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg)
	{
		out.println(msg);
	}
}

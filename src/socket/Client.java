package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client 
{
	private Scanner console;					// input from the user
	private ClientHandler comm;		// communication with the server
	private boolean closed = false;
	private Socket socket;
	
	public Client()
	{
		try
		{
			socket = new Socket("localhost", 4242);
			// For reading lines from the console
			console = new Scanner(System.in);

			// Fire off a new thread to handle incoming messages from server
			comm = new ClientHandler(socket, this);
			comm.setDaemon(true);
			comm.start();

			// Greeting; name request and response
			System.out.println("Please enter your name");
			String name = console.nextLine(); //blocks until keyboard input
			comm.send(name);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void initialize()
	{
		this.connect();
	}

	/**
	 * Get console input and send it to server;
	 * stop & clean up when client has disconnected
	 */
	private void connect()
	{
		while (!closed)
		{
			//console.nextLine() blocks until text is entered
			comm.send(console.nextLine());
		}
	}

	/**
	 * This function is called when Player chooses to leave game.
	 */
	public void exit()
	{
		closed = true;
	}
}

class ClientHandler extends Thread
{
	private Socket sock;			// the underlying socket for communication
	private Client client;		// for which this is handling communication
	private BufferedReader in;		// from server
	private PrintWriter out;		// to server

	public ClientHandler(Socket sock, Client client) throws IOException
	{
		this.sock = sock;
		this.client = client;
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new PrintWriter(sock.getOutputStream(), true);
	}

	public void send(String msg)
	{
		//called when have keyboard input
		this.out.println(msg);
	}

	public void run()
	{
		// Get lines from server; print to console
		try
		{
			String line;
			while ((line = in.readLine()) != null)
			{
				System.out.println(line);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			client.exit();
			System.out.println("server hung up");
		}

		// Clean up
		try
		{
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client 
{
	private Scanner console;		
	private boolean closed = false;
	private Socket socket;
	private BufferedReader in;		// from server
	private PrintWriter out;		// to server
	
	public Client()
	{
		try
		{
			socket = new Socket("localhost", 4242);
			console = new Scanner(System.in);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			listenerThread();


			System.out.println("Please enter your name");
			String name = console.nextLine(); //blocks until keyboard input
			send(name);
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
			send(console.nextLine());
		}
	}

	/**
	 * This function is called when Player chooses to leave game.
	 */
	private void exit()
	{
		closed = true;
	}
	
	public void send(String msg)
	{
		//called when have keyboard input
		this.out.println(msg);
	}
	
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
        			exit();
        			System.out.println("server hung up");
        		}

        		// Clean up
        		try
        		{
        			out.close();
        			in.close();
        			socket.close();
        		}
        		catch (IOException e)
        		{
        			e.printStackTrace();
        		}
        	}
            
        };
        Thread clientListener = new Thread(listener);
        clientListener.start();
	}
}


package main;

import java.util.Scanner;

import Test.Tester;
import socket.Client;
import socket.ClientMessageReceiver;
import socket.Server;
import socket.ServerMessageReceiver;
import utils.PlayerManager;

/**
 * 
 * @author Trae
 * @version 1.0 (4/8/2019)
 * 
 * This class represents the main class of the program.
 * This class is responsible for initializing Client Socket and Server Socket as well as any Singleton Objects.
 * 
 *
 */
public class PlayerMain 
{
	/**
	 * Program start
	 * @param args Command Line Arguments 
	 */
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Start Server?");
		
		int i = sc.nextInt();
		
		sc.close();
		
		if(i == 1)
		{
			initializeServer();
		}
		
		Client.getInstance();
		ClientMessageReceiver.getInstance();
		PlayerManager.getInstance();
		Tester test = new Tester();
		
		test.socketTest();
	}
	
	/**
	 * This function initializes the Server Instance.
	 * In order to continue operations on the main thread, Server instance is initialized on a 
	 * secondary thread. Elevates issues caused when a Client socket and Server socket are created in the same application
	 * @version 1.0 (4/8/2019)
	 * @author Trae
	 */
	private static void initializeServer()
	{
		// Runnable object created instead of extending Thread.
    	Runnable listener = new Runnable()
    	{
            @Override
        	public void run()
        	{
            	System.out.println("Starting Server!");
            	Server.getInstance();
            	ServerMessageReceiver.getInstance();
           
        	}
        };
        
        //NOTE: Because this thread starts the Server, Thread priority set to MAX_PRIORITY
        Thread processThread = new Thread(listener);
        processThread.setPriority(Thread.MAX_PRIORITY);
        processThread.setDaemon(true);
        processThread.start();
	}
	
}



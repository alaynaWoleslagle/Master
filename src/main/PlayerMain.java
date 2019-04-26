package main;

import java.util.Scanner;

import logic.GameProcessor;
import messages.BaseMessage.Action;
import messages.PlayerStatusMessage;
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
		
		System.out.println("Create Game?");
		
		int i = sc.nextInt();
				
		if(i == 1)
		{
			initializeServer();
		}
		
		Client.getInstance();
		GameProcessor.getInstance();
		
		PlayerManager.initializePlayer("trae");
		
		System.out.println("Ready?");
		
		i = sc.nextInt();
		sc.close();
		if(i == 1)
		{
			PlayerStatusMessage msg = new PlayerStatusMessage();
			msg.setPlayerId(PlayerManager.getPlayer().getPlayerId());
			System.out.println("TRAE: " + PlayerManager.getPlayer().getPlayerId());

			msg.setType(Action.START);
			
			PlayerManager.getPlayer().setReady(true);
			ClientMessageReceiver.sendMessage(msg);
			
			System.out.println("[INFO] Sending Player Start");
			// Do Something.
		}
		else
		{
			System.out.println("Not Ready");
		}

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
            	ServerMessageReceiver.getInstance();
            	Server.getInstance();
        	}
        };
        
        //NOTE: Because this thread starts the Server, Thread priority set to MAX_PRIORITY
        Thread processThread = new Thread(listener);
        processThread.setDaemon(true);
        processThread.start();
	}
	
}



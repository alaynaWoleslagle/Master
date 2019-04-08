package main;

import socket.Server;

public class ServerMain 
{

	/**
	 * This is a deprecated class used to run a separate Server application.
	 * The Server is now initialized through PlayerMain class through user selection.
	 *  
	 * @author Trae
	 * @version 1.0 (4/8/2019)
	 * @deprecated
	 * @param args Command line arguments
	 */
	public static void main(String[] args) 
	{
		System.out.println("waiting for connections");
		Server.getInstance();

	}

}

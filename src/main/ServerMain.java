package main;

import socket.Server;

public class ServerMain 
{

	public static void main(String[] args) 
	{
		System.out.println("waiting for connections");
		Server server = new Server();
		server.initialize();
	}

}

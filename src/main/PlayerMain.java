package main;

import Test.Tester;
import socket.Client;


public class PlayerMain 
{
	public static void main(String[] args)
	{
		Client.getInstance();
		Tester test = new Tester();
		
		test.socketTest();
	}
	
}



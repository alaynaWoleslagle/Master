package main;

import java.util.Random;
import java.util.Scanner;

import messages.Student;
import socket.Client;


public class PlayerMain 
{
	public static void main(String[] args)
	{
		socketTest();
	}
	
	/**
	 *  Simple method that tests Client socket functionality.
	 *  User enters a name from the command line.
	 *  Name is used to generate a Student Object that is sent over the socket 250 times at random intervals.
	 */
	public static void socketTest()
	{
		Client.getInstance();
		Scanner console = new Scanner(System.in);
		Random rand = new Random();
		
		System.out.println("Enter name");
		String name = console.nextLine();
		console.close();

		Student student1 = new Student(rand.nextInt(50), name);
		

		
		for (int i = 0; i < 250; i++)
		{
			Client.send(student1);
			try 
			{   
				Thread.sleep((int) ((Math.random()*((5000-2000)+1))+2000));
			} 
			catch (InterruptedException e) 
			{
				break;
			}
		}
	}
}



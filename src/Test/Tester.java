package Test;


import socket.Client;
import utils.PlayerManager;

public class Tester 
{

	public Tester()
	{}
	
	/**
	 *  Simple method that tests Client socket functionality.
	 *  User enters a name from the command line.
	 *  Name is used to generate a Student Object that is sent over the socket 250 times at random intervals.
	 */
	public void socketTest()
	{
		Client.getInstance();
		PlayerManager.getInstance();
	   // Random r = new Random();
		//int id = r.nextInt((100 - 1) + 1) + 1;

	    //Student student = new Student(id, "TEST");
		System.out.println("Made it 1");
		PlayerManager.initializePlayer("trae");

		
		/*for(int i = 0; i < 250; i++)
		{
		   try 
		   {
			   Thread.sleep(r.nextInt((1000 - 500) + 1) + 500);
		   } 
		   catch (InterruptedException e) 
		   {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }
		}*/
		
	}
}

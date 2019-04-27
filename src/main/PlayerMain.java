package main;


import gui.UserInterface;
import javafx.application.Application;



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
	    Application.launch(UserInterface.class, args);
	}	
}



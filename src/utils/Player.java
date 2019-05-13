package utils;


import java.util.ArrayList;
import javafx.scene.paint.Color;

/**
 * Player construct that holds all relevant information pertaining to a specific player.
 * 
 * @author Trae
 * @version 1.0 (4/10/19)
 *
 */
public class Player 
{

	/** name: Name of player */
	private volatile String name;

    private volatile String position;
	
	/** playerId: Unique id associated with player */
	private volatile int playerId;
	
	private volatile boolean isReady;
	
	/** TODO: This is a place holder. 
	 * Currently the player character is represented by a color on the UI.*/
	private volatile int character;

	private volatile Color color;

	private ArrayList <String> hand;
	
	/**
	 * Player Object constructor.
	 * @param name: Name of player
	 * @param id:	Unique id of player
	 */
	public Player(String name, int id)
	{
		setName(name);
		setPlayerId(id);
	}
	
	public Player(String name, Color color, int id, String position, ArrayList <String> hand)
	{
		setName(name);
		setPlayerId(id);
		setPosition(position);
		setHand(hand);
		setColor(color);

	}
	
	
	/**
	 * Default Player Object constructor.
	 */
	public Player()
	{
		
	}

	/**
	 * Returns String representation of Player Object.
	 */
	public String toString()
	{
		return "[Player] " + getName() + " : " + getPlayerId();
	}

	/**
	 * Returns the name of player.
	 * @return Player name
	 */
	public String getName() 
	{
		return name;
	}

	/**
	 * Sets the name of player.
	 * @param name
	 */
	public synchronized void setName(String name) 
	{
		this.name = name;
	}

    /**
     * Returns the position of the player
     * @return position
     */
    public String getPosition() { return position; }

    /**
     * Sets the position of the player
     * @param position
     */
    public synchronized void setPosition(String position) { this.position = position; }

	/**
	 * Returns the unique player Id for player.
	 * @return uniqueId of player.
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * Sets unique playerId of player
	 * @return color
	 */
	public synchronized void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * Returns the unique player Id for player.
	 * @return uniqueId of player.
	 */
	public int getPlayerId() 
	{
		return playerId;
	}

	/**
	 * Sets unique playerId of player
	 * @param playerId
	 */
	public synchronized void setPlayerId(int playerId) 
	{
		this.playerId = playerId;
	}

	/**
	 * Returns the hand of the player
	 * @return hand
	 */
	public ArrayList <String> getHand() { return hand; }

	/**
	 * Sets the hand of the player
	 * @param hand
	 */
	public synchronized void setHand(ArrayList <String> hand) 
	{ 
		this.hand = hand; 
	}

	/**
	 * Returns the status of game start state.
	 * @return true if player is ready to start game.
	 */
	public boolean isReady() 
	{
		return isReady;
	}

	/**
	 * Set whether or not player is ready to start game.
	 * @param isReady boolean value containing players ready state.
	 */
	public synchronized void setReady(boolean isReady) 
	{
		this.isReady = isReady;
	}

	public int getCharacter() 
	{
		return character;
	}

	public void setCharacter(int character) 
	{
		this.character = character;
	}

}

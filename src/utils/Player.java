package utils;


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
	
	/** playerId: Unique id associated with player */
	private volatile int playerId;
	
	private volatile boolean isReady;
	
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

}

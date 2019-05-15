package utils;


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
	
	private volatile PlayerCard playerCard;
	private volatile WeaponCard weaponCard;
	private volatile RoomCard roomCard;




	private volatile boolean blacklist = false;
	
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
	 * Returns whether a player is blacklisted.
	 * @return blacklist
	 */
	public boolean getBlacklist() { return blacklist; }

	/**
	 * Sets a player to blacklisted.
	 */
	public synchronized void setBlacklist()
	{
		this.blacklist = true;
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

	public synchronized int getCharacter() 
	{
		return character;
	}

	public synchronized void setCharacter(int character) 
	{
		this.character = character;
	}

	public synchronized RoomCard getRoomCard() 
	{
		return roomCard;
	}

	public synchronized void setRoomCard(RoomCard roomCard) 
	{
		this.roomCard = roomCard;
	}

	public synchronized WeaponCard getWeaponCard() 
	{
		return weaponCard;
	}

	public synchronized void setWeaponCard(WeaponCard weaponCard) 
	{
		this.weaponCard = weaponCard;
	}

	public synchronized PlayerCard getPlayerCard() 
	{
		return playerCard;
	}

	public synchronized void setPlayerCard(PlayerCard playerCard) 
	{
		this.playerCard = playerCard;
	}

	public Color getColor(){
		return color;
	}
	public void setColor(int colorNum)
	{

		if(colorNum == 0)
		{
			color = Color.DARKRED;
			position = "hallway2";
			name = "Miss Scarlet";

		}
		else if(colorNum ==1)
		{
			color = Color.YELLOW;
			position = "hallway11";
			name = "Mr. Green";
		}
		else if(colorNum==2)
		{
			color = Color.WHITE;
			position = "hallway12";
			name = "Mrs. White";
		}
		else if(colorNum==3)
		{
			color = Color.GREEN;
			position = "hallway8";
			name = "Mrs. Peacock";

		}
		else if(colorNum==4)
		{
			color = Color.CADETBLUE;
			position = "hallway3";
			name = "Prof. Plum";
		}
		else if(colorNum==5)
		{
			color = Color.PLUM;
			position = "hallway5";
			name = "Col. Mustard";
		}
		else
		{
			color = null;
			position = null;
			name = null;
		}
	}

}

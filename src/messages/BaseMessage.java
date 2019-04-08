package messages;

import java.io.Serializable;

public class BaseMessage implements Serializable
{
	/**
	 * This Abstract class is the Base class for which all message objects extend from.
	 * This class has no Constructor.
	 * 
	 * @author Trae
	 * @version 1.0 (4/8/2019)
	 */
	private static final long serialVersionUID = -7224412092815499945L;


	/**
	 * Enum used to specify what type of message is being sent over Socket connection to allow for separate processing 
	 * for different message types. A message class can share multiple Message Types.
	 * @author Trae
	 * 
	 * INIT: 			Player initialization Message. Loop back message from Server used to initialize player and set unique 'playerId'.
	 * PLAYER_JOIN: 	Player Join Message. Status Message used notify clients that a new player has joined the game.
	 *
	 */
	public enum MessageType 
	{ 
	    INIT, PLAYER_JOIN; 
	} 
	
	/**
	 * type: Message Type
	 *       Message Type SHALL be set by the Client who is sending the Message.
	 *       
	 */
	private MessageType type = MessageType.INIT;
	
	/**
	 *  name: Player Name
	 *  	  Player name SHALL be set during Player Initialization through USER input.
	 *  	  No uniqueness requirements for 'name' due to playerId being unique value.
	 */
	public String name = "";
	
	/** playerId: Unique Player Id. 
	 * 			  Player Id SHALL be set by Server being that Server is 
	 *            responsible for Maintaining Official Player list.
	 */
	private int playerId = -1;
	

	/**
	 * Returns Player name.
	 * @return  Player name
	 */
	public String getName() 
	{
		return name;
	}


	/**
	 * Sets player name.
	 * @param name: Player name
	 */
	public void setName(String name) 
	{
		this.name = name;
	}


	/**
	 * Gets Message Type.
	 * @return Message Type
	 */
	public MessageType getType() 
	{
		return type;
	}

	/**
	 * Sets Message Type.
	 * @param type: Message Type
	 */
	public void setType(MessageType type) 
	{
		this.type = type;
	}


	/**
	 * Gets Player Id.
	 * @return Player Id
	 */
	public int getPlayerId() 
	{
		return playerId;
	}

	/**
	 * Sets Player Id.
	 * @param playerId: Player Id
	 */
	public void setPlayerId(int playerId) 
	{
		this.playerId = playerId;
	}
	

}

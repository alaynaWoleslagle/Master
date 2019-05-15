package messages;

import java.io.Serializable;

public abstract class BaseMessage implements Serializable
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
		UI_UPDATE
	} 
	
	public enum Action
	{
		PLAYER_INIT, PLAYER_JOIN, PLAYER_START, PLAYER_SELECTION, TURN, GAME_START, SUGGESTION;
	}
	
	/**
	 * type: Message Type
	 *       Message Type SHALL be set by the Client who is sending the Message.
	 *       
	 */
	private Action type;
	
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
	 * varField1: Variable Field 1.
	 * 			  This field is a multi-purpose field intended to be used for the purpose
	 * 			  of setting sending an interger value over the socket. 
	 * NOTE: It is recommended to be used only when there is a specified Action and a specific function to 
	 * 		 handle it. This field should only have ONE purpuse per Action in order to prevent improperly handling
	 *       a message.
	 */
	private int varField1 = -1;
	
	private boolean varField2 = false;
	
	private Object[] varField3;

	private String position;
	
	private MessageType messageType;




	public String getPosition(){return position;}

	public void setPosition(String position){this.position = position;}
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
	public Action getType() 
	{
		return type;
	}

	/**
	 * Sets Message Type.
	 * @param type: Message Type
	 */
	public void setType(Action type) 
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


	/**
	 * Get VarField1
	 * @return VarField1
	 */
	public int getVarField1() 
	{
		return varField1;
	}

	/**
	 * Sets varField1
	 * @param varField1
	 */
	public void setVarField1(int varField1) 
	{
		this.varField1 = varField1;
	}


	public MessageType getMessageType() 
	{
		return messageType;
	}


	public void setMessageType(MessageType messageType) 
	{
		this.messageType = messageType;
	}


	/**
	 * Get VarField2
	 * @return
	 */
	public boolean getVarField2() 
	{
		return varField2;
	}


	/**
	 * Set varField2
	 * @param varField2
	 */
	public void setVarField2(boolean varField2) 
	{
		this.varField2 = varField2;
	}


	public Object[] getVarField3() 
	{
		return varField3;
	}


	public void setVarField3(Object[] varField3) 
	{
		this.varField3 = varField3;
	}
	
	
	

}

package messages;

import java.io.Serializable;

/**
 * /**
 * 
 * @author Trae
 * 
 * @version 1.0 (4/5/2019)
 *
 * Player Message.
 * 
 * .
 */
public class PlayerStatusMessage extends BaseMessage implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5206627738168945498L;
	
	public PlayerStatusMessage(String name, int id)
	{
		setName(name);
		setPlayerId(id);
	}
	
	public PlayerStatusMessage(String name)
	{
		setName(name);
	}
	
	public PlayerStatusMessage()
	{
		
	}

	
	public String toString()
	{
		return "[Player] " + getName() + " : " + getPlayerId();
	}
}

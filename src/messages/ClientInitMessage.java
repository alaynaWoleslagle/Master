package messages;

import java.io.Serializable;

/**
 * 
 * @author Trae
 * 
 * @version 1.0 (4/5/2019)
 *
 * Client Initiation Message.
 * 
 * This message is sent to Server to generate a new Player object for the new client.
 */
public class ClientInitMessage extends BaseMessage implements Serializable
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3255838498603119265L;

	/**
	 *  Public Constructor for ClientInitMessage.
	 * @param name Name of Player.
	 */
	public ClientInitMessage(String name)
	{
		this.setName(name);
		this.setType(MessageType.INIT);
	}

	/**
	 *  Public toString Override function.
	 *  @TODO: Move method to BaseMessage class.
	 */ 
	public String toString()
	{
		return "[Init] " + getName() + " : " + getPlayerId();
	}

}

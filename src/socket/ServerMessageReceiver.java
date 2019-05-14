package socket;



import java.util.*;
import java.util.Map.Entry;

import gui.Room;
import messages.BaseMessage.Action;
import messages.PlayerStatusMessage;
import utils.Player;
import utils.PlayerCard;
import utils.RoomCard;
import utils.WeaponCard;

/**
 * Singleton Class
 * This class is responsible for receiving Messages from the Queue.
 * Messages are added to a Queue which is will then be processed on a separate thread.
 * 
 * This class acts as an interface between the Server socket and Client Socket through the receipt of messages.
 * 
 * @author Trae
 *
 * @version 1.0 (4/5/2019)
 * 
 */
public class ServerMessageReceiver extends MessageReceiver
{
    private static volatile ServerMessageReceiver msgReceiver = null;
    private static volatile Map<Integer,Player> playerTruth = null;
    private static volatile Object[] solutionDeck = null;
    
    /**
     * These sets hold all unselectable cards
     */
    private volatile Set<RoomCard> room = null;
    private volatile Set<WeaponCard> weapon = null;
    private volatile Set<PlayerCard> player = null;
	private static int currentTurn = 0;
	private static int disproveTurn;
	private static ArrayList<String> currentSuggestion = new ArrayList<>();


	
	private ServerMessageReceiver()
	{
        /**
         *  This checks to ensure no other instance is created using Reflection API
         */
        if (msgReceiver != null)
        {
        	System.out.println("[WARNING]: Attempt to create new Instance using Reflection API");
        }
        else
        {
        	if(initialize())
        	{
        		playerTruth = new HashMap<Integer, Player>();
        		room = new HashSet<RoomCard>();
        		weapon = new HashSet<WeaponCard>();
        		player = new HashSet<PlayerCard>();
        		solutionDeck = new Object[3];
        		
        		Random rnd = new Random();

            	solutionDeck[0] = generateRoom(rnd);
        		solutionDeck[1] = generatePlayer(rnd);
        		solutionDeck[2] = generateWeapon(rnd);
        		
        	}
        }
	}
	
    public static ServerMessageReceiver getInstance() 
    {
        if (msgReceiver == null) 
        { 
        	/**
        	 *  This is a thread-safe check to ensure another thread can't initialize another MessageReceiver class.
        	 */
            synchronized (ServerMessageReceiver.class) 
            {
                if (msgReceiver == null)
                {
                	msgReceiver = new ServerMessageReceiver();
                }
                
            }
        }

        return msgReceiver;
    }
    
	
    /**
     * This function updates the player start status 
     * @param id
     * @param isReady
     * @return true: is all players in game are ready
     */
	protected synchronized boolean updatePlayerStartStatus(int id, boolean isReady)
	{
		if(playerTruth.containsKey(id))
		{
			playerTruth.computeIfPresent(id, (k, c) -> updateStart(c, isReady) );
		}
		
		return allPlayersReady();
	}
    
    private Player updateStart(Player player, boolean ready)
    {
    	player.setReady(ready);
		return player;
    }
    
    
    /**
     * TODO: This function routes incoming messages to for main processing.
     * TODO: Game logic function should be called here. Function that receives Message and processes it.
     * @param msg
     */
    protected synchronized void processIncomingMessage(Object object)
    {
    	if( object instanceof PlayerStatusMessage )
    	{
    		PlayerStatusMessage msg = (PlayerStatusMessage) object;
    		
    		if(msg.getType() == Action.PLAYER_START && playerTruth.containsKey(msg.getPlayerId()))
    		{
    			playerTruth.computeIfPresent(msg.getPlayerId(), (k, c) -> updateStart(c, true) );
    		}
    		else
    		{
    			System.out.println("No player in game");
    		}
    	}
    }
    
    protected synchronized Object[] generateCardDeck(boolean isTruth)
    {
    	Object[] arr = new Object[3];
		Random rnd = new Random();

		RoomCard roomCard = generateRoom(rnd);
		PlayerCard playerCard = generatePlayer(rnd);
		WeaponCard weaponCard = generateWeapon(rnd);
		
		arr[0] = roomCard;
		arr[1] = playerCard;
		arr[2] = weaponCard;
		
		return arr;
    }
    
    private synchronized RoomCard generateRoom(Random rnd)
    {
		RoomCard roomCard = RoomCard.values()[rnd.nextInt(RoomCard.values().length)];
		
		while(room.contains(roomCard))
		{
			roomCard = RoomCard.values()[rnd.nextInt(RoomCard.values().length)];
		}
		room.add(roomCard);
		
		return roomCard;
		
    }
    
    private synchronized PlayerCard generatePlayer(Random rnd)
    {
		PlayerCard playerCard = PlayerCard.values()[rnd.nextInt(PlayerCard.values().length)];
		
		while(player.contains(playerCard))
		{
			playerCard = PlayerCard.values()[rnd.nextInt(PlayerCard.values().length)];
		}
		player.add(playerCard);
		
		return playerCard;
    }
    
    private synchronized WeaponCard generateWeapon(Random rnd)
    {
    	WeaponCard weaponCard = WeaponCard.values()[rnd.nextInt(PlayerCard.values().length)];
    	
    	while(weapon.contains(weaponCard))
    	{
    		weaponCard = WeaponCard.values()[rnd.nextInt(PlayerCard.values().length)];
    	}
    	weapon.add(weaponCard);

    	return weaponCard;
    }

	protected synchronized static void addPlayer(PlayerStatusMessage object) 
	{
		Player player = new Player();
		player.setName(object.getName());
		player.setPlayerId(object.getPlayerId());
		
		playerTruth.put(object.getPlayerId(), player);
		System.out.println("[INFO]: Player added to game. " + player.getName());
	}
	
	protected synchronized void updatePlayerCharacter(PlayerStatusMessage object)
	{
		playerTruth.computeIfPresent(object.getPlayerId(), (k,c) -> updateCharacter(c, object.getVarField1()));
	}
    
    private Player updateCharacter(Player player, int color)
    {
    	player.setCharacter(color);
    	return player;
    }
    
    /**
     * Returns true if all players are in ready status
     * @return
     */
    private synchronized boolean allPlayersReady()
    {
        for (Entry<Integer, Player> entry : playerTruth.entrySet())
        { 
        	if(entry.getValue().isReady() == false)
        	{
        		return false;
        	}
        }
        return true;
    }

	public static void nextTurn() {
		if(currentTurn==5)

		{
			currentTurn = 0;
		}
		else

		{
			currentTurn = currentTurn + 1;
		}
	}

	public static void setDisproveTurn(int turn){
		disproveTurn= turn+1;
	}
	public static int getDisproveTurn(){
		return disproveTurn;
	}
	public static void nextDisproveTurn(){
		if(disproveTurn==5)

		{
			disproveTurn = 0;
		}
		else

		{
			disproveTurn = disproveTurn + 1;
		}
	}



	public static int getTurn(){
    	return currentTurn;
	}

    public static boolean validateAccusation(String room, String player, String weapon){
		RoomCard solutionRoom = (RoomCard) solutionDeck[0];
		PlayerCard solutionPlayer = (PlayerCard) solutionDeck[1];
		WeaponCard solutionWeapon = (WeaponCard) solutionDeck[2];
    	if (room.equals(solutionRoom.getValue()) & player.equals(solutionPlayer.getValue())& weapon.equals(solutionWeapon.getValue())){
    		return true;
		}
		else{
			return false;
		}
	}

	public static void setSuggestion (ArrayList <String> suggestion){
    	currentSuggestion = suggestion;
	}
	public static ArrayList<String> getSuggestion (){
    	return currentSuggestion;
	}


}

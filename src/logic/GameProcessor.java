package logic;

import messages.GameLogicMessage;
import messages.PlayerStatusMessage;
import gui.LobbyScreen;
import gui.GameScreen;
import gui.UserInterface;
import javafx.application.Platform;
import javafx.scene.Scene;
import messages.BaseMessage.Action;
import messages.BaseMessage.MessageType;
import socket.Client;
import socket.ClientMessageReceiver;
import socket.Server;
import socket.ServerMessageReceiver;
import utils.*;


import java.util.*;
import java.util.HashMap;
import javafx.scene.paint.Color;

public class GameProcessor 
{
	private static volatile GameProcessor instance = null;

	private static String[][] board;

	private static Map<Integer, Player> players = new HashMap<Integer, Player>();
	private static Map<String, Integer> playerIds = new HashMap<>();
	private static Map<String, String[]> possibleMoves = new HashMap<String, String[]>();
	private static Map<String, String[]> rightBelowHallways = new HashMap<String, String[]>();



	private static ArrayList<String> suggestion = new ArrayList<>();
	private String[] cards;


	public static boolean winner;

	private static LobbyScreen lobbyScreen = null;
	private static GameScreen gameScreen = null;
    //private static Scene lobbyScene = null;
    private static UserInterface userInterface;
    private static int turn = 0;
    private static int disprovingTurn;
	
    public static GameProcessor getInstance()
    {
        if (instance == null) 
        { 
        	/**
        	 *  This is a thread-safe check to ensure another thread can't initialize another MessageReceiver class.
        	 */
            synchronized (GameProcessor.class) 
            {
                if (instance == null)
                {
                	instance = new GameProcessor();
                }
                
            }
        }

		return instance;
	}

	private GameProcessor() 
	{
		/**
		 *  This checks to ensure no other instance is created using Reflection API
		 */
		if (instance != null) 
		{
			System.out.println("Exception Handled: Attempt to create new Instance using Reflection API");
		} else 
		{


			/**
			 * creates a basic board to use for logic testing
			 */
			possibleMoves.put("study", new String[]{"hallway1", "hallway3", "kitchen"});
			possibleMoves.put("hallway1", new String[]{"study", "hall"});
			possibleMoves.put("hall", new String[]{"hallway2", "hallway4", "hallway1"});
			possibleMoves.put("hallway2", new String[]{"hall", "lounge"});
			possibleMoves.put("lounge", new String[]{"hallway2", "hallway5", "conservatory"});
			possibleMoves.put("hallway3", new String[]{"study", "library"});
			possibleMoves.put("hallway4", new String[]{"hall", "billiard_room"});
			possibleMoves.put("hallway5", new String[]{"lounge", "dining_room"});
			possibleMoves.put("library", new String[]{"hallway3", "hallway8", "hallway6"});
			possibleMoves.put("hallway6", new String[]{"library", "billiard_room"});
			possibleMoves.put("billiard_room", new String[]{"hallway6", "hallway4", "hallway7", "hallway9"});
			possibleMoves.put("hallway7", new String[]{"billiard_room", "dining_room"});
			possibleMoves.put("dining_room", new String[]{"hallway7", "hallway5", "hallway10"});
			possibleMoves.put("hallway8", new String[]{"library", "conservatory"});
			possibleMoves.put("hallway9", new String[]{"billiard_room", "ballroom"});
			possibleMoves.put("hallway10", new String[]{"dining_room", "kitchen"});
			possibleMoves.put("conservatory", new String[]{"hallway8", "hallway11", "lounge"});
			possibleMoves.put("hallway11", new String[]{"conservatory", "ballroom"});
			possibleMoves.put("ballroom", new String[]{"hallway11", "hallway9", "hallway12"});
			possibleMoves.put("hallway12", new String[]{"ballroom", "kitchen"});
			possibleMoves.put("kitchen", new String[]{"hallway12", "hallway10", "study"});

			rightBelowHallways.put("study", new String[]{"hallway1", "hallway3"});
			rightBelowHallways.put("hall", new String[]{"hallway2", "hallway4"});
			rightBelowHallways.put("lounge", new String[]{"", "hallway5"});
			rightBelowHallways.put("library", new String[]{"hallway6", "hallway8"});
			rightBelowHallways.put("billiard_room", new String[]{"hallway7", "hallway9"});
			rightBelowHallways.put("dining_room", new String[]{"", "hallway10"});
			rightBelowHallways.put("conservatory", new String[]{"hallway11", ""});
			rightBelowHallways.put("ballroom", new String[]{"hallway12", ""});
			rightBelowHallways.put("kitchen", new String[]{"", ""});

		}

	}

	public static void setNextTurn(){
		ServerMessageReceiver.getInstance();
		ServerMessageReceiver.nextTurn();
	}


    public static Object [] handleRoomMove(String room){
		ServerMessageReceiver.getInstance();
		turn = ServerMessageReceiver.getTurn();
    	Object[] returnVal = new Object[3];
		Player currentPlayer = players.get(turn);
        String currentLocation = currentPlayer.getPosition();
		String playerName = currentPlayer.getName();
		Color playerColor = currentPlayer.getColor();
		returnVal[0] = playerName;
		returnVal[1] = playerColor;
		returnVal[2] = false;
        String[] availableMoves = possibleMoves.get(currentLocation);
        ServerMessageReceiver.nextTurn();

        if (Arrays.asList(availableMoves).contains(room)){
			returnVal[2] = true;
		}
		return returnVal;
    }
    public static Object [] handleRightMove(String room)
    {
		ServerMessageReceiver.getInstance();
		turn = ServerMessageReceiver.getTurn();
		Object[] returnVal = new Object[3];
        String hallway = rightBelowHallways.get(room)[0];
		Player currentPlayer = players.get(turn);
        String currentLocation = currentPlayer.getPosition();
        String playerName = currentPlayer.getName();
        Color playerColor = currentPlayer.getColor();
        returnVal[0] = playerName;
        returnVal[1] = playerColor;
        returnVal[2] = false;
        String[] availableMoves = possibleMoves.get(currentLocation);
        ServerMessageReceiver.nextTurn();
        for (int i=0; i<6; i++)
        {
            Player checkPlayer = players.get(i);
            if(checkPlayer.getPosition().equals(hallway))
            {
                return returnVal;
            }
        }
        if(Arrays.asList(availableMoves).contains(hallway))
        {
        	returnVal[2] = true;
		}
        return returnVal;
    }

    public static Object [] handleBelowMove(String room){
		ServerMessageReceiver.getInstance();
		turn = ServerMessageReceiver.getTurn();
		Object[] returnVal = new Object[3];
        String hallway = rightBelowHallways.get(room)[1];
		Player currentPlayer = players.get(turn);
        String currentLocation = currentPlayer.getPosition();
		String playerName = currentPlayer.getName();
		Color playerColor = currentPlayer.getColor();
		returnVal[0] = playerName;
		returnVal[1] = playerColor;
		returnVal[2] = false;
        String[] availableMoves = possibleMoves.get(currentLocation);
        ServerMessageReceiver.nextTurn();
        for (int i=0; i<6; i++){
            Player checkPlayer = players.get(i);
            if(checkPlayer.getPosition().equals(hallway)){
                return returnVal;
            }
        }
		if(Arrays.asList(availableMoves).contains(hallway)){
			returnVal[2] = true;
		}
		return returnVal;
    }


    public static void submitSuggestion(String character, String weapon, String room){
		ServerMessageReceiver.getInstance();
		turn = ServerMessageReceiver.getTurn();
    	Player currentPlayer = players.get(turn);

        if (room.equals(currentPlayer.getPosition())){
			int suggestedPlayerId = playerIds.get(character);
			Player suggestedPlayer = players.get(suggestedPlayerId);
			suggestedPlayer.setPosition(room);
			suggestion.add(character);
			suggestion.add(weapon);
			suggestion.add(room);
			ServerMessageReceiver.setSuggestion(suggestion);
        }
    }

    public static boolean HandDisproves(int currentDisproverId){
    	Player currentDisprover = players.get(currentDisproverId);
		PlayerCard playerCard = currentDisprover.getPlayerCard();
		WeaponCard weaponCard = currentDisprover.getWeaponCard();
		RoomCard roomCard = currentDisprover.getRoomCard();
		suggestion = ServerMessageReceiver.getSuggestion();
		if(suggestion.contains(playerCard.getValue()) || suggestion.contains(weaponCard.getValue()) || suggestion.contains(roomCard.getValue())){
			return true;
		}
		return false;
	}

	public static void skipToDisprovingPlayer(){
		ServerMessageReceiver.getInstance();
		turn = ServerMessageReceiver.getTurn();
    	ServerMessageReceiver.setDisproveTurn(turn);
    	while(ServerMessageReceiver.getDisproveTurn()!=turn){
    		if(HandDisproves(disprovingTurn)){
    			break;
			}
			ServerMessageReceiver.nextDisproveTurn();
		}
	}

    public static void disproveSuggestion(String card){
		if(suggestion.contains(card)){
			suggestion.clear();
		}
	}

    public static void submitAccusation(String character, String weapon, String room){
		ServerMessageReceiver.getInstance();
		turn = ServerMessageReceiver.getTurn();
        Player currentPlayer = players.get(turn);
		ServerMessageReceiver.getInstance();
        if(ServerMessageReceiver.validateAccusation(character, weapon, room)){
            winner = true;
        }
        else{
        	currentPlayer.setBlacklist();
        }
    }

    public static boolean getWinner(){
    	return winner;
	}

    public static boolean playerBlacklisted(){
		ServerMessageReceiver.getInstance();
		turn = ServerMessageReceiver.getTurn();
		Player currentPlayer = players.get(turn);
		ServerMessageReceiver.nextTurn();
		return currentPlayer.getBlacklist();
	}

    public static void  processMessage(Object obj)
    {
    	if( obj instanceof PlayerStatusMessage )
    	{
        	PlayerStatusMessage msg = (PlayerStatusMessage)obj;
        	System.out.println(msg.getType());
        	Player tmp = new Player();
        	tmp.setName(msg.getName());
        	tmp.setPlayerId(msg.getPlayerId());
    		
        	if(msg.getType() == Action.PLAYER_JOIN)
    		{
    			Player player = new Player(msg.getName(), msg.getPlayerId());
    			PlayerManager.addNewPlayer(player);
    			System.out.println("[NEW PLAYER]: Total Player Count: " + PlayerManager.playerCount());
    		}
    		else if(msg.getType() == Action.PLAYER_SELECTION)
    		{
    			if (lobbyScreen != null)
    			{
    				playerSelection(msg);
    			}
    		}
    		else if(msg.getType() == Action.GAME_START)
    		{
    			System.out.println("Creating Game Scene");
    			if (lobbyScreen != null)
    			{	
    				createGame();
    			}
    		}
    	}
        else if(obj instanceof GameLogicMessage)
		{
            GameLogicMessage logicMsg = (GameLogicMessage)obj;
            if(logicMsg.getType() == Action.TURN)
			{

            }

        }
    }
    
    public static void initializeClientServer(String name, int port, boolean newgame, UserInterface ui)
    {
    	// TODO: Allow New game option to select port. Currently port not used.
    	userInterface = ui;
    	/**
    	 * If newgame is TRUE, initialize Game Server.
    	 */
    	if(newgame)
    	{
    		Runnable listener = new Runnable()
    		{
    			@Override
    			public void run()
    			{
    				ServerMessageReceiver.getInstance();
    				Server.getInstance();
    			}
    		};
    		
    		Thread processThread = new Thread(listener);
    		processThread.setDaemon(true);
    		processThread.start();
    	}
    	
    	createClient(name);
    	
		try 
	   	{
		Thread.sleep(100);
	   	} 
	   	catch (InterruptedException e) 
	   	{
	   		// TODO Auto-generated catch block
	   		e.printStackTrace();
	   	}
		createLobby();    
		
    }
    
	private static void createClient(String name)
	{
		Client.getInstance();
		PlayerManager.initializePlayer(name);
	}
	
	private static void createLobby()
	{
		lobbyScreen = new LobbyScreen(PlayerManager.getPlayer().getPlayerId(), PlayerManager.getPlayer().getName());
		Scene lobbyScene = lobbyScreen.createScene();
		userInterface.setScene(lobbyScene);
	}
	
	/**
	 * This function is responsible for generating the Game screen
	 */
	private static void createGame()
	{
		Platform.runLater(
				new Runnable()
				{
					@Override 
					public void run() 
					{
						gameScreen = new GameScreen();
						Scene gameScene = gameScreen.createScene();
						userInterface.setScene(gameScene);
					}
				});
	}

	/**
	 * This function handles the player character selection. 
	 * When a player selects a character, a function call is made to ViewController with
	 * the intended purpose of sending the player character selection over the socket connection
	 * to the other game players.
	 * @param playerId
	 * @param color
	 */
	public static void registerCharacterSelection(int playerId, int color)
	{
		PlayerManager.getPlayer().setCharacter(color);
		
		PlayerStatusMessage msg = new PlayerStatusMessage();
		msg.setPlayerId(playerId);
		msg.setVarField1(color);
		msg.setType(Action.PLAYER_SELECTION);
		msg.setMessageType(MessageType.UI_UPDATE);
		
		ClientMessageReceiver.sendMessage(msg);
	}
	
	
	public static void playerSelection(PlayerStatusMessage msg)
	{
		Player player = PlayerManager.getOtherPlayer(msg.getPlayerId());
		if (player == null)
		{
			System.out.println("Made it 29");
		}
		
		System.out.println(player);
		System.out.println("Color:" + msg.getVarField1());

		lobbyScreen.addPlayer(player.getName(), false, player.getPlayerId(), msg.getVarField1());
	}
	
	public static void updatePlayerReadyStatus(int playerId, boolean isReady)
	{
		if (PlayerManager.updateReadyStatus(isReady))
		{
			PlayerStatusMessage msg = new PlayerStatusMessage();
			
			msg.setPlayerId(playerId);
			msg.setVarField2(isReady);
			msg.setType(Action.PLAYER_START);
			msg.setMessageType(MessageType.UI_UPDATE);
			
			ClientMessageReceiver.sendMessage(msg);
		}
	}


}

package logic;

import messages.GameLogicMessage;
import messages.PlayerStatusMessage;
import gui.LobbyScreen;
import gui.UserInterface;
import javafx.scene.Scene;
import messages.BaseMessage.Action;
import socket.Client;
import socket.ClientMessageReceiver;
import socket.Server;
import socket.ServerMessageReceiver;
import utils.Player;
import utils.PlayerManager;


import java.util.*;
import java.util.HashMap;

public class GameProcessor {
	private static volatile GameProcessor instance = null;

	private static String[][] board;

	private static Map<Integer, Player> players = new HashMap<Integer, Player>();
	private static Map<String, String> hallways = new HashMap<String, String>();
	private static Map<String, String[]> possibleMoves = new HashMap<String, String[]>();
	private static Map<String, String[]> rightBelowHallways = new HashMap<String, String[]>();

	private ArrayList<String> cards = new ArrayList<>();
	private ArrayList<String> roomCards = new ArrayList<>();
	private ArrayList<String> weaponCards = new ArrayList<>();
	private ArrayList<String> playerCards = new ArrayList<>();

	private static ArrayList<String> solution = new ArrayList<>();

	private ArrayList<String> hand1 = new ArrayList<>();
	private ArrayList<String> hand2 = new ArrayList<>();
	private ArrayList<String> hand3 = new ArrayList<>();
	private ArrayList<String> hand4 = new ArrayList<>();
	private ArrayList<String> hand5 = new ArrayList<>();
	private ArrayList<String> hand6 = new ArrayList<>();
	private ArrayList<ArrayList<String>> hands = new ArrayList<>();

	private static ArrayList<Integer> blacklist = new ArrayList<>();

	private static boolean winner;

	private static LobbyScreen lobby = null;
	private static Scene lobbyScene = null;
	private static UserInterface userInterface;

	private static int turn = 0;

	public static GameProcessor getInstance() {
		if (instance == null) {
			/**
			 *  This is a thread-safe check to ensure another thread can't initialize another MessageReceiver class.
			 */
			synchronized (GameProcessor.class) {
				if (instance == null) {
					instance = new GameProcessor();
				}

			}
		}

		return instance;
	}

	private GameProcessor() {
		/**
		 *  This checks to ensure no other instance is created using Reflection API
		 */
		if (instance != null) {
			System.out.println("Exception Handled: Attempt to create new Instance using Reflection API");
		} else {

			winner = false;

			/**
			 * creates the deck of cards and shuffles them
			 */
			roomCards.addAll(Arrays.asList("study", "hall", "lounge", "library", "billiard_room", "dining_room", "conservatory", "ball_room", "kitchen"));
			weaponCards.addAll(Arrays.asList("candlestick", "knife", "pipe", "gun", "rope", "wrench"));
			playerCards.addAll(Arrays.asList("mustard", "scarlet", "plum", "green", "white", "peacock"));
			Collections.shuffle(roomCards);
			Collections.shuffle(weaponCards);
			Collections.shuffle(playerCards);

			/**
			 * allocates three cards in the deck to the solution
			 */

			solution.add(roomCards.get(0));
			solution.add(playerCards.get(0));
			solution.add(weaponCards.get(0));
			roomCards.remove(0);
			playerCards.remove(0);
			weaponCards.remove(0);

			/**
			 * shuffles the rest of the cards together
			 */
			cards.addAll(roomCards);
			cards.addAll(playerCards);
			cards.addAll(weaponCards);
			Collections.shuffle(cards);

			/**
			 * deals the rest of the cards to the players
			 */
			hands.addAll(Arrays.asList(hand1, hand2, hand3, hand4, hand5, hand6));
			for (int i = 0; i < 6; i++) {
				ArrayList<String> currentHand = hands.get(i);
				for (int j = 0; j < 3; j++) {
					currentHand.add(cards.get(0));
					cards.remove(0);
				}
			}

			/**
			 * sets each hallway to empty at the start, because each person starts in a room
			 */
			hallways.put("hallway1", "empty");
			hallways.put("hallway2", "empty");
			hallways.put("hallway3", "empty");
			hallways.put("hallway4", "empty");
			hallways.put("hallway5", "empty");
			hallways.put("hallway6", "empty");
			hallways.put("hallway7", "empty");
			hallways.put("hallway8", "empty");
			hallways.put("hallway9", "empty");
			hallways.put("hallway10", "empty");
			hallways.put("hallway11", "empty");
			hallways.put("hallway12", "empty");

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



			/**
			 *creates each player, gives them an id and a starting position, and puts them in a hashmap
			 */
			Player player1 = new Player("mustard", 0, "study", hands.get(0));
			Player player2 = new Player("scarlet", 1, "loounge", hands.get(0));
			Player player3 = new Player("plum", 2, "conservatory", hands.get(0));
			Player player4 = new Player("green", 3, "kitchen", hands.get(0));
			Player player5 = new Player("white", 4, "library", hands.get(0));
			Player player6 = new Player("peacock", 5, "dining_room", hands.get(0));
			players.put(0, player1);
			players.put(1, player2);
			players.put(2, player3);
			players.put(3, player4);
			players.put(4, player5);
			players.put(5, player6);
		}

	}

	public static int nextTurn(int turn) {
		if(turn==6)

		{
			return 0;
		}
		else

		{
			return turn + 1;
		}
	}



    public static boolean handleRoomMove(String room){
        Player currentPlayer = players.get(turn);
        String currentLocation = currentPlayer.getPosition();
        String[] availableMoves = possibleMoves.get(currentLocation);
        turn = nextTurn(turn);

        return Arrays.asList(availableMoves).contains(room);
    }
    public static boolean handleRightMove(String room){
        String hallway = rightBelowHallways.get(room)[0];
		Player currentPlayer = players.get(turn);
        String currentLocation = currentPlayer.getPosition();
        String[] availableMoves = possibleMoves.get(currentLocation);
        turn = nextTurn(turn);
        for (int i=0; i<6; i++){
            Player checkPlayer = players.get(i);
            if(checkPlayer.getPosition().equals(hallway)){
                return false;
            }
        }
        return Arrays.asList(availableMoves).contains(hallway);
    }

    public static boolean handleBelowMove(String room){
        String hallway = rightBelowHallways.get(room)[1];
		Player currentPlayer = players.get(turn);
        String currentLocation = currentPlayer.getPosition();
        String[] availableMoves = possibleMoves.get(currentLocation);
        turn = nextTurn(turn);
        for (int i=0; i<6; i++){
            Player checkPlayer = players.get(i);
            if(checkPlayer.getPosition().equals(hallway)){
                return false;
            }
        }
        return Arrays.asList(availableMoves).contains(hallway);
    }

    public static String submitSuggestion(String character, String weapon, String room){
        Player currentPlayer = players.get(turn);
        if (room.equals(currentPlayer.getPosition())){
            for (int i=0; i<6; i++){
                Player checkPlayer = players.get(i);
                ArrayList<String> checkHand = checkPlayer.getHand();
                if(checkHand.contains(character)){
                    return character;
                }
                else if(checkHand.contains(weapon)){
                    return weapon;
                }
                else if (checkHand.contains(room)){
                    return room;
                }
                else{
                    return "";
                }
            }
        }
        return "cannot suggest from here";
    }

    public static boolean submitAccusation(String character, String weapon, String room){
        Player currentPlayer = players.get(turn);
        if(solution.contains(character) && solution.contains(weapon) && solution.contains(room)){
            return true;
        }
        else{
            return false;
        }
    }

    public static void  processMessage(Object msg)
    {
    	if( msg instanceof PlayerStatusMessage )
    	{
        	PlayerStatusMessage tmpMsg = (PlayerStatusMessage)msg;
        	Player tmp = new Player();
        	tmp.setName(tmpMsg.getName());
        	tmp.setPlayerId(tmpMsg.getPlayerId());
        	
    		if(tmpMsg.getType() == Action.INIT)
    		{
    			PlayerManager.setPlayer(tmp);
    		}
    		else if(tmpMsg.getType() == Action.PLAYER_JOIN)
    		{
        		PlayerManager.addNewPlayer(tmp);
    		}

			if (lobby != null)
			{
				playerSelection(tmpMsg);
			}
    	}
        else if(msg instanceof GameLogicMessage)
		{
            GameLogicMessage tmpMsg = (GameLogicMessage)msg;
            if(tmpMsg.getType() == Action.TURN)
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
		System.out.println("Made it 6" + PlayerManager.getPlayer());
		lobby = new LobbyScreen(PlayerManager.getPlayer().getPlayerId(), PlayerManager.getPlayer().getName());
		lobbyScene = lobby.createScene();
		userInterface.setScene(lobbyScene);
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
		PlayerStatusMessage msg = new PlayerStatusMessage();
		msg.setPlayerId(playerId);
		msg.setVarField1(color);
		msg.setType(Action.PLAYER_SELECTION);
		
		ClientMessageReceiver.sendMessage(msg);
		System.out.println("TRAEEE Msg Sent");
	}
	
	public static void playerSelection(PlayerStatusMessage msg)
	{
		Player player = PlayerManager.getOtherPlayer(msg.getPlayerId());
		if (player == null)
		{
			System.out.println("Made it 29");
		}
		
		lobby.addPlayer(player.getName(), false, player.getPlayerId(), msg.getVarField1());
	}


}

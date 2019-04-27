package logic;

import messages.BaseMessage;
import messages.GameLogicMessage;
import messages.PlayerStatusMessage;
import controller.ViewController;
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


import java.lang.reflect.Array;
import java.util.*;
import java.util.HashMap;

public class GameProcessor 
{
    private static volatile GameProcessor instance = null;

    private static String [][] board;

    private static Map<Integer, Player> players = new HashMap< Integer, Player>();
    private static Map<String, String> hallways = new HashMap< String,String>();

    private ArrayList<String> cards = new ArrayList<> ();
    private ArrayList<String> roomCards = new ArrayList<> ();
    private ArrayList<String> weaponCards = new ArrayList<> ();
    private ArrayList<String> playerCards = new ArrayList<> ();

    private static String [] solution = new String [3];

    private String [] hand1 = new String [3];
    private String [] hand2 = new String [3];
    private String [] hand3 = new String [3];
    private String [] hand4 = new String [3];
    private String [] hand5 = new String [3];
    private String [] hand6 = new String [3];
    private ArrayList<String []> hands = new ArrayList<>();

    private static ArrayList<Integer> blacklist = new ArrayList<>();

    private static boolean winner;
	
	private static LobbyScreen lobby = null;
    private static Scene lobbyScene = null;
    private static UserInterface userInterface;


	private GameProcessor()
	{
        /**
         *  This checks to ensure no other instance is created using Reflection API
         */
        if (instance != null)
        {
            System.out.println("Exception Handled: Attempt to create new Instance using Reflection API");
        }
        else
        {
            System.out.println("Player Manager Started");
        }

        winner = false;

        /**
         * creates the deck of cards and shuffles them
         */
        roomCards.addAll(Arrays.asList("study", "hall", "lounge", "library", "billiard_room", "dining_room",
                "conservatory", "ball_room", "kitchen"));
        weaponCards.addAll(Arrays.asList("candlestick", "knife", "pipe", "gun", "rope", "wrench"));
        playerCards.addAll(Arrays.asList( "mustard", "scarlet", "plum", "green", "white", "peacock"));
        Collections.shuffle(roomCards);
        Collections.shuffle(weaponCards);
        Collections.shuffle(playerCards);

        /**
         * allocates three cards in the deck to the solution
         */

        solution[0] = roomCards.get(0);
        solution[1] = playerCards.get(0);
        solution[2] = weaponCards.get(0);
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
        hands.addAll(Arrays.asList(hand1,hand2, hand3, hand4, hand5, hand6));
        for (int i=0; i<6; i++){
            String [] currentHand = hands.get(i);
            for (int j=0;j<3; j++){
                currentHand[j] = cards.get(0);
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
        board = new String [5][5];
        board[0][0] = "study"; board[0][1] = "hallway1"; board[0][2] = "hall"; board[0][3] = "hallway2"; board[0][4] = "lounge";
        board[1][0] = "hallway3"; board[1][1] = "deadspace"; board[1][2] = "hallway4"; board[1][3] = "deadspace"; board[1][4] = "hallway5";
        board[2][0] = "library"; board[2][1] = "hallway6"; board[2][2] = "billiard_room"; board[2][3] = "hallway7"; board[2][4] = "dining_room";
        board[3][0] = "hallway8"; board[3][1] = "deadspace"; board[3][2] = "hallway9"; board[3][3] = "deadspace"; board[3][4] = "hallway10";
        board[4][0] = "conservatory"; board[4][1] = "hallway11"; board[4][2] = "ballroom"; board[4][3] = "hallway12"; board[4][4] = "kitchen";

        /**
         *creates each player, gives them an id and a starting position, and puts them in a hashmap
         */
        Player player1 = new Player("jon", 1, new int [] {0,0});
        Player player2 = new Player("trae", 2, new int [] {0,4});
        Player player3 = new Player("alayna", 3, new int [] {4,0});
        Player player4 = new Player("ryan", 4, new int [] {4,4});
        Player player5 = new Player("john", 5, new int [] {2,2});
        Player player6 = new Player("professors", 6, new int [] {2,4});
        players.put(1, player1);
        players.put(2, player2);
        players.put(3, player3);
        players.put(4, player4);
        players.put(5, player5);
        players.put(6, player6);

    }

    public static void turn(int id, String move, String [] guess, String [] suggestion){
        if (blacklist.contains(id)){
            //cant play
        }
        Player player = players.get(id);
        int [] position = player.getPosition();
        int [] newPosition = makeMove(move, position);

        if (newPosition==position){

        }

        if(validateMove(newPosition)){
            player.setPosition(newPosition);
        }

        if (guess!=null){
            for(int i=0; i<3; i++){
                if (Arrays.asList(solution).contains(guess[i])==false){
                    //incorrect guess - you're out
                    blacklist.add(id);
                }
                else{
                    winner = true;

                }
            }
        }

        if (suggestion!=null){
            //move player
            for(int i=0; i<3; i++){
                if (Arrays.asList(solution).contains(guess[i])==false){
                    //need to show whose card the disproof belonged to
                }
            }

        }


    }

    public static int[] makeMove (String move, int[] position){
        int [] newPosition = new int [2];
        if (move.equals("left")){

            if(position[1]>4 || position[1]<0){
                newPosition[1] = position[1];
            }
            else {
                newPosition[1] = position[1]- 1;
            }
        }
        else if (move.equals("right")){

            if (position[1]>4 || position[1]<0){
                newPosition[1] = position[1];
            }
            else{
                newPosition[1] = position[1]+1;
            }
        }
        else if (move.equals("up")){

            if (position[0]>4 || position[0]<0){
                newPosition[0] = position[0];
            }
            else {
                newPosition[0] = position[0]-1;
            }
        }
        else if (move.equals("down")){

            if (position[0]>4 || position[0]<0){
                newPosition[0] = position[0];
            }
            else {
                newPosition[0] = position[0]+1;
            }
        }
        if (board[newPosition[0]][newPosition[1]] == "deadspace"){
            return position;
        }
        else{
            return newPosition;
        }

    }

    public static boolean validateMove (int [] position){
        String boardPosition = board[position[0]][position[1]];
        if (boardPosition.contains("hallway")){
            if (hallways.get(boardPosition).equals("occupied")){
                //invalid move
                return false;
            }
            else{
                hallways.put(boardPosition, "occupied");
                return true;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        GameProcessor gp = new GameProcessor();
        String [] guess = new String[] {"mustard", "knife", "library"};
        String [] suggestion = new String[3];
        gp.turn(1, "right", guess, suggestion);
        gp.turn(1, "right", guess, suggestion);
        gp.turn(1, "right", guess, suggestion);
        gp.turn(1, "right", guess, suggestion);
        gp.turn(1, "right", guess, suggestion);
        gp.turn(1, "right", guess, suggestion);

    }

    public static Map<Integer, String> getLocations(String id){
        Map<Integer, String> locations = new HashMap< Integer, String>();
        for (int i=0; i<players.size(); i++){
            Player player = players.get(i);
            int [] position = player.getPosition();
            String location = board[position[0]][position[1]];
            int playerId = player.getPlayerId();
            locations.put(playerId, location);
        }
        return locations;

    }
    public static boolean winner(){ return winner; }

    public static ArrayList <Integer> getBlacklist(){ return blacklist; }



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
		lobby = new LobbyScreen(2, PlayerManager.getPlayer().getName());
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

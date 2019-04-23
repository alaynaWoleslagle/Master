package utils;

import messages.BaseMessage;
import messages.PlayerStatusMessage;
import utils.Player;
import utils.PlayerManager;


import java.lang.reflect.Array;
import java.util.*;
import java.util.HashMap;

public class GameProcessor {
    private static volatile GameProcessor instance = null;

    private static String [][] board;

    private static Map<String, Player> players = new HashMap< String,Player>();
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


    private GameProcessor(){

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


        roomCards.addAll(Arrays.asList("study", "hall", "lounge", "library", "billiard_room", "dining_room",
                "conservatory", "ball_room", "kitchen"));
        weaponCards.addAll(Arrays.asList("candlestick", "knife", "pipe", "gun", "rope", "wrench"));
        playerCards.addAll(Arrays.asList( "mustard", "scarlet", "plum", "green", "white", "peacock"));
        Collections.shuffle(roomCards);
        Collections.shuffle(weaponCards);
        Collections.shuffle(playerCards);

        solution[0] = roomCards.get(0);
        solution[1] = playerCards.get(0);
        solution[2] = weaponCards.get(0);
        roomCards.remove(0);
        playerCards.remove(0);
        weaponCards.remove(0);

        cards.addAll(roomCards);
        cards.addAll(playerCards);
        cards.addAll(weaponCards);
        Collections.shuffle(cards);


        hands.addAll(Arrays.asList(hand1,hand2, hand3, hand4, hand5, hand6));
        for (int i=0; i<6; i++){
            String [] currentHand = hands.get(i);
            for (int j=0;j<3; j++){
                currentHand[j] = cards.get(0);
                cards.remove(0);
            }
        }


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


        board = new String [5][5];
        board[0][0] = "study"; board[0][1] = "hallway1"; board[0][2] = "hall"; board[0][3] = "hallway2"; board[0][4] = "lounge";
        board[1][0] = "hallway3"; board[1][1] = "deadspace"; board[1][2] = "hallway4"; board[1][3] = "deadspace"; board[1][4] = "hallway5";
        board[2][0] = "library"; board[2][1] = "hallway6"; board[2][2] = "billiard_room"; board[2][3] = "hallway7"; board[2][4] = "dining_room";
        board[3][0] = "hallway8"; board[3][1] = "deadspace"; board[3][2] = "hallway9"; board[3][3] = "deadspace"; board[3][4] = "hallway10";
        board[4][0] = "conservatory"; board[4][1] = "hallway11"; board[4][2] = "ballroom"; board[4][3] = "hallway12"; board[4][4] = "kitchen";

        Player player1 = new Player("jon", 1, new int [] {0,0});
        Player player2 = new Player("trae", 2, new int [] {0,4});
        Player player3 = new Player("alayna", 3, new int [] {4,0});
        Player player4 = new Player("ryan", 4, new int [] {4,4});
        Player player5 = new Player("john", 5, new int [] {2,2});
        players.put("1", player1);
        players.put("2", player2);
        players.put("3", player3);
        players.put("4", player4);
        players.put("5", player5);

    }

    public static void turn(String id, String move, String [] guess, String [] suggestion){
        //Player player = players.get(id);
        Player player = players.get(id);
        int [] position = player.getPosition();
        int [] newPosition = makemove(move, position);

        if (newPosition==position){
            //return invalid move
        }

        if(validateMove(newPosition)){
            player.setPosition(newPosition);
        }

        if (guess!=null){
            for(int i=0; i<3; i++){
                if (Arrays.asList(solution).contains(guess[i])==false){
                    //incorrect guess - you're out
                    //solution.
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

    public static int[] makemove (String move, int[] position){
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
        gp.turn("1", "right", guess, suggestion);
    }


    public static utils.GameProcessor getInstance()
    {
        if (instance == null)
        {
            /**
             *  This is a thread-safe check to ensure another thread can't initialize another MessageReceiver class.
             */
            synchronized (utils.GameProcessor.class)
            {
                if (instance == null)
                {
                    instance = new utils.GameProcessor();
                }

            }
        }

        return instance;
    }


    public static void  processMessage(Object msg)
    {
        if( msg instanceof PlayerStatusMessage)
        {
            PlayerStatusMessage tmpMsg = (PlayerStatusMessage)msg;
            Player tmp = new Player();
            tmp.setName(tmpMsg.getName());
            tmp.setPlayerId(tmpMsg.getPlayerId());

            if(tmpMsg.getType() == BaseMessage.Action.INIT)
            {
                PlayerManager.setPlayer(tmp);
            }
            else if(tmpMsg.getType() == BaseMessage.Action.PLAYER_JOIN)
            {
                PlayerManager.addNewPlayer(tmp);
            }
        }
    }

}

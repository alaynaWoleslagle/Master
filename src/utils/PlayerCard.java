package utils;


public enum PlayerCard 
{

	MUSTARD("Col. Mustard"), SCARLET("Miss Scarlet"), PLUM("Prof. Plum"), GREEN("Mr. Green"), WHITE("Mrs. White"), PEACOCK("Mrs. Peacock");
	    
    private String playerCard;

    PlayerCard(String playerCard) 
    {
        this.playerCard = playerCard ;
    }
    

    public String getValue() 
    {
        return playerCard;
    }
    
    
    public int length()
    {
    	return PlayerCard.values().length;
    }
  
}

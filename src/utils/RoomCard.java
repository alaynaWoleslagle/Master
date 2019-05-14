package utils;


public enum RoomCard 
{

	STUDY("STUDY"), 
	HALL("HALL"), 
	LOUNGE("LOUNGE"), 
	LIBRARY("LIBRARY"),
	BILLARDROOM("BILLARD ROOM"), 
	DININGROOM("DINING ROOM"), 
	CONSERVATORY("CONSERVATORY"), 
	BALLROOM("BALLROOM"), 
	KITCHEN("KITCHEN");
	
    private String roomCard;

    RoomCard(String index) 
    {
        this.roomCard = index ;
    }

    
    public String getValue()
    {
        return roomCard;
    }
    
    public int length()
    {
    	return RoomCard.values().length;
    }
}

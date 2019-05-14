package utils;


public enum WeaponCard 
{	

	CANDLESTICK("CANDLE STICK"), KNIFE("KNIFE"), PIPE("PIPE"), GUN("GUN"), ROPE("ROPE"), WRENCH("WRENCH");
	    
    private String weaponCard;

    WeaponCard(String index) 
    {
        this.weaponCard = index ;
    }

    public String getValue()
    {
        return weaponCard;
    }
    
    public int length()
    {
    	return WeaponCard.values().length;
    }
}

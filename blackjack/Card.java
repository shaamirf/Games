package blackjack;

public class Card {
    private int value;
    private String suit;

    public Card(int v, String s){
        value = v; //1-13: acts as key/value pairs for the 13 different cards
        suit = s;
    }

    public int getValue(){
        return value;
    }
    public String getSuit(){
        return suit;
    }

    public String toString(){ //will be used when displaying cards every turn
        if(value==1) return "A of "+suit;
        if(value==11) return "J of "+suit;
        if(value==12) return "Q of "+suit;
        if(value==13) return "K of "+suit;
        
        return value+" of "+suit;
    }
}

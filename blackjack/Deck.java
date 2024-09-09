package blackjack;
import java.util.*;

public class Deck {
    private ArrayList<Card>deck;

    public Deck(){
        deck = new ArrayList<>();
    }

    public Card getCard(){ //removes card from deck and hands it to the player
        Card c = deck.get(0);
        deck.remove(0);
        return c;
    }

    public void addCard(Card c){ //to add the card back after end of round
        deck.add(c);
    }

    public void shuffle(){ //why cant we just use Collections.shuffle :((((
        //swaps two random indeces within the arrayList 200 times
        for(int i = 0;i<200;i++){
            int pos1 = (int)(Math.random()*deck.size());
            int pos2 = (int)(Math.random()*deck.size());
            Card temp = deck.get(pos1);
            deck.set(pos1,deck.get(pos2));
            deck.set(pos2,temp);
        }
    }
}

package blackjack;
import java.util.*;
public class Player {

    private String name;
    private int balance;
    private ArrayList<Card>cards;

    public Player(String n, int b){
        name = n;
        balance = b;
        cards = new ArrayList<Card>(); //contains all the cards player has during a round
    }

    public String getName(){ 
        return name;
    }

    public int getBalance(){
        return balance;
    }

    public void addAmount(int a){
        balance+=a;
    }

    public void removeAmount(int a){
        balance-=a;
    }

    public void printCards(){  //used for displaying all player cards at end of each turn
        for(Card c:cards) System.out.print(c+", ");
        System.out.println();
    }

    public ArrayList<Card> getDeck(){
        return cards;
    }

    public void hand(Card c){ //used when handing cards to players from the deck
        cards.add(c);
    }

    public void clear(){ //empties the array list after each turn
        while(cards.size()>0)
            cards.remove(0);
    }

    public int total(){
        //basic idea is to add up number values first, then J,Q,K as 10, A will be added at the end
        int total = 0;
        int a_count = 0;
        for(Card c:cards){
            int val = c.getValue();
            if(val == 1)
                a_count++;
            else if(val<11)
                total += val;
            else
                total += 10;
        }
        //if total points + A will go over 21, then A will be set to 1, otherwise it will stay 11.
        for(int i = 0;i<a_count;i++){
            if(total+11>21) 
                total+=1;
            else
                total+=11;
        }
        return total;
    }

    public String toString(){
        return "name: "+name+", bal: "+balance;
    }
}

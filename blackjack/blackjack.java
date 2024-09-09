package blackjack;
import java.util.*;

public class blackjack {
    public static void main(String[] args) {

        //initializing deck
        Deck d = new Deck();
        for(int i = 1; i<14;i++){
            d.addCard(new Card(i,"Diamonds"));
        }
        for(int i = 1; i<14;i++){
            d.addCard(new Card(i,"Spades"));
        }
        for(int i = 1; i<14;i++){
            d.addCard(new Card(i,"Hearts"));
        }
        for(int i = 1; i<14;i++){
            d.addCard(new Card(i,"Clubs"));
        }
        d.shuffle();
        //

        //set up player
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Username");
        String name = in.nextLine();

        Player a = new Player(name, 500); //player object
        Player b = new Player("Dealer",0);//dealer object

        int round = 1;
        boolean run = true; //true value keeps the game going

        while(run){
            run = game(a,b,d,round); //components of a round are coded in game method 
            round++;
            d.shuffle();
        }

        System.out.println("Game Over!"); //final message

    }
    public static boolean game(Player a, Player b, Deck d, int r){ //passes in player, dealer, card deck, and round number
        Scanner choice = new Scanner(System.in);
        System.out.println("Round "+r+":");

        int bet = Integer.MAX_VALUE;
        int troll = 0; //just for troll

        //to ensure bet amount is legal
        while(bet>a.getBalance()){
            System.out.print("Enter Bet: ");
            bet = choice.nextInt();
            choice.nextLine();

            //this part is just for fun, do your best not to bet more than you have twice in a row ^_^
            if(bet>a.getBalance()){
                troll++;
                if(troll==2){
                    System.out.println("DEALER: HAHA Told you so.. Your money is mine!! #sucks to suck");
                    return false;
                }
                System.out.println("DEALER: Cheat again and I take all your money..\nYou only have "+a.getBalance()+"! Enter again.");
            }
        }

        //start round with two cards each
        a.hand(d.getCard());
        a.hand(d.getCard());

        b.hand(d.getCard());
        b.hand(d.getCard());

        int winner = 0; //used in while loop to cover multiple turns
        while(winner==0){
            display(a,b); //display method displays both cards and total value for the player (only one card shown for dealer)

            //user input
            int c = 0;
            System.out.print("1.Hit\n2.Stand\n3.Forfeit\nEnter number: ");
            c = choice.nextInt();
            choice.nextLine();

            //if user hits
            if(c==1){
                a.hand(d.getCard()); //draw card from deck and add to player's arrayList
                winner = winLoss(a,b); //run a winLoss check (checks for 21 or busts)
            }
            //if user stands
            else if(c==2){
                //keep drawing for dealer until reaches 17 or above
                while(b.total()<=17){
                    b.hand(d.getCard());
                    winner = winLoss(a,b); //run a winLoss check
                }
                //this condition occurs when both player and dealer stand and are below 21
                if(winner==0){ 
                    if(a.total()>b.total())
                        winner = 5;
                    if(a.total()<b.total())
                        winner = 4;
                    if(a.total()==b.total())
                        winner = 6;
                }
            //if user forfeits
            } else{
                a.removeAmount(bet/2); //user loses half of money betted
                winner = -1;
            }
        }
        //final display
        System.out.println();
        System.out.println(a.getName()+":");
        a.printCards();
        System.out.println("Total: "+a.total());
        System.out.println("Dealer:");
        b.printCards();
        System.out.println("Total: "+b.total());
        //the section is similar to display function, except it also displays dealer's cards and total value because the round ended
        
        //different conditions based on result (tie, bust, higher score, lower score etc.)
        //made it a little humorous by making the Dealer comment for literally everything
        if(winner==1){
            System.out.println("Dealer Busts! You Won "+bet+"!\nDEALER: Lucky chump -_-");
            a.addAmount(bet);
        }
        if(winner==2){
            System.out.println("*Bust! You Lost "+bet+"!");
            a.removeAmount(bet);
            if(a.getBalance()!=0) System.out.println("DEALER: HAHA NOOB!");
        }
        if(winner==3) {
            System.out.println("*Blackjack! You Won "+bet*3/2+"!\nDEALER: WHAT ARE THE ODDS...ugh");
            a.addAmount(bet*3/2);
        }
        if(winner==4){
            System.out.println("*Lower score than dealer, You Lost "+bet+"!");
            a.removeAmount(bet);
            if(a.getBalance()!=0) System.out.println("DEALER: If I were you I'd stop betting right now...");
        }
        if(winner==5){
            System.out.println("Higher score than dealer, You Won "+bet+"!\nDEALER: DR. FRY WILL NOT APPROVE!!!! GO HOME RN");
            a.addAmount(bet);
        }
        if(winner==6)
            System.out.println("Tie!");

        //this section returns all the cards in play back to the deck for the next round
        for(Card x:a.getDeck())
            d.addCard(x);
        a.clear();

        for(Card x:b.getDeck())
            d.addCard(x);
        b.clear();

        //conditional to check if player is broke lol
        if(a.getBalance()==0){
            System.out.println("Out of Balance!\nDEALER: DR.FRY WAS RIGHT!! lol #statsftw #quitgambling");
            return false;
        }

        //user input to continue or take home amount won
        System.out.println("Go again?\n1.Yes\n2.No");
        int again = choice.nextInt();
        choice.nextLine();

        if(again==1) return true;
        else{
            System.out.println("You take home "+a.getBalance()+"!!\nDEALER: pls dont ever come back :(");
            return false;
        }

    }
    public static void display(Player a, Player b){ //displays the arrayList of cards for player as well as total after every turn
        System.out.println();
        System.out.println(a.getName()+":");
        a.printCards();
        System.out.println("Total: "+a.total());
        //dealer: only first card is shown until the end
        System.out.println("Dealer:");
        System.out.println(b.getDeck().get(0));
        
    }
    public static int winLoss(Player a, Player b){ //checks for blackjacks and busts (automatic wins)
        if(a.total()==21 && b.total()==21)
            return 6;
        if(a.total()==21)
            return 3;
        if(b.total()==21)
            return 4;
        if(a.total()>21)
            return 2;
        if(b.total()>21)
            return 1;
        return 0;
    }
}

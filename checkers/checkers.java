package checkers;
import java.util.*;
public class checkers {
    public static void main(String[] args)
    {
        pieces[][]board = new pieces[8][8];
        //creating board
        for(int i = 0;i<8;i++)
            for(int j = 0;j<8;j++)  board[i][j]= new pieces(" ",8-j,i-1,false);

        for(int i = 0;i<3;i++)
        {
            if(i==1)
                for(int j = 0;j<8;j+=2) board[i][j].setColor("R");
            else
                for(int j = 1;j<8;j+=2) board[i][j].setColor("R");
        }
        for(int i = 5;i<8;i++)
        {
            if(i%2==0) 
                for(int j = 1;j<8;j+=2) board[i][j].setColor("W");
            else 
                for(int j = 0;j<8;j+=2) board[i][j].setColor("W");
        }

        //for running the game
        boolean kill;
        int p = 1;
        int[]counts = {12,12,0};
        //counts[0] represents red piece count
        //counts[1] represents white piece count
        //counts[2] is a special case where the remaining pieces are stuck and cannot move, hence that player loses (or draw if both are stuck).

        while(counts[0]>0 && counts[1]>0 && counts[2]==0){
            kill = player(p,board,counts);
            if(!kill){
                if(p==1) p++;
                else p--;
            }
        }
        if(p==1) p++;
            else p--;

        ///prints final board
        printBoard(board);
        if(counts[2]==3) System.out.println("Draw");
        else if(p==1 || counts[2]==1) System.out.println("Player 1 Wins");
        else System.out.println("Player 2 Wins");     
    }

//_________________________________________________________________________________________________________

    //absolute value function (will be used later)
    public static int abs(int i)
        {
            if(i<0) i*= -1;
            return i;
        }

//_________________________________________________________________________________________________________

    public static void printBoard(pieces[][]b)
    {
        for(int i = 0;i<8;i++)
        {
            System.out.println("  +---+---+---+---+---+---+---+---+");
            System.out.print(8-i+" ");
            for(int j = 0;j<8;j++) 
            {
                // alternate tiles will be textured with ||| while the others will show either "R", "W", or " ".
                if(i==0||i%2==0){
                    if(j==0||j%2==0){
                        System.out.print("||||");
                    } else{
                        //to make distinction between king and regular pieces
                        if(b[i][j].kingStatus()) System.out.print("|~"+b[i][j]+"~");
                        else System.out.print("| "+b[i][j]+" ");
                    }
                }else{
                    if(j==0||j%2==0){
                        //to make distinction between king and regular pieces
                        if(b[i][j].kingStatus()) System.out.print("|~"+b[i][j]+"~");
                        else System.out.print("| "+b[i][j]+" ");
                    }else{
                        System.out.print("||||");
                    }
                }
            }
            System.out.println("|");
        }
        System.out.println("  +---+---+---+---+---+---+---+---+");
        System.out.println("    1   2   3   4   5   6   7   8  ");
    }
//_________________________________________________________________________________________________________

    public static boolean player(int p, pieces[][]b, int[]c)
    {
        Scanner in = new Scanner(System.in);

        //important note * board coordinates do not match the coordinates in the matrix.
        //                 in other words, if user enters 5,6 as coordinates that does not correspond to b[5][6]
        //                 for this reason, I have used the transformation (x,y)-->[8-y][x-1] to match board coordinates.
        //                 hence, (5,6) will correspond to b[2][4] in the 2D Array.

        ///prints out the board each turn
        printBoard(b);

        //coordinates for piece user wants to move
        System.out.print("Player "+p+": Enter x coordinate for the piece you would like to move:");
        int x1 = in.nextInt();
        in.nextLine();
        System.out.print("Player "+p+": Enter y coordinate for the piece you would like to move:");
        int y1 = in.nextInt();
        in.nextLine();

        //Out of bounds check
        if(x1<1 || x1>8 || y1<1 || y1>8)
        {
            System.out.println("Out of Bounds, Try again");
            return player(p,b,c);
        }

        //validates whether piece is at specified position
        if(p==1){
            if(!b[8-y1][x1-1].getColor().equals("R")){
                System.out.println("Piece not there, Try again");
                return player(p,b,c);
            }
        }
        if(p==2){
            if(!b[8-y1][x1-1].getColor().equals("W")){
                System.out.println("Piece not there, Try again");
                return player(p,b,c);
            }
        }

        //destination coordinates
        System.out.print("Player "+p+": Enter x coordinate for where you would like to move the piece:");
        int x2 = in.nextInt();
        in.nextLine();
        System.out.print("Player "+p+": Enter y coordinate for where you would like to move the piece:");
        int y2 = in.nextInt();
        in.nextLine();

        //Out of bounds check
        if(x2<1 || x2>8 || y2<1 || y2>8)
        {
            System.out.println("Out of Bounds, Try again");
            return player(p,b,c);
        }

        //invalid moves
        if(abs(y2-y1)>2 || abs(x2-x1)>2 || abs(y2-y1)!=abs(x2-x1)){
            System.out.println("Invalid Move, Try again");
            return player(p,b,c);
        }
        if(p==1 && !b[8-y1][x1-1].kingStatus() && y2-y1>0){
            System.out.println("Invalid Move, Try again");
            return player(p,b,c);
        }
        if(p==2 && !b[8-y1][x1-1].kingStatus() && y2-y1<0){
            System.out.println("Invalid Move, Try again");
            return player(p,b,c);
        }

        //regular move
        if(abs(x2-x1)==1){
            //for player 1
            if(p==1){
                if(b[8-y2][x2-1].getColor().equals(" ")){
                    //the new position will store the piece object, while the old position resets to blank
                    b[8-y2][x2-1] = b[8-y1][x1-1];
                    b[8-y1][x1-1] = new pieces(" ",x1,y1,false);
                    //checkForTrapped method explained down below
                    c[2] = checkForTrapped(b);
                    //to make a piece king
                    if(y2==1){
                        b[8-y2][x2-1].makeKing(true);
                        return false;
                    }
                    return false;
                }
            }
            //for player 2
            if(p==2){
                if(b[8-y2][x2-1].getColor().equals(" ")){
                    //the new position will store the piece object, while the old position resets to blank
                    b[8-y2][x2-1] = b[8-y1][x1-1];
                    b[8-y1][x1-1] = new pieces(" ",x1,y1,false);
                    //checkForTrapped method explained down below
                    c[2] = checkForTrapped(b);
                    //to make a piece king
                    if(y2==8){
                        b[8-y2][x2-1].makeKing(true);
                        return false;
                    }
                    return false;
                }
            }
        }

        //kill move (jump)
        if(abs(x2-x1)==2){
            //x and y will represent the coordinates for the piece jumped over
            //located by finding x and y average of the start and destination points.
            int x = (x1+x2)/2;
            int y = (y1+y2)/2;
            int temp = 8-y;
            y = x-1;
            x = temp;

            //for player 1
            if(p==1){
                if(b[x][y].getColor().equals("W") && b[8-y2][x2-1].getColor().equals(" ")){
                    //in a jump move, the old position AND the position jumped over will reset to blank
                    b[8-y2][x2-1] = b[8-y1][x1-1];
                    b[8-y1][x1-1] = new pieces(" ",x1,y1,false);
                    b[x][y].setColor(" ");
                    b[x][y].makeKing(false);
                    //decrement piece count once jumped over
                    c[1] = c[1] - 1;
                    c[2] = checkForTrapped(b);
                    //to make a piece king
                    if(y2==1){
                        b[8-y2][x2-1].makeKing(true);
                        return false;
                    }
                    //method explained below
                    return checkForKill(p, x2, y2, b);
                }
            }
            //for player 2
            if(p==2){
                if(b[x][y].getColor().equals("R") && b[8-y2][x2-1].getColor().equals(" ")){
                    b[8-y2][x2-1] = b[8-y1][x1-1];
                    b[8-y1][x1-1] = new pieces(" ",x1,y1,false);
                    b[x][y].setColor(" ");
                    b[x][y].makeKing(false);
                    //decrement piece count once jumped over
                    c[0] = c[0] - 1;
                    c[2] = checkForTrapped(b);
                    //to make a piece king
                    if(y2==8){
                        b[8-y2][x2-1].makeKing(true);
                        return false;
                    }
                    //method explained below
                    return checkForKill(p, x2, y2, b);
                }
            }
        }
        System.out.println("Invalid Move, Try again");
        return player(p,b,c);
    }

//_________________________________________________________________________________________________________

    public static boolean checkForKill(int p, int x2, int y2, pieces[][]b){
        //after every kill move, this method will be called.
        //this method will check if another kill is possible.
        //if a kill is possible, the player goes again
        //if not, then the turn alternates
        //this method will not be called if a piece becomes a king after a jump; the turn will still alternate
        int x3;
        int y3;
        int t3;
        if(p==1){
            if(y2-2>0){
                if(x2-2>0){
                    x3 = x2-1;
                    y3 = y2-1;
                    t3 = 8-y3;
                    y3 = x3-1;
                    x3 = t3;
                    if(b[x3][y3].getColor().equals("W") && b[8-(y2-2)][(x2-2)-1].getColor().equals(" "))
                        return true;
                }
                if(x2+2<9){
                    x3 = x2+1;
                    y3 = y2-1;
                    t3 = 8-y3;
                    y3 = x3-1;
                    x3 = t3;
                    if(b[x3][y3].getColor().equals("W") && b[8-(y2-2)][(x2+2)-1].getColor().equals(" "))
                        return true;
                }
            }
        }
        else{
            if(y2+2<9){
                if(x2-2>0){
                    x3 = x2-1;
                    y3 = y2+1;
                    t3 = 8-y3;
                    y3 = x3-1;
                    x3 = t3;
                    if(b[x3][y3].getColor().equals("R") && b[8-(y2+2)][(x2-2)-1].getColor().equals(" "))
                        return true;
                }
                if(x2+2<9){
                    x3 = x2+1;
                    y3 = y2+1;
                    t3 = 8-y3;
                    y3 = x3-1;
                    x3 = t3;
                    if(b[x3][y3].getColor().equals("R") && b[8-(y2+2)][(x2+2)-1].getColor().equals(" "))
                        return true;
                }
            }
        }
        if(b[8-y2][x2-1].kingStatus()){
            if(p==1){
                if(y2+2<9){
                    if(x2-2>0){
                        x3 = x2-1;
                        y3 = y2+1;
                        t3 = 8-y3;
                        y3 = x3-1;
                        x3 = t3;
                        if(b[x3][y3].getColor().equals("W") && b[8-(y2+2)][(x2-2)-1].getColor().equals(" "))
                            return true;
                    }
                    if(x2+2<9){
                        x3 = x2+1;
                        y3 = y2+1;
                        t3 = 8-y3;
                        y3 = x3-1;
                        x3 = t3;
                        if(b[x3][y3].getColor().equals("W") && b[8-(y2+2)][(x2+2)-1].getColor().equals(" "))
                            return true;
                    }
                }
            }
            else{
                if(y2-2>0){
                    if(x2-2>0){
                        x3 = x2-1;
                        y3 = y2-1;
                        t3 = 8-y3;
                        y3 = x3-1;
                        x3 = t3;
                        if(b[x3][y3].getColor().equals("R") && b[8-(y2-2)][(x2-2)-1].getColor().equals(" "))
                            return true;
                    }
                    if(x2+2<9){
                        x3 = x2+1;
                        y3 = y2-1;
                        t3 = 8-y3;
                        y3 = x3-1;
                        x3 = t3;
                        if(b[x3][y3].getColor().equals("R") && b[8-(y2-2)][(x2+2)-1].getColor().equals(" "))
                            return true;
                    }
                }
            }
        }
        return false;
    }
//_________________________________________________________________________________________________________
    

    public static int checkForTrapped(pieces[][]b){
        //this methods checks if each piece has the ability to either move one block or jump.
        //if neither piece from one side can move, then they are trapped and they automatically lose.
        //if both teams are trapped at a specific turn, then the game will result in a draw.
        boolean r = true;
        boolean w = true;
        for(int i = 0;i<8;i++){
            for(int j = 0;j<8;j++){
                if(b[i][j].getColor().equals("R")){
                    int check = 1;
                    while(check<3 && i+check<8){
                        if(j-check>=0){
                            if(b[i+check][j-check].getColor().equals(" "))
                                r = false;
                        }
                        if(j+check<8){
                            if(b[i+check][j+check].getColor().equals(" "))
                                r = false;
                        }
                        check++;
                    }
                }
                if(b[i][j].getColor().equals("R") && b[i][j].kingStatus()){
                    int check = 1;
                    while(check<3 && i-check>=0){
                        if(j-check>=0){
                            if(b[i-check][j-check].getColor().equals(" "))
                                r = false;
                        }
                        if(j+check<8){
                            if(b[i-check][j+check].getColor().equals(" "))
                                r = false;
                        }
                        check++;
                    }
                }
            }
        }    
        for(int i = 0;i<8;i++){
            for(int j = 0;j<8;j++){
                if(b[i][j].getColor().equals("W")){
                    int check = 1;
                    while(check<3 && i-check>=0){
                        if(j-check>=0){
                            if(b[i-check][j-check].getColor().equals(" "))
                                w = false;
                        }
                        if(j+check<8){
                            if(b[i-check][j+check].getColor().equals(" "))
                                w = false;
                        }
                        check++;
                    }
                }
                if(b[i][j].getColor().equals("W") && b[i][j].kingStatus()){
                    int check = 1;
                    while(check<3 && i+check<8){
                        if(j-check>=0){
                            if(b[i+check][j-check].getColor().equals(" "))
                                w = false;
                        }
                        if(j+check<8){
                            if(b[i+check][j+check].getColor().equals(" "))
                                w = false;
                        }
                        check++;
                    }
                }
            }
        }
        if(r&&w) return 3;
        else if(w) return 2;
        else if(r) return 1;
        else return 0;
        //0 : game goes on.
        //1 : red is trapped, so white wins.
        //2 : white is trapped, so red wins.
        //3 : both are trapped, game is a draw.
    }
}
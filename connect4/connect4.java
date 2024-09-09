package connect4;
import java.util.*;
public class connect4 {
    public static void main(String[] args) {
        //creating connect 4 board
        String[][]board = new String[6][7];
        for(int i = 0;i<6;i++)
        {
            for(int j = 0;j<7;j++) 
                board[i][j]=" ";
        }

        ///for running the game
        boolean run = true;
        boolean draw = false;
        int player = 1;

        while(run && !draw)
        {
            run = player(player,board);
            draw = isDraw(board);
            //switches between player1 and player2
            if(run) 
            {
                if(player==1) player++;
                else player--;
            }
        }
        
        ///prints final board
        printBoard(board);
        
        ///final statement
        if(draw) System.out.println("Draw");
        else if(player==1) System.out.println("Player 1 Wins");
        else System.out.println("Player 2 Wins");
    }
    public static boolean player(int p, String[][]b)
    {
        Scanner in = new Scanner(System.in);

        ///prints out the board each turn
        printBoard(b);

        ///user input
        System.out.println("Player "+p+": Enter Column #");
        int col = in.nextInt();
        in.nextLine();
        col--;

        int row = 5;
        boolean connect = true;

        //places value (1 or 2) in the lowest available row
        while(row>=0)
        {
            if(b[row][col].equals(" "))
            {
                if(p==1) b[row][col] = "1";
                else b[row][col] = "2";
                
                connect = straightCheck(b,row,col); //checks for 4 in a row (vertical or horizontal)
                if(connect) connect = diagonalCheck(b,row,col); //checks for 4 in a row (diagonal)
                return connect; ///breaks out of loop
            }
            row--;
        }
        System.out.println("Row is full, pick another");
        connect = player(p,b);
        return connect;
    }
    public static boolean straightCheck(String[][]b, int r, int c)
    {
        int hstreak = 1; //horizontal
        int vstreak = 1; //vertical
        int check = 1;
        while((hstreak<4 || vstreak<4) && check <4)
        {
            //checks previous and next values if within limits and compares them
            //vertical
            if(r+check<=5)
            {
                if(b[r+check][c].equals(b[r][c]))
                    vstreak++;
            }
            if(r-check>=0)
            {
                if(b[r-check][c].equals(b[r][c]))
                    vstreak++;
            }
            //horizontal
            if(c+check<=6)
            {
                if(b[r][c+check].equals(b[r][c]))
                    hstreak++;
            }
            if(c-check>=0)
            {
                if(b[r][c-check].equals(b[r][c]))
                    hstreak++;
            }
            check++; //checks for a max of 3 times per turn
        }
        if(vstreak==4||hstreak==4) return false;
        return true;
    }
    public static boolean diagonalCheck(String[][]b, int r, int c)
    {
        int bltrstreak = 1; //bottom left to top right
        int tlbrstreak = 1; //top left to bottom right
        int check = 1;
        while((bltrstreak<4 || tlbrstreak <4) && check <4) 
        {
            //checks previous and next values if within limits and compares them
            //bottom left to top right
            if(r-check>=0 && c+check<=6)
            {
                if(b[r-check][c+check].equals(b[r][c]))
                    bltrstreak++;
            }
            if(r+check<=5 && c-check>=0)
            {
                if(b[r+check][c-check].equals(b[r][c]))
                    bltrstreak++;
            }
            //top left to bottom right
            if(r-check>=0 && c-check>=0)
            {
                if(b[r-check][c-check].equals(b[r][c]))
                    tlbrstreak++;
            }
            if(r+check<=5 && c+check<=6)
            {
                if(b[r+check][c+check].equals(b[r][c]))
                    tlbrstreak++;
            }
            check++; //checks for a max of 3 times per turn
        }
        if(bltrstreak==4||tlbrstreak==4) return false;
        return true;
    }
    public static void printBoard(String[][]b)
    {
        //print out the board
        for(int i = 0;i<6;i++)
        {
            System.out.println("+---+---+---+---+---+---+---+");
            for(int j = 0;j<7;j++) 
            {
                System.out.print("| "+b[i][j]+" ");
            }
            System.out.println("|");
        }
        System.out.println("+---+---+---+---+---+---+---+");
    }
    public static boolean isDraw(String[][]b)
    {
        //if no empty spaces left in the board, return false to end the game
        for(int i = 0;i<6;i++)
        {
            for(int j = 0;j<7;j++) 
                if(b[i][j]==" ") return false;
        }
        return true; 
    }
}

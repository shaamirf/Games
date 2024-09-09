package checkers;

public class pieces
{
    private String color;
    private int xCor;
    private int yCor;
    private boolean king;
  
    public pieces()
    {
        color = "W";
        xCor = 0;
        yCor = 0;
        king = false;
    }
  
    public pieces(String c, int x, int y, boolean b)
    {
        color = c;
        xCor = x;
        yCor = y;
        king = b;
    }
    
    public String getColor()
    {
        return color;
    }

    public int getX()
    {
        return xCor;
    }
    
    public int getY()
    {
        return yCor;
    }
    
    public boolean kingStatus()
    {
        return king;
    }
    
    public void setColor(String c)
    {
        color = c;
    }

    public void setX(int x)
    {
        xCor = x;
    }
    
    public void setY(int y)
    {
        yCor = y;
    }
    
    public void makeKing(boolean k)
    {
        king = k;
    }

    public String toString(){
        return color;
    }
}


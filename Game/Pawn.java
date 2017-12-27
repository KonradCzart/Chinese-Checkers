package Game;

public class Pawn 
{
	private ColorPlayer myPlayer;
	private int id;
	
	private int x;
	private int y;
	
	public Pawn(ColorPlayer player, int id, int x, int y)
	{
		myPlayer = player;
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public ColorPlayer getPlayer() 
	{
		return myPlayer;
	}

	public void setPlayer(ColorPlayer myPlayer) 
	{
		this.myPlayer = myPlayer;
	}

	public int getX() 
	{
		return x;
	}

	public void setX(int x) 
	{
		this.x = x;
	}

	public int getY() 
	{
		return y;
	}

	public void setY(int y) 
	{
		this.y = y;
	}
	
	public int getID()
	{
		return this.id;
	}
	
}

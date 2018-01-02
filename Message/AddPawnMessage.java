package Message;

import Game.ColorPlayer;

/**
 * 
 * @author Konrad Czart
 * Message with information about position pawn
 */
public class AddPawnMessage implements Message
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 890321962529751062L;	

	private ColorPlayer player;
	private int myX;
	private int myY;
	
	public AddPawnMessage(ColorPlayer player, int myX, int myY)
	{
		this.player = player;
		this.myX = myX;
		this.myY = myY;
	}
	
	public ColorPlayer getPlayer() 
	{
		return player;
	}


	public int getMyX() 
	{
		return myX;
	}


	public int getMyY() 
	{
		return myY;
	}

}

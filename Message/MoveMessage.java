package Message;

import Game.ColorPlayer;

public class MoveMessage implements Message
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 629892178674023525L;
	
	private int oldX;
	private int oldY;
	private int newX;
	private int newY;
	private Boolean endTurn;
	private ColorPlayer nextTurnPlayer;
	
	

	public MoveMessage(int oldX, int oldY, int newX, int newY)
	{
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
		endTurn = false;
	}
	
	public MoveMessage(Boolean endTurn)
	{
		this.endTurn = endTurn;
		this.oldX = 0;
		this.oldY = 0;
		this.newX = 0;
		this.newY = 0;
	}
	
	public int getOldX() 
	{
		return oldX;
	}



	public int getOldY() 
	{
		return oldY;
	}


	public int getNewX() 
	{
		return newX;
	}



	public int getNewY() 
	{
		return newY;
	}


	public Boolean getEndTurn() 
	{
		return endTurn;
	}	
	
	public ColorPlayer getNextTurnPlayer() {
		return nextTurnPlayer;
	}

	public void setNextTurnPlayer(ColorPlayer nextTurnPlayer) {
		this.nextTurnPlayer = nextTurnPlayer;
	}

}

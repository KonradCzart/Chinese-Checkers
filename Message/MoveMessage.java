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
	private String nextTurnPlayer;
	private ColorPlayer movePlayer;
	
	

	public MoveMessage(int oldX, int oldY, int newX, int newY)
	{
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
		endTurn = false;
	}
	
	public MoveMessage(int oldX, int oldY, int newX, int newY, ColorPlayer movePlayer)
	{
		this.oldX = oldX;
		this.oldY = oldY;
		this.newX = newX;
		this.newY = newY;
		endTurn = false;
		this.movePlayer = movePlayer;
		this.nextTurnPlayer = "";
	}
	
	public MoveMessage(Boolean endTurn)
	{
		this.endTurn = endTurn;
		this.oldX = 0;
		this.oldY = 0;
		this.newX = 0;
		this.newY = 0;
		this.movePlayer = ColorPlayer.PLAYER_EMPTY;
		this.nextTurnPlayer = "";
	}
	
	public MoveMessage(Boolean endTurn, ColorPlayer movePlayer, String name)
	{
		this.endTurn = endTurn;
		this.oldX = 0;
		this.oldY = 0;
		this.newX = 0;
		this.newY = 0;
		this.movePlayer = movePlayer;
		this.nextTurnPlayer = name;
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
	
	public String getNextTurnPlayer() {
		return nextTurnPlayer;
	}

	public void setNextTurnPlayer(String nextTurnPlayer) {
		this.nextTurnPlayer = nextTurnPlayer;
	}
	
	public void setMovePlayer(ColorPlayer movePlayer)
	{
		this.movePlayer = movePlayer;
	}
	
	public ColorPlayer getMovePlayer()
	{
		return movePlayer;
	}

}

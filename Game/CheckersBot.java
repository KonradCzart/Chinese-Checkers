package Game;

import java.util.ArrayList;

import Message.MoveMessage;

public class CheckersBot 
{
	private ColorPlayer myPlayer;
	private MoveMessage moveMessage;
	private Game myGame;
	private int gameID;
	private ArrayList<Pawn> tabPawn;
	private int goalX;
	private int goalY;
	private Boolean finishGoal;
	Boolean remove;
	
	public CheckersBot(ColorPlayer myPlayer, Game myGame, int gameID)
	{
		this.myPlayer = myPlayer;
		this.gameID = gameID;
		this.myGame = myGame;
		moveMessage = null;
		setGoal(myPlayer);
		finishGoal = false;
		remove = false;
	}
	
	public MoveMessage moveBot()
	{

		Boolean move = false;
		Pawn removePawn = null;
		
		int roadX;
		int roadY;
		
		for(Pawn tmp : tabPawn)
		{
			roadX = tmp.getX() - goalX;
			roadY = tmp.getY() - goalY;
			
			move = this.getMoveRoad(roadX, roadY, tmp, 2);
			if(move)
			{
				removePawn = tmp;
				break;
			}
				
		}
		
		if(remove)
		{
			remove = false;
			tabPawn.remove(removePawn);
		}
		
		if(move)
		{
			return moveMessage;
		}
		
		
		for(Pawn tmp : tabPawn)
		{
			roadX = tmp.getX() - goalX;
			roadY = tmp.getY() - goalY;
			
			move = this.getMoveRoad(roadX, roadY, tmp, 1);
			
			if(move)
			{
				removePawn = tmp;
				break;
			}
				
		}
		
		if(remove)
		{
			remove = false;
			tabPawn.remove(removePawn);
		}
		
		if(move)
		{
			
			return moveMessage;
		}
		moveMessage = new MoveMessage(true);
		return moveMessage;
	}
	
	public int getGameID()
	{
		return gameID;
	}
	
	public ColorPlayer getBotPlayer()
	{
		return myPlayer;
	}
	
	private Boolean getMoveRoad(int lengthX, int lengthY, Pawn currentPawn, int count)
	{
		Boolean move = false;

		int road = 0;
		int pawnX = currentPawn.getX();
		int pawnY = currentPawn.getY();
		int tmpX =0;
		int tmpY = 0;
		
		if (lengthX == 0 && lengthY == 0 && !finishGoal)
		{
			finishGoal = true;
		}
		
		if(Math.abs(lengthX) < 3 && Math.abs(lengthY) < 3 && finishGoal)
		{
			move = false;
			remove = true;
		}
		else if(lengthX >= 0 && lengthY >= 0)
		{
			
			tmpX = pawnX;
			tmpY = pawnY - count;
			try {
				myGame.move(myPlayer, pawnX, pawnY, tmpX, tmpY);
				currentPawn.setX(tmpX);
				currentPawn.setY(tmpY);
				moveMessage = new MoveMessage(pawnX, pawnY, tmpX, tmpY);
				return true;
			} catch (BadPlayerException | IncorrectMoveException e) {
				move = false;
			}
			
			tmpX = pawnX - count;
			tmpY = pawnY;
			try {
				myGame.move(myPlayer, pawnX, pawnY, tmpX, tmpY);
				currentPawn.setX(tmpX);
				currentPawn.setY(tmpY);
				moveMessage = new MoveMessage(pawnX, pawnY, tmpX, tmpY);
				return true;
			} catch (BadPlayerException | IncorrectMoveException e) {
				move = false;
			}
			
			
		}
		else if(lengthX >= 0 && lengthY <= 0)
		{
			tmpX = pawnX - count;
			tmpY = pawnY + count;
			try {
				myGame.move(myPlayer, pawnX, pawnY, tmpX, tmpY);
				currentPawn.setX(tmpX);
				currentPawn.setY(tmpY);
				moveMessage = new MoveMessage(pawnX, pawnY, tmpX, tmpY);
				return true;
			} catch (BadPlayerException | IncorrectMoveException e) {
				move = false;
			}
			
			tmpX = pawnX - count;
			tmpY = pawnY;
			try {
				myGame.move(myPlayer, pawnX, pawnY, tmpX, tmpY);
				currentPawn.setX(tmpX);
				currentPawn.setY(tmpY);
				moveMessage = new MoveMessage(pawnX, pawnY, tmpX, tmpY);
				return true;
			} catch (BadPlayerException | IncorrectMoveException e) {
				move = false;
			}
		}
		else if(lengthX <= 0 && lengthY <= 0)
		{
			tmpX = pawnX + count;
			tmpY = pawnY;
			try {
				myGame.move(myPlayer, pawnX, pawnY, tmpX, tmpY);
				currentPawn.setX(tmpX);
				currentPawn.setY(tmpY);
				moveMessage = new MoveMessage(pawnX, pawnY, tmpX, tmpY);
				return true;
			} catch (BadPlayerException | IncorrectMoveException e) {
				move = false;
			}
			
			tmpX = pawnX;
			tmpY = pawnY + count;
			try {
				myGame.move(myPlayer, pawnX, pawnY, tmpX, tmpY);
				currentPawn.setX(tmpX);
				currentPawn.setY(tmpY);
				moveMessage = new MoveMessage(pawnX, pawnY, tmpX, tmpY);
				return true;
			} catch (BadPlayerException | IncorrectMoveException e) {
				move = false;
			}
		}
		else if(lengthX <= 0 && lengthY >= 0)
		{
			tmpX = pawnX + count;
			tmpY = pawnY - count;
			try {
				myGame.move(myPlayer, pawnX, pawnY, tmpX, tmpY);
				currentPawn.setX(tmpX);
				currentPawn.setY(tmpY);
				moveMessage = new MoveMessage(pawnX, pawnY, tmpX, tmpY);
				return true;
			} catch (BadPlayerException | IncorrectMoveException e) {
				move = false;
			}
			
			tmpX = pawnX;
			tmpY = pawnY + count;
			try {
				myGame.move(myPlayer, pawnX, pawnY, tmpX, tmpY);
				currentPawn.setX(tmpX);
				currentPawn.setY(tmpY);
				moveMessage = new MoveMessage(pawnX, pawnY, tmpX, tmpY);
				return true;
			} catch (BadPlayerException | IncorrectMoveException e) {
				move = false;
			}
		}
		if(remove)
			tabPawn.remove(currentPawn);
		
		return move;
	}
	
	public void setPawn(ArrayList<Pawn> tabPawn)
	{
		this.tabPawn = tabPawn;
	}

	private void setGoal(ColorPlayer player)
	{
		switch (player)
		{
			case PLAYER_ONE:
			{
				goalX = 17;
				goalY = 5;
				
				break;
			}
			case PLAYER_TWO:
			{
				goalX = 13;
				goalY = 1;

				break;
			}
			case PLAYER_THREE:
			{
				goalX = 5;
				goalY = 5;
				
				break;
			}
			case PLAYER_FOUR:
			{
				goalX = 1;
				goalY = 13;
				
				break;
			}
			case PLAYER_FIVE:
			{
				goalX = 5;
				goalY = 17;

				break;
			}
			case PLAYER_SIX:
			{
				goalX = 13;
				goalY = 13;
				
				break;
			}
			default:
				goalX = 0;
				goalY = 0;
				break;

		}
	}
	
}

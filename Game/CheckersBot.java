package Game;

import java.util.ArrayList;

import Message.MoveMessage;


/**
 * 
 * @author Konrad Czart
 *	Stupid bot for testing the game
 */
public class CheckersBot 
{
	private ColorPlayer myPlayer;
	private MoveMessage moveMessage;
	private Game myGame;
	private int gameID;
	private ArrayList<Pawn> tabPawn;
	private ArrayList<Pawn> tabPawnGoal;
	private int goalX;
	private int goalY;
	private Boolean finishGoal;

	/**
	 * 
	 * @param myPlayer Color of pawns to move
	 * @param myGame Game for bot
	 * @param gameID Game id for the bot
	 */
	public CheckersBot(ColorPlayer myPlayer, Game myGame, int gameID)
	{
		tabPawnGoal = new ArrayList<Pawn>();
		this.myPlayer = myPlayer;
		this.gameID = gameID;
		this.myGame = myGame;
		moveMessage = null;
		setGoal(myPlayer);
		finishGoal = false;

	}
	
	/**
	 * 
	 * @return MoveMessage for the customer about the movement made by the bot
	 */
	public MoveMessage moveBot()
	{

		Boolean move = false;
		
		int roadX;
		int roadY;
		
		for(Pawn tmp : tabPawn)
		{
			if(!tabPawnGoal.contains(tmp))
			{
				roadX = tmp.getX() - goalX;
				roadY = tmp.getY() - goalY;
			
				move = this.getMoveRoad(roadX, roadY, tmp, 2);
				if(move)
				{
					return moveMessage;
				}
			}
				
		}
		
		
		for(Pawn tmp : tabPawn)
		{
			if(!tabPawnGoal.contains(tmp))
			{
				roadX = tmp.getX() - goalX;
				roadY = tmp.getY() - goalY;
			
				move = this.getMoveRoad(roadX, roadY, tmp, 1);
				if(move)
				{
					return moveMessage;
				}
			}
				
		}
		
		moveMessage = new MoveMessage(true);
		return moveMessage;
	}
	
	/**
	 * 
	 * @return current gameID
	 */
	public int getGameID()
	{
		return gameID;
	}
	
	/**
	 * 
	 * @return Game id
	 */
	public ColorPlayer getBotPlayer()
	{
		return myPlayer;
	}
	
	
	/**
	 * Logic move bot
	 */
	private Boolean getMoveRoad(int lengthX, int lengthY, Pawn currentPawn, int count)
	{
		Boolean move = false;


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
			tabPawnGoal.add(currentPawn);
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

		
		return move;
	}
	
	/**
	 * Adds pawns to the bot that it can move
	 * @param tabPawn ArrayList<Pawn> Pawns for bot
	 */
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

package Game;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;


public class Game 
{
	private Field[][] tabField;
	private int countPlayer;
	private int currentPlayer;
	private Boolean start;
	private int id;
	private Boolean endTurn;
	private Pawn lastMovePawn;
	private ArrayList<ColorPlayer> winPlayer;
	private ArrayList<ColorPlayer> queuePlayerTab;
	private ArrayList<ColorPlayer> botPlayerTab;
	
	private ArrayList<Pawn> tabPawn;
	
	
	
	public Game(int id)
	{
		this.id = id;
		currentPlayer = 0;
		tabField = new Field[19][19];
		tabPawn = new ArrayList<Pawn>();
		winPlayer = new ArrayList<ColorPlayer>();
		queuePlayerTab = new ArrayList<ColorPlayer>();
		botPlayerTab = new ArrayList<ColorPlayer>();
		endTurn = false;
		lastMovePawn = null;
		createBoard();
		start = false;
	}
	
	public void addBotToGame(ColorPlayer botPlayer)
	{
		botPlayerTab.add(botPlayer);
	}
	
	public Boolean botQueue()
	{
		
		ColorPlayer queuePlayer = queuePlayerTab.get(currentPlayer);
		
		for(ColorPlayer tmp : botPlayerTab)
		{
			if(queuePlayer == tmp)
				return true;
			
		}
		
		return false;
	}

	public ArrayList<Pawn> getConcretPawn(ColorPlayer concret)
	{
		ArrayList<Pawn> newTabPawn = new ArrayList<Pawn>();
		
		for(Pawn tmp : tabPawn)
		{
			if(tmp.getPlayer() == concret)
				newTabPawn.add(tmp);
		}
		
		return newTabPawn;
	}
	
	public ArrayList<Pawn> getArrayPawn()
	{
		return tabPawn;
	}
	
	public Boolean winPlayer(ColorPlayer player)
	{
		Boolean win = false;
		switch (player)
		{
			case PLAYER_ONE:
			{
				win = this.pawnInZone(ColorPlayer.PLAYER_ONE, Zone.ZONE_FOUR, 10);
				break;
			}
			case PLAYER_TWO:
			{
				win = this.pawnInZone(ColorPlayer.PLAYER_TWO, Zone.ZONE_FIVE, 10);
				break;
			}
			case PLAYER_THREE:
			{
				win = this.pawnInZone(ColorPlayer.PLAYER_THREE, Zone.ZONE_SIX, 10);
				break;
			}
			case PLAYER_FOUR:
			{
				win = this.pawnInZone(ColorPlayer.PLAYER_FOUR, Zone.ZONE_ONE, 10);
				break;
			}
			case PLAYER_FIVE:
			{
				win = this.pawnInZone(ColorPlayer.PLAYER_FIVE, Zone.ZONE_TWO, 10);
				break;
			}
			case PLAYER_SIX:
			{
				win = this.pawnInZone(ColorPlayer.PLAYER_SIX, Zone.ZONE_THREE, 10);
				break;
			}
			default:
				break;

		}
		
		return win;
	}
	
	public Boolean pawnInZone(ColorPlayer player, Zone zone,int number)
	{
		int count = 0;
		ColorPlayer currentPlayer;
		Zone currentZone;
		int x;
		int y;
		
		for(Pawn tmp : tabPawn)
		{
			currentPlayer = tmp.getPlayer();
			x = tmp.getX();
			y = tmp.getY();
			currentZone = tabField[x][y].getZone();
			
			if(player == currentPlayer && zone == currentZone)
				count++;
		}
		
		if(count == number)
			return true;
		else
			return false;
	}
	
	public void endMove(ColorPlayer movePlayer) throws BadPlayerException
	{
		ColorPlayer queuePlayer = queuePlayerTab.get(currentPlayer);
		
		if(movePlayer != queuePlayer)
			throw new BadPlayerException();
		
		currentPlayer = (currentPlayer + 1) % countPlayer;
		
		ColorPlayer nextPlayer = queuePlayerTab.get(currentPlayer);
		
		while(winPlayer.contains(nextPlayer))
		{
			currentPlayer = (currentPlayer + 1) % countPlayer;
			nextPlayer = queuePlayerTab.get(currentPlayer);
		}
		
		
		
		lastMovePawn = null;
		endTurn = false;

	}
	
	public ColorPlayer getQueuePlayer()
	{
		ColorPlayer queue = queuePlayerTab.get(currentPlayer);
		return queue;
	}
	
	public void move(ColorPlayer movePlayer, int oldX, int oldY, int newX, int newY) throws BadPlayerException, IncorrectMoveException
	{

		Pawn currentPawn = null;
		Boolean isPawn = false;
		Boolean correctMove;
		int pawnX = -10;
		int pawnY = -10;
		Boolean specialValidata = false;
		
		ColorPlayer queuePlayer = queuePlayerTab.get(currentPlayer);
		
		if(movePlayer != queuePlayer)
			throw new BadPlayerException();
		
		if(endTurn)
			throw new IncorrectMoveException();
		
		if(lastMovePawn != null)
		{
			currentPawn = lastMovePawn;
			pawnX = currentPawn.getX();
			pawnY = currentPawn.getY();
			
			if(oldX == pawnX && oldY == pawnY)
			{
				isPawn = true;
				specialValidata = true;
			}
		}
		else
		{
			for (Pawn tmp : tabPawn)
			{
				pawnX = tmp.getX();
				pawnY = tmp.getY();
				
				if(oldX == pawnX && oldY == pawnY)
				{
					currentPawn = tmp;
					isPawn = true;
					break;
				}
			}
		}
		
		if(!isPawn)
			throw new IncorrectMoveException();
			
		if(currentPawn.getPlayer() != movePlayer)
				throw new BadPlayerException();
		
		correctMove = this.moveValidate(pawnX, pawnY, newX, newY, specialValidata);
		
		if(!correctMove)
			throw new IncorrectMoveException();
		else
		{
			currentPawn.setX(newX);
			currentPawn.setY(newY);
			
			tabField[pawnX][pawnY].setFieldStatus(FieldStatus.AVAILABLE);
			tabField[newX][newY].setFieldStatus(FieldStatus.UNAVAILABLE);
			
			if(!endTurn)
				lastMovePawn = currentPawn;
				
			Boolean win = this.winPlayer(movePlayer);
			
			if(win)
				winPlayer.add(movePlayer);
				
			
		}
		
	}
		
	
	public int getID()
	{
		return id;
	}
	
	public void startGame()
	{
		
		start = true;
		queuePlayerTab.sort(null);
		double x = Math.random();
		int a = (int) (x * countPlayer);
	
		currentPlayer = a;
	}
	
	public Boolean getStartStatus()
	{
		return start;
	}
	
	public ColorPlayer addPalyer()
	{
		if(!start)
		{
			ColorPlayer newPlayer;
			if(countPlayer == 0)
			{
				newPlayer = ColorPlayer.PLAYER_ONE;
				addPawn(newPlayer, Zone.ZONE_ONE);
				countPlayer++;
				currentPlayer = 0;
				
				queuePlayerTab.add(newPlayer);
				return newPlayer;
			}
			else if(countPlayer == 1)
			{
				
				newPlayer = ColorPlayer.PLAYER_FOUR;
				
				addPawn(newPlayer, Zone.ZONE_FOUR);
				countPlayer++;
			
				queuePlayerTab.add(newPlayer);
				return newPlayer;
			}
			else if(countPlayer == 2)
			{
				newPlayer = ColorPlayer.PLAYER_TWO;
				
				addPawn(newPlayer, Zone.ZONE_TWO);
				countPlayer++;
			
				queuePlayerTab.add(newPlayer);
				return newPlayer;
			}
			else if(countPlayer == 3)
			{
				newPlayer = ColorPlayer.PLAYER_FIVE;
				
				addPawn(newPlayer, Zone.ZONE_FIVE);
				countPlayer++;
			
				queuePlayerTab.add(newPlayer);
				return newPlayer;
			}
			else if(countPlayer == 4)
			{
				newPlayer = ColorPlayer.PLAYER_THREE;
				
				addPawn(newPlayer, Zone.ZONE_THREE);
				countPlayer++;
			
				queuePlayerTab.add(newPlayer);
				return newPlayer;
			}
			else if(countPlayer == 5)
			{
				newPlayer = ColorPlayer.PLAYER_SIX;
				
				addPawn(newPlayer, Zone.ZONE_SIX);
				countPlayer++;
			
				queuePlayerTab.add(newPlayer);
				return newPlayer;
			}
			else
				return ColorPlayer.PLAYER_EMPTY;
			
		}
		else
			return ColorPlayer.PLAYER_EMPTY;

	}
	
	public Boolean moveValidate(int oldX, int oldY, int newX, int newY, Boolean special)
	{
		int tmpX;
		int tmpY;
		endTurn = true;
		
		if(newX >= 19 || newX < 0 || newY >= 19 || newY < 0)
		{
			endTurn = false;
			return false;
		}
		
		FieldStatus newFieldStatus = tabField[newX][newY].getFieldStatus();
		FieldStatus tmpStatus;
		
		if(newFieldStatus != FieldStatus.AVAILABLE)
		{
			endTurn = false;
			return false;
		}
		//upper right (1)
		
		tmpX = oldX - 1;
		tmpY = oldY + 1;
		tmpStatus = tabField[tmpX][tmpY].getFieldStatus();
		
		if(tmpX == newX && tmpY == newY && !special)
			return true;
		else if(tmpStatus == FieldStatus.UNAVAILABLE )
		{
			tmpX = tmpX - 1;
			tmpY = tmpY + 1;
			
			if(tmpX == newX && tmpY == newY)
			{
				endTurn = false;
				return true;
			}
		}
		
		//right (2)
		
		tmpX = oldX;
		tmpY = oldY + 1;
		tmpStatus = tabField[tmpX][tmpY].getFieldStatus();
			
		if(tmpX == newX && tmpY == newY && !special)
			return true;
		else if(tmpStatus == FieldStatus.UNAVAILABLE )
		{
			tmpX = tmpX;
			tmpY = tmpY + 1;
			
			if(tmpX == newX && tmpY == newY)
			{
				endTurn = false;
				return true;
			}
		}

		//down (3)
		
		tmpX = oldX + 1;
		tmpY = oldY ;
		tmpStatus = tabField[tmpX][tmpY].getFieldStatus();
			
		if(tmpX == newX && tmpY == newY && !special)
			return true;
		else if(tmpStatus == FieldStatus.UNAVAILABLE )
		{
			tmpX = tmpX + 1;
			tmpY = tmpY;
			
			if(tmpX == newX && tmpY == newY)
			{
				endTurn = false;
				return true;
			}
		}
		
		//down left (4)
		
		tmpX = oldX +1;
		tmpY = oldY -1;
		tmpStatus = tabField[tmpX][tmpY].getFieldStatus();
			
		if(tmpX == newX && tmpY == newY && !special) 
			return true;
		else if(tmpStatus == FieldStatus.UNAVAILABLE )
		{
			tmpX = tmpX + 1;
			tmpY = tmpY - 1;
			
			if(tmpX == newX && tmpY == newY)
			{
				endTurn = false;
				return true;
			}
		}		
		//left (5)
		
		tmpX = oldX;
		tmpY = oldY - 1;
		tmpStatus = tabField[tmpX][tmpY].getFieldStatus();
			
		if(tmpX == newX && tmpY == newY && !special)
			return true;
		else if(tmpStatus == FieldStatus.UNAVAILABLE )
		{
			tmpX = tmpX;
			tmpY = tmpY - 1;
			
			if(tmpX == newX && tmpY == newY)
			{
				endTurn = false;
				return true;
			}
		}
		
		//upper (6)
		
		tmpX = oldX - 1;
		tmpY = oldY;
		tmpStatus = tabField[tmpX][tmpY].getFieldStatus();
			
		if(tmpX == newX && tmpY == newY && !special)
			return true;
		else if(tmpStatus == FieldStatus.UNAVAILABLE )
		{
			tmpX = tmpX - 1;
			tmpY = tmpY;
			
			if(tmpX == newX && tmpY == newY)
			{
				endTurn = false;
				return true;
			}
		}		
		
		endTurn = false;
		return false;
	}
	
	private void addPawn(ColorPlayer color, Zone newZone)
	{
		Zone currentZone;
		Pawn newPawn;
		int number = 1;
		
		for(int i = 0; i < 19; i++)
		{
			for(int j = 0; j < 19; j++)
			{
				currentZone = tabField[i][j].getZone();
				
				if(currentZone == newZone)
				{
					newPawn = new Pawn(color, number, i, j);
					tabField[i][j].setFieldStatus(FieldStatus.UNAVAILABLE);
					tabPawn.add(newPawn);
				}
			}
		}
	}
	
	public int getCountPlayer() 
	{
		return countPlayer;
	}
	
	private void createBoard()
	{
		int i = 0; 
		
		for(int j = 0; j<19; j++)
			tabField[i][j] = new Field(FieldStatus.CLOSED , Zone.ZONE_EMPTY);

		int k = 13;
		
		for(i=1; i<5; i++)
		{
			for(int j = 0; j < k; j++)
				tabField[i][j] = new Field(FieldStatus.CLOSED, Zone.ZONE_EMPTY);
			
			for(int j = k; j < 14; j++)
				tabField[i][j] = new Field(FieldStatus.AVAILABLE, Zone.ZONE_ONE);
			
			for(int j = 14; j < 19; j++)
				tabField[i][j] = new Field(FieldStatus.CLOSED, Zone.ZONE_EMPTY);
			
			k--;	
		}
		
		k = 4;
		for(i=5; i<10; i++)
		{
			for(int j = 0; j < 5; j++)
				tabField[i][j] = new Field(FieldStatus.CLOSED, Zone.ZONE_EMPTY);
			
			int tmp = 5 + k;
			
			for(int j = 5; j < tmp; j++)
				tabField[i][j] = new Field(FieldStatus.AVAILABLE, Zone.ZONE_SIX);
			
			for(int j = tmp; j < 14; j++)
				tabField[i][j] = new Field(FieldStatus.AVAILABLE, Zone.ZONE_CENTER);
			
			tmp = 14 + k;
			
			for(int j = 14; j < tmp; j++)
				tabField[i][j] = new Field(FieldStatus.AVAILABLE, Zone.ZONE_TWO);
			
			for(int j = tmp; j < 19; j++)
				tabField[i][j] = new Field(FieldStatus.CLOSED, Zone.ZONE_EMPTY);
			
			k--;
			
		}
		
		
		k = 4;
		int c = 8;
		
		for(i=10; i<14; i++)
		{
			for(int j = 0; j < k; j++)
				tabField[i][j] = new Field(FieldStatus.CLOSED, Zone.ZONE_EMPTY);
			
			
			for(int j = k; j < 5; j++)
				tabField[i][j] = new Field(FieldStatus.AVAILABLE, Zone.ZONE_FIVE);
			
			int tmp = 5 + c;
			
			for(int j = 5; j < tmp; j++)
				tabField[i][j] = new Field(FieldStatus.AVAILABLE, Zone.ZONE_CENTER);
			
				
			k--;
			c--;
			
			for(int j = tmp; j < 14; j++)
				tabField[i][j] = new Field(FieldStatus.AVAILABLE, Zone.ZONE_THREE);
			
			for(int j = 14; j < 19; j++)
				tabField[i][j] = new Field(FieldStatus.CLOSED, Zone.ZONE_EMPTY);
		
		}
		
		k = 4;
		
		for(i=14; i<18; i++)
		{
			for(int j = 0; j < 5; j++)
				tabField[i][j] = new Field(FieldStatus.CLOSED, Zone.ZONE_EMPTY);
			
			int tmp = k + 5;
			
			for(int j = 5; j < tmp; j++)
				tabField[i][j] = new Field(FieldStatus.AVAILABLE, Zone.ZONE_FOUR);
			
			for(int j = tmp; j < 19; j++)
				tabField[i][j] = new Field(FieldStatus.CLOSED, Zone.ZONE_EMPTY);
			
			k--;	
		}
		
		for(int j = 0; j<19; j++)
			tabField[18][j] = new Field(FieldStatus.CLOSED , Zone.ZONE_EMPTY);
				
	}

	//test createBoard
	public void getPaint()
	{
		FieldStatus currentStatus;
		Zone currentZone;
		
		for(int i=0; i<19; i++)
		{
			for(int j=0; j<19; j++)
			{
				currentStatus = tabField[i][j].getFieldStatus();
				currentZone = tabField[i][j].getZone();
				
				if(currentStatus == FieldStatus.AVAILABLE)
				{
					if(currentZone == Zone.ZONE_ONE)
						System.out.printf("1");
					else if(currentZone == Zone.ZONE_TWO)
						System.out.printf("2");
					else if(currentZone == Zone.ZONE_THREE)
						System.out.printf("3");
					else if(currentZone == Zone.ZONE_FOUR)
						System.out.printf("4");
					else if(currentZone == Zone.ZONE_FIVE)
						System.out.printf("5");
					else if(currentZone == Zone.ZONE_SIX)
						System.out.printf("6");
					else
						System.out.printf("0");
				}
				else if(currentStatus == FieldStatus.CLOSED)
				{
					System.out.printf("_");
				}
				else if(currentStatus == FieldStatus.UNAVAILABLE)
				{
					System.out.printf("8");
				}
			}
			
			
			System.out.printf("\n");
		}
	}

}

package Game;

import java.util.ArrayList;



public class Game 
{
	private Field[][] tabField;
	private int countPlayer;
	
	private ArrayList<Pawn> tabPawn;
	
	public Game()
	{
		tabField = new Field[19][19];
		tabPawn = new ArrayList<Pawn>();
		
		createBoard();	
	}
	
	public ColorPlayer addPalyer()
	{
		
		if(countPlayer == 0)
		{
			addPawn(ColorPlayer.BLUE, Zone.ZONE_ONE);
			countPlayer++;
			
			return ColorPlayer.BLUE;
		}
		else if(countPlayer == 1)
		{
			addPawn(ColorPlayer.BROWN, Zone.ZONE_FOUR);
			countPlayer++;
			
			return ColorPlayer.BROWN;
		}
		else if(countPlayer == 2)
		{
			addPawn(ColorPlayer.GREEN, Zone.ZONE_TWO);
			countPlayer++;
			
			return ColorPlayer.GREEN;
		}
		else if(countPlayer == 3)
		{
			addPawn(ColorPlayer.ORANGE, Zone.ZONE_FIVE);
			countPlayer++;
			
			return ColorPlayer.ORANGE;
		}
		else if(countPlayer == 4)
		{
			addPawn(ColorPlayer.RED, Zone.ZONE_THREE);
			countPlayer++;
			
			return ColorPlayer.RED;
		}
		else if(countPlayer == 5)
		{
			addPawn(ColorPlayer.YELLOW, Zone.ZONE_SIX);
			countPlayer++;
			
			return ColorPlayer.YELLOW;
		}
		else
		{
			return ColorPlayer.EMPTY;
		}

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
			for(int j = 0; j < 4; j++)
				tabField[i][j] = new Field(FieldStatus.CLOSED, Zone.ZONE_EMPTY);
			
			int tmp = k + 4;
			
			for(int j = 4; j < tmp; j++)
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

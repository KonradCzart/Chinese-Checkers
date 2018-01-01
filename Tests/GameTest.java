package Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Game.BadPlayerException;
import Game.ColorPlayer;
import Game.Game;
import Game.IncorrectMoveException;
import Game.Zone;

public class GameTest {

	
	private Game myGame;
	
	@Before
	public void setUp()
	{
		myGame = new Game(258);
		myGame.addPalyer();
		myGame.addPalyer();
		myGame.addPalyer();
		myGame.startGame();
	}

	@After
	public void tearDown()
	{
		myGame = null;
	}
	@Test
	public void testMoveValidate() {
		assertTrue(myGame.moveValidate(4, 10, 5, 9, false));
		assertTrue(myGame.moveValidate(4, 10, 5, 10, false));
		
		assertTrue(myGame.moveValidate(7, 6, 8, 6, false));
		assertTrue(myGame.moveValidate(7, 6, 7, 7, false));
		
		assertFalse(myGame.moveValidate(7, 6, 8, 7, false));
		assertFalse(myGame.moveValidate(14, 6, 13, 5, false));
		assertFalse(myGame.moveValidate(8, 14, 7, 13, false));
	}
	
	@Test
	public void testMove()
	{
//		
//		try {
//			myGame.move(ColorPlayer.PLAYER_ONE, 4, 10, 5, 10);
//			myGame.move(ColorPlayer.PLAYER_ONE, 4, 11, 6, 9);
//		} catch (BadPlayerException e) {
//			fail();
//		} catch (IncorrectMoveException e) {
//			fail();
//		}
	}
	
	@Test
	public void testPawnInZone()
	{
		assertTrue(myGame.pawnInZone(ColorPlayer.PLAYER_ONE, Zone.ZONE_ONE, 10));
		assertTrue(myGame.pawnInZone(ColorPlayer.PLAYER_TWO, Zone.ZONE_TWO, 10));
		assertTrue(myGame.pawnInZone(ColorPlayer.PLAYER_FOUR, Zone.ZONE_FOUR, 10));
	}
	


}

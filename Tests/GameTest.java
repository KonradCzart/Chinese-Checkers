package Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Game.BadPlayerException;
import Game.ColorPlayer;
import Game.Game;
import Game.IncorrectMoveException;
import Server.ThreadedServer;
import junit.framework.Assert;

public class GameTest {

	
	Game myGame;
	
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
		Assert.assertTrue(myGame.moveValidate(4, 10, 5, 9));
		Assert.assertTrue(myGame.moveValidate(4, 10, 5, 10));
		
		Assert.assertTrue(myGame.moveValidate(7, 6, 8, 6));
		Assert.assertTrue(myGame.moveValidate(7, 6, 7, 7));
		
		Assert.assertFalse(myGame.moveValidate(7, 6, 8, 7));
		Assert.assertFalse(myGame.moveValidate(14, 6, 13, 5));
		Assert.assertFalse(myGame.moveValidate(8, 14, 7, 13));
	}
	
	@Test
	public void testMove()
	{
		
		try {
			myGame.move(ColorPlayer.PLAYER_ONE, 4, 10, 5, 10);
			myGame.move(ColorPlayer.PLAYER_ONE, 4, 11, 6, 9);
		} catch (BadPlayerException e) {
			Assert.fail();
		} catch (IncorrectMoveException e) {
			Assert.fail();
		}
	}
	@Test
	public void testMoveQueue()
	{
		try {
			myGame.move(ColorPlayer.PLAYER_TWO, 14, 5, 13, 5);
			Assert.fail();
		} catch (BadPlayerException e) {
		} catch (IncorrectMoveException e) {
		}
		
		try {
			myGame.endMove(ColorPlayer.PLAYER_ONE);
			myGame.move(ColorPlayer.PLAYER_TWO, 14, 5, 13, 5);
			myGame.endMove(ColorPlayer.PLAYER_TWO);
			myGame.move(ColorPlayer.PLAYER_THREE, 5, 14, 5, 13);
			myGame.endMove(ColorPlayer.PLAYER_THREE);
			myGame.move(ColorPlayer.PLAYER_ONE, 4, 13, 6, 13);
		} catch (BadPlayerException e) {
			Assert.fail();
		} catch (IncorrectMoveException e) {
			Assert.fail();
		}

	}

}
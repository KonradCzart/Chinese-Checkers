package Message;

/**
 * @author Konrad Czart
 * Class for sending messages with join status
 */
public class JoinGameMessage implements Message
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4509545077265697349L;

	private int idGame;
	private Boolean newGame;
	
	public JoinGameMessage(int id, Boolean newGame)
	{
		this.idGame = id;
		this.newGame = newGame;
	}
	
	public int getIdGame()
	{
		return idGame;
	}
	
	public Boolean getNewGame()
	{
		return newGame;
	}
}

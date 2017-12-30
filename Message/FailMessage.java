package Message;

/*
 * @author Konrad Czart
 * Class for sending messages with errors
 */
public class FailMessage 
{
	private int codFail;
	private String description;
	
	public FailMessage(int codFail, String description)
	{
		this.codFail = codFail;
		this.description = description;
	}
	
	public int getCodFail()
	{
		return codFail;
	}
	
	public String getDescription()
	{
		return description;
	}

}

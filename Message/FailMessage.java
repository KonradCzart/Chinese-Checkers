package Message;

/*
 * @author Konrad Czart
 * Class for sending messages with errors
 */
public class FailMessage implements Message
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2216660814983659713L;
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

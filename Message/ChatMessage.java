package Message;

public class ChatMessage implements Message
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 455332486206856176L;


	private String description;
	
	public ChatMessage(String description)
	{
		this.description = description;

	}
	

	public String getDescription()
	{
		return description;
	}

}

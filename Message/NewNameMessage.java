package Message;

public class NewNameMessage implements Message
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1394397776322815349L;

	
	private String name;
	
	public NewNameMessage(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
}

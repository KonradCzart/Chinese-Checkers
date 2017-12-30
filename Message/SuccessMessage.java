package Message;

/*
 * @author Konrad Czart
 * Class for sending messages with success
 */
public class SuccessMessage 
{
	private int codSuccess;
	private String description;
	
	public SuccessMessage(int codSuccess, String description)
	{
		this.codSuccess = codSuccess;
		this.description = description;
	}
	
	public int getCodSuccess()
	{
		return codSuccess;
	}
	
	public String getDescription()
	{
		return description;
	}
}

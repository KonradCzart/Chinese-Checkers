package Game;

public class Field 
{
	FieldStatus status;
	Zone zone;
	
	
	public Field()
	{
		status = FieldStatus.CLOSED;
		zone = Zone.ZONE_EMPTY;
	}
	
	public Field(FieldStatus status, Zone zone)
	{
		this.status = status;
		this.zone = zone;
	}
	
	public void setFieldStatus(FieldStatus newStatus)
	{
		status = newStatus;
	}
	
	public FieldStatus getFieldStatus()
	{
		return status;
	}
	
	public void setZone(Zone newZone)
	{
		zone = newZone;
	}
	
	public Zone getZone()
	{
		return zone;
	}
}

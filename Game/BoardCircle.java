package Game;

import javafx.scene.shape.Circle;

/**
 * Created by Kacper on 2017-12-30.
 */
public class BoardCircle extends Circle
{
	private ColorPlayer colorPlayer;
	private FieldStatus fieldStatus;

	public BoardCircle(FieldStatus fieldStatus)
	{
		this.fieldStatus = fieldStatus;
	}

	public ColorPlayer getColorPlayer()
	{
		return colorPlayer;
	}

	public FieldStatus getFieldStatus()
	{
		return fieldStatus;
	}

	public void setColorPlayer(ColorPlayer colorPlayer) {
		this.colorPlayer = colorPlayer;
	}

	public void setFieldStatus(FieldStatus fieldStatus) {
		this.fieldStatus = fieldStatus;
	}
}

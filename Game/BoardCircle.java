package Game;

import javafx.scene.shape.Circle;

/**
 * Created by Kacper on 2017-12-30.
 */
public class BoardCircle extends Circle
{
	private ColorPlayer colorPlayer;
	private FieldStatus fieldStatus;

	private int x;

	private int y;
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

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}

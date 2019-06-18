package algorithm;

import org.opencv.core.Point;

public class ShortPoint {
	
	short x;
	short y;

	
	public ShortPoint(short x, short y) {
		super();
		this.x = x;
		this.y = y;
	}


	public short getX() {
		return x;
	}


	public void setX(short x) {
		this.x = x;
	}


	public short getY() {
		return y;
	}


	public void setY(short y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Point) {
			Point oPoint = (Point) o;
			return (this.x == (short) oPoint.x && this.y == (short) oPoint.y);
		} else if(o instanceof ShortPoint) {
			ShortPoint shPoint = (ShortPoint) o;
			return (this.x == shPoint.x && this.y == shPoint.y);
		}
		return false;
	}
	
	

}

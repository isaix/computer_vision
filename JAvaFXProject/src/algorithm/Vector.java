package algorithm;

public class Vector {
	
	double x;
	double y;
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double calculateAngle(Vector otherVector) {
		double cosV = (this.dotProduct(otherVector))/(this.getLength() * otherVector.getLength());
		
		return Math.toDegrees(Math.acos(cosV));
	}
	
	public double calculateAbsoluteAngle(Vector otherVector) {
		double v = Math.atan2(otherVector.y, otherVector.x) - Math.atan2(this.y, this.x);
		if(v < 0) {
			v += 2 * Math.PI;
		}
		return Math.toDegrees(v);
		
	}
	
	public double getLength() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public double dotProduct(Vector otherVector) {
		return this.x*otherVector.x + this.y*otherVector.y;
	}
	
	public double crossProduct(Vector otherVector) {
		return this.x*otherVector.y - this.y*otherVector.x;
	}
	
	public Vector getNormalizedVector() {
		double length = this.getLength();
		return new Vector(x/length, y/length);
	}
	
	public Vector getTvaerVector() {
		return new Vector(-this.y, this.x);
	}
	
	public Vector getOppositeVector() {
		return new Vector(-this.x, -this.y);
	}
	
	public Vector mulitply(double factor) {
		return new Vector(x * factor, y * factor);
	}
}

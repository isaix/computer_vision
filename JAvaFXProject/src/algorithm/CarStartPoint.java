package algorithm;

import java.util.ArrayList;

import org.opencv.core.Point;

public class CarStartPoint {

	public ArrayList<Point> carPoints;
	public double carAngle;
	
	public CarStartPoint(ArrayList<Point> carPoints, double carAngle) {
		this.carPoints = carPoints;
		this.carAngle = carAngle;
	}
	
}

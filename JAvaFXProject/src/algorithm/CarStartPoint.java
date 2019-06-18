package algorithm;

import java.util.ArrayList;

import org.opencv.core.Point;

public class CarStartPoint {

	public ArrayList<Point> carPoints = new ArrayList<Point>();
	public double carAngle;
	
	public CarStartPoint(ArrayList<Point> carPoints, double carAngle) {
		this.carPoints.addAll(carPoints);
		this.carAngle = carAngle;
	}
	
}

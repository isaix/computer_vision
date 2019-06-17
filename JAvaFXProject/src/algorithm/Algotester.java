package algorithm;

import org.opencv.core.Point;

public class Algotester {
	
	public static void main(String[] args) {
		Node node1 = new Node(0, 0);
		Node node2 = new Node(100, 100);
		Point[] points = AlgorithmController.getPoints(10, node1, node2);
		for(Point point : points) {
			System.out.println("X:" + point.x + " Y:" + point.y);
		}
	}

}

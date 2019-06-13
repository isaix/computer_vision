package algorithm;

import java.util.*;
import java.util.Map.Entry;

import org.opencv.core.Point;

public class Algotester {
	
	public static void main(String[] args) {
		int[][] testArray = new int[1920][1080];
		Random r = new Random();
		
		ArrayList<Point> somePoints = new ArrayList<Point>();
		for(int i = 0; i < testArray.length; i++) {
			for(int j = 0; j < testArray[i].length; j++) {
				
				if(r.nextInt(200000) == 1) {
					somePoints.add(new Point(i, j));
				}
				
				
			}
		}

		ArrayList<Node> nodeList = AlgorithmController.ConvertToGraph(somePoints);
		/*
		for(Node node : nodeList) {
			System.out.println(node.getNumber());
			System.out.println("x: " + node.getX());
			System.out.println("y: " + node.getY());
			System.out.println("Distances");
			HashMap<Integer, Integer> distances = node.getDistances();
			for(Entry<Integer, Integer> entry : distances.entrySet()) {
				System.out.println(entry.getKey());
				System.out.println(entry.getValue());
				System.out.println();
			}
		}
		*/
		ArrayList<Node> mst = AlgorithmController.convertToMST(nodeList, nodeList.get(0));
		
		for(Node node : mst) {
			System.out.println(node.getNumber());
			//System.out.println("x: " + node.getX());
			//System.out.println("y: " + node.getY());
			System.out.println("Neighbors");
			//System.out.println(node.getDistances().size());
			//System.out.println();
			//System.out.println();
			//System.out.println("Distances");
			HashMap<Integer, Double> distances = node.getDistances();
			
			for(Entry<Integer, Double> entry : distances.entrySet()) {
				System.out.println(entry.getKey());
				//System.out.println(entry.getValue());
				System.out.println();
			}
			System.out.println();
		}
		ArrayList<Integer> order = AlgorithmController.performDFS(mst, mst.get(0));
		
		for(Integer inte: order) {
			System.out.print(inte + " ");
		}
		
		Move move = AlgorithmController.calculateMove(nodeList, 0, 1);
		System.out.println("distance " + move.getDistance());
		System.out.println("angle " + move.getAngle());
		
		
	}
	
	
}

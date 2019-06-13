package Algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;
import org.opencv.core.*;


public class AlgorithmController {

	static public ArrayList<Integer> order;

	public static ArrayList<Node> ConvertToGraph(ArrayList<Point> points) {

		int number = 0;
		ArrayList<Node> nodes = new ArrayList<Node>();
		for(int i = 0; i < points.size(); i++) {
			nodes.add(new Node(i, (int)points.get(i).x, (int)points.get(i).y));
		}
		
		/*
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				if(matrix[i][j] == 1) {
					nodes.add(new Node(number, j, i));
					number++;
				}
			}
		}
		*/

		for(int i = 0; i < nodes.size(); i++) {
			Node from = nodes.get(i);
			for(int j = i+1; j < nodes.size(); j++) {
				Node to = nodes.get(j);
				double dist = calculateDistance(from, to);
				to.addDistance(from.getNumber(), dist);
				from.addDistance(to.getNumber(), dist);
			}
		}
		return nodes;
	}

	//Make a minimum spanning tree using Prim's Algorithm
	public static ArrayList<Node> convertToMST(ArrayList<Node> graph, Node start){
		HashMap<Integer,Node> mst = new HashMap<Integer,Node>();

		mst.put(start.getNumber(), new Node(start.getNumber(), start.getX(), start.getY()));
		ArrayList<Integer> visited = new ArrayList<Integer>();
		visited.add(start.getNumber());

		for(int i = 0; i < graph.size()-1; i++) {
			Double curShortest = Double.MAX_VALUE;
			Node curShortestNode = null;
			Node curShortestNodeFrom = null;
			for(Node node : graph) {
				if(visited.contains(node.getNumber())) {
					for(Entry<Integer, Double> entry: node.getDistances().entrySet()) {
						if(!visited.contains(entry.getKey()) && entry.getValue() < curShortest) {
							curShortest = entry.getValue();
							curShortestNode = graph.get(entry.getKey());
							curShortestNodeFrom = mst.get(node.getNumber());
						}
					}
				}
			}

			Node newNode = new Node(curShortestNode.getNumber(), curShortestNode.getX(), curShortestNode.getY());
			double distance = calculateDistance(newNode, curShortestNodeFrom);
			newNode.addDistance(curShortestNodeFrom.getNumber(), distance);
			curShortestNodeFrom.addDistance(newNode.getNumber(), distance);
			mst.put(newNode.getNumber(), newNode);
			visited.add(newNode.getNumber());
		}

		ArrayList<Node> mstFinal = new ArrayList<Node>();
		for(Entry<Integer, Node> node : mst.entrySet()) {
			mstFinal.add(node.getValue());
		}

		return mstFinal;
	}

	//Performs a DFS and returns a list holding the order in which the nodes were visited
	public static ArrayList<Integer> performDFS(ArrayList<Node> graph, Node node){
		if(order == null) {
			order = new ArrayList<Integer>();
		}
		
		order.add(node.getNumber());
		HashMap<Integer, Double> distances = node.getDistances();
		for(Entry<Integer, Double> entry : distances.entrySet()) {
			if(!order.contains(entry.getKey())) {
				performDFS(graph, graph.get(entry.getKey()));
			}
		}
		//order.add(node.getNumber());
		
		return order;
	}
	
	//Calculate the move needed to move from current position to next ball
	public static Move calculateMove(ArrayList<Node> graph, int fromIndex, int toIndex) {
		Move move = new Move();
		Node from = graph.get(fromIndex);
		Node to = graph.get(toIndex);
		double dist = to.getDistances().get(from.getNumber());
		int a = Math.abs(to.getX() - from.getX());
		int b = Math.abs(to.getY() - from.getY());
		move.setDistance(dist);
		double cosC = (Math.pow(a, 2) + Math.pow(dist, 2) - Math.pow(b, 2))/(2*a*dist);
		move.setAngle(Math.toDegrees(Math.acos(cosC)));
		
		return move;
	}
	
	public static void resetOrder(){
		order = new ArrayList<Integer>();
	}

	//Calculates total distance (in matrix coordinates) using Pythagoras
	private static double calculateDistance(Node from, Node to){
		int a = Math.abs(from.getX() - to.getX());
		int b = Math.abs(from.getY() - to.getY());

		return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
	}


	//Solve the travelling salemans problem in constant time
	public static ArrayList<Integer> solveTravelingSalesmanProblemInConstantTime() {
		//TODO: Implement
		return null;
	}

}

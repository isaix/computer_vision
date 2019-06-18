package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;
import org.opencv.core.*;

import algorithm.Move;

public class AlgorithmController {

	static ArrayList<Integer> order;
	static int area = 77;

	public static ArrayList<Node> ConvertToGraph(ArrayList<Point> points, ArrayList<Point> car, ArrayList<ShortPoint> redPoints) {
		
		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node(0, (int)car.get(0).x, (int)car.get(0).y ));
		for(int i = 0; i < points.size(); i++) {
			BallTypes curballtype = findBallType( points.get(i), redPoints);
			
			Node ballNode = new Node(i+1, (int)points.get(i).x, (int)points.get(i).y);
			HelperNode helperNode;
			System.out.println("THIS IS THE BALL COORDINATES");
			System.out.print("X: " + points.get(i).x + "Y:" + points.get(i).y);
			System.out.println(curballtype.toString());
			switch(curballtype) {
			
			case NORMAL:
				nodes.add(ballNode);
				break;
			case WALL_LEFT:
				helperNode = new HelperNode(i+1, (int)points.get(i).x + 2*area, (int)points.get(i).y - area);
				helperNode.setNodePointer(ballNode);
				nodes.add(helperNode);
				break;
			case WALL_RIGHT:
				helperNode = new HelperNode(i+1, (int)points.get(i).x - 2*area, (int)points.get(i).y + area);
				helperNode.setNodePointer(ballNode);
				nodes.add(helperNode);
				break;
			case WALL_TOP:
				helperNode = new HelperNode(i+1, (int)points.get(i).x - area, (int)points.get(i).y - 2*area);
				helperNode.setNodePointer(ballNode);
				nodes.add(helperNode);
				break;
			case WALL_BOTTOM:
				helperNode = new HelperNode(i+1, (int)points.get(i).x + area, (int)points.get(i).y + 2*area);
				helperNode.setNodePointer(ballNode);
				nodes.add(helperNode);
				break;
			case CORNER_TOPRIGHT:
				helperNode = new HelperNode(i+1, (int)points.get(i).x - 2*area, (int)points.get(i).y - 2*area);
				helperNode.setNodePointer(ballNode);
				nodes.add(helperNode);
				break;
			case CORNER_TOPLEFT:
				helperNode = new HelperNode(i+1, (int)points.get(i).x + 2*area, (int)points.get(i).y - 2*area);
				helperNode.setNodePointer(ballNode);
				nodes.add(helperNode);
				break;
			case CORNER_BOTTOMLEFT:
				helperNode = new HelperNode(i+1, (int)points.get(i).x + 2*area, (int)points.get(i).y + 2*area);
				helperNode.setNodePointer(ballNode);
				nodes.add(helperNode);
				break;
			case CORNER_BOTTOMRIGHT:
				helperNode = new HelperNode(i+1, (int)points.get(i).x - 2*area, (int)points.get(i).y + 2*area);
				helperNode.setNodePointer(ballNode);
				nodes.add(helperNode);
				break;
				
			}
			
		}

		for(int i = 1; i < nodes.size(); i++) {
			Node to = nodes.get(i);
			System.out.println("X: " + nodes.get(i).getX() + "Y: " + nodes.get(i).getY());
			if(isPossibleMove(car, to, redPoints)) {
				nodes.get(0).addDistance(i, calculateDistance(nodes.get(0), to));
				nodes.get(i).addDistance(0, calculateDistance(nodes.get(0), to));
			}
			/*
			for(int j = i+1; j < nodes.size(); j++) {
				Node to = nodes.get(j);
				double dist = calculateDistance(from, to);
				
				if(isPossibleMove()) {
					to.addDistance(from.getNumber(), dist);
					from.addDistance(to.getNumber(), dist);
				}
			}
			*/
		}
		return nodes;
	}
	
	private static BallTypes findBallType(Point point, ArrayList<ShortPoint> redPoints) {
		int wallCount = 0;
		boolean top = false;
		boolean bottom = false;
		boolean left = false;
		boolean right = false;
		
		for(double i = point.x; i < point.x + 78; i++) {
			//System.out.println("AHHHHHHHH");
			if(redPoints.contains(new Point(i, point.y))) {
				right = true;
				wallCount++;
				break;
			}
		}
		for(double i = point.x; i > point.x - 78; i--) {
			if(redPoints.contains(new Point(i, point.y))) {
				left = true;
				wallCount++;
				break;
			}
		}
		for(double i = point.y; i < point.y + 78; i++) {
			if(redPoints.contains(new Point(point.x, i))) {
				top = true;
				wallCount++;
				break;
			}
		}
		for(double i = point.y; i > point.y - 78; i--) {
			if(redPoints.contains(new Point(point.x, i))) {
				bottom = true;
				wallCount++;
				break;
			}
		}
		//System.out.println("WallCount: " + wallCount);
		
		if(top && left) {
			return BallTypes.CORNER_TOPLEFT;
		} else if(top && right) {
			return BallTypes.CORNER_TOPRIGHT;
		} else if(bottom && left) {
			return BallTypes.CORNER_BOTTOMLEFT;
		} else if(bottom && right) {
			return BallTypes.CORNER_BOTTOMRIGHT;
		} else if(left) {
			return BallTypes.WALL_LEFT;
		} else if(right) {
			return BallTypes.WALL_RIGHT;
		} else if(top) {
			return BallTypes.WALL_TOP;
		} else if(bottom) {
			return BallTypes.WALL_BOTTOM;
		} else return BallTypes.NORMAL;
		
	}

	private static boolean isPossibleMove(ArrayList<Point> car, Node to, ArrayList<ShortPoint> redPoints) {
		int tolerance = 108;
		Point middlePoint = new Point((car.get(1).x + car.get(2).x)/2, (car.get(1).y + car.get(2).y)/2);
		Vector carVector = new Vector(middlePoint.x - car.get(0).x, middlePoint.y - car.get(0).y);
		Vector tvaerVector = carVector.getTvaerVector().getNormalizedVector();
		Vector edgeVector = tvaerVector.mulitply(108);
		Node carNode1 = new Node(-1, (int) (car.get(0).x + edgeVector.getX()), (int) (car.get(0).y + edgeVector.getY()));
		Node carNode2 = new Node(-1, (int) (car.get(0).x - edgeVector.getX()), (int) (car.get(0).y - edgeVector.getY()));
		Node carMiddle = new Node(-1, (int)(car.get(0).x), (int)(car.get(0).y));
		Node[] carnodes = {carNode1, carNode2, carMiddle};

		Node ballNode1 = new Node(-1, (int)(to.getX() + edgeVector.getX()), (int)(to.getY() + edgeVector.getY()));
		Node ballNode2 = new Node(-1, (int)(to.getX() - edgeVector.getX()), (int)(to.getY() - edgeVector.getY()));
		Node[] ballnodes = {ballNode1, ballNode2, to};

		for(int i = 0; i < carnodes.length; i++) {
			for(Point point : getPoints(20, carnodes[i], ballnodes[i])) {
				if(redPoints.contains(point)) {
					//System.out.println("NOT POSSIBLE");
					return false;
				}
			}
		}
		System.out.println("POSSIBLE");
		return true;		
	}

	public static Point[] getPoints(int howManyPoints, Node node1, Node node2) {
		Point[] points = new Point[howManyPoints];
		int yDiff = node2.getY() - node1.getY();
		int xDiff = node2.getX() - node1.getX();
		//System.out.println("DIVIDER MED NOGET ANDET END  0, PLS");
		//System.out.println(node2.getX());
		//System.out.println(node1.getX());
		double slope = (double)((node2.getY() - node1.getY())/(node2.getX() - node1.getX()));
		double x, y;

		howManyPoints--;
		for(double i = 0; i < howManyPoints; i++) {
			y = slope == 0 ? 0 : yDiff * (i / howManyPoints);
			x = slope == 0 ? xDiff * (i / howManyPoints) : y / slope;
			points[(int)i] = new Point((int)Math.round(x) + node1.getX(), (int)Math.round(y) + node1.getY());

		}
		points[howManyPoints] = new Point(node2.getX(), (int)node2.getY() );
		return points;

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
	public static int findNearestBall(ArrayList<Node> graph) {
		Node car = graph.get(0);
		double shortestDistance = Double.MAX_VALUE;
		int shortestBall = -1;
		for(Entry<Integer, Double> entry : car.getDistances().entrySet()) {
			if(entry.getValue() < shortestDistance) {
				shortestBall = entry.getKey();
				shortestDistance = entry.getValue();
			}
		}
		return shortestBall;

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

	public static void resetOrder() {
		order = new ArrayList<Integer>();
	}

	//Calculate the move needed to move from current position to next ball
	public static Move calculateMove(ArrayList<Node> graph, ArrayList<Point> car, int toIndex) {
		Move move = new Move();
		Point middlePoint = new Point((car.get(1).x + car.get(2).x)/2,(car.get(1).y - car.get(2).y)/2);

		Node A = new Node((int)car.get(0).x, (int)car.get(0).y);

		Node B = new Node((int) middlePoint.x, (int) middlePoint.y);

		Node to = graph.get(toIndex);
		//Code to figure out if car should turn left or right
		double camper = (car.get(0).y - middlePoint.y)/(car.get(0).x - middlePoint.x);
		double intersection = middlePoint.y - camper * middlePoint.x;
		double lineY = camper * to.getX() + intersection;



		double dist = calculateDistance(B, to);

		double a = dist;
		double b = calculateDistance(A, to);
		double c = calculateDistance(A, B);

		move.setDistance(dist);

		/*
		double cosC = (Math.pow(a, 2) + Math.pow(c, 2) - Math.pow(b, 2))/(2*a*c);
		move.setAngle(Math.toDegrees(Math.acos(cosC)));
		if(lineY < to.getY()) {
			move.setAngle(-move.getAngle());
		}
		if(car.get(0).x > middlePoint.x) {
			move.setAngle(-move.getAngle());
		}
		 */

		double slope1 = calculateSlope(B.getX(), A.getX(), B.getY(), A.getY());
		double slope2 = calculateSlope(to.getX(), B.getX(), to.getY(), B.getY());

		double angle = calculateAngleOfLines(slope1, slope2);
		move.setAngle(angle);

		return move;
	}


	public static ArrayList<Move> calculateMoveButThisOneIsBetterBecauseWeUseVectors(ArrayList<Node> graph, ArrayList<Point> car, int toIndex) {
		//System.out.println(car.get(1).x);
		//System.out.println(car.get(2).x);
		Point middlePoint = new Point((car.get(1).x + car.get(2).x)/2, (car.get(1).y + car.get(2).y)/2);
		//System.out.println("middle" + middlePoint.x + ", " + middlePoint.y);
		Node ball = graph.get(toIndex);
		if(ball instanceof HelperNode) {
			return calculateMoveButThisOneIsOnlyUsedWhenItIsAHelperNode(car, (HelperNode) ball);
		}

		Vector carVector = new Vector(car.get(0).x - middlePoint.x, car.get(0).y - middlePoint.y);
		//System.out.println(carVector.getX() + ", " + carVector.getY());
		Vector ballVector = new Vector(ball.getX() - middlePoint.x, ball.getY() - middlePoint.y);
		//System.out.println(ballVector.getX() + ", " + ballVector.getY());
		double angle = carVector.calculateAngle(ballVector);
		double length = calculateDistance(ball, new Node(-1, (int) middlePoint.x, (int) middlePoint.y));

		if(carVector.crossProduct(ballVector) > 0) {
			angle = -angle;
		}
		Move move = new Move();
		move.setAngle(angle);
		move.setDistance((length/777)*100-10);
		//System.out.println("DISTNACE: " + length);
		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(move);
		return moves;

	}
	
	public static ArrayList<Move> calculateMoveButThisOneIsOnlyUsedWhenItIsAHelperNode(ArrayList<Point> car, HelperNode helperNode){
		
		ArrayList<Move> moves = new ArrayList<Move>();
		Point middlePoint = new Point((car.get(1).x + car.get(2).x)/2, (car.get(1).y + car.get(2).y)/2);
		System.out.println("middle" + middlePoint.x + ", " + middlePoint.y);
		
		Vector carVector = new Vector(car.get(0).x - middlePoint.x, car.get(0).y - middlePoint.y);
		System.out.println(carVector.getX() + ", " + carVector.getY());
		Vector helperVector = new Vector(helperNode.getX() - middlePoint.x, helperNode.getY() - middlePoint.y);
		System.out.println(helperVector.getX() + ", " + helperVector.getY());
		double angle = carVector.calculateAngle(helperVector);
		double length = calculateDistance(helperNode, new Node(-1, (int) middlePoint.x, (int) middlePoint.y));

		if(carVector.crossProduct(helperVector) > 0) {
			angle = -angle;
		}
		Move move = new Move();
		move.setAngle(angle);
		move.setDistance((length/777)*100-10);
		moves.add(move);
		
		Vector ballVector = new Vector(helperNode.getNodePointer().getX() - helperNode.getX(), helperNode.getNodePointer().getY() - helperNode.getY());
		angle = helperVector.calculateAngle(ballVector);
		
		if(helperVector.crossProduct(ballVector) > 0) {
			angle = -angle;
		}
		
		length = calculateDistance(helperNode, helperNode.getNodePointer());
		Move move2 = new Move();
		move2.setAngle(angle*0.5);
		move2.setDistance((length/777)*100-10);
		moves.add(move2);
		Move move3 = new Move();
		move3.setAngle(0);
		move3.setDistance(-move2.getDistance());
		moves.add(move3);
		System.out.println("MANGE MANGE MOVES " + moves.size());
		return moves;
		
		
		
		/*
		System.out.println("Collect ball now");
		if(true) {
			System.out.println("We are done now");
			try {
				Robot robot = new Robot();
				robot.selfDestroy();
				robot.out!
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		*/
		
	}

	public static double calculateAngleOfLines(double a1, double a2) {

		double angle = 180 - Math.abs(Math.atan(a1) - Math.atan(a2));

		return angle;
	}

	public static double calculateSlope(double x1, double x2, double y1, double y2) {

		double a = (y2 - y1)/(x2 - x1);
		return a;
	}

	public static double calculateCarAngle(ArrayList<Point> car) {
		Point middlePoint = new Point((car.get(1).x + car.get(2).x)/2, (car.get(1).y + car.get(2).y)/2);
		double a = car.get(0).x - middlePoint.x;
		double b = car.get(0).y - middlePoint.y;
		double dist = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
		double cosC = (Math.pow(a, 2) + Math.pow(dist, 2) - Math.pow(b, 2))/(2*a*dist);
		return Math.toDegrees(Math.acos(cosC));
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

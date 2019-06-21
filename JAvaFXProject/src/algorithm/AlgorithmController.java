package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;
import org.opencv.core.*;

import algorithm.Move;
import carConnection.SocketRunnable;

public class AlgorithmController {

	static ArrayList<Integer> order;
	static final int TI_CM = 77;

	public static ArrayList<Node> ConvertToGraph(ArrayList<Point> points, ArrayList<Point> car, ArrayList<ShortPoint> redPoints) {


		ArrayList<Node> nodes = new ArrayList<Node>();
		nodes.add(new Node(0, (int)car.get(0).x, (int)car.get(0).y ));

		for(int i = 0; i < points.size(); i++) {
			Node ballNode = new Node(i+1, (int)points.get(i).x, (int)points.get(i).y);
			findBallType(ballNode, redPoints);
			nodes.add(ballNode);
		}

		for(int i = 1; i < nodes.size(); i++) {

			if(nodes.get(i).getType().equals(BallTypes.NORMAL)) {
				Node to = nodes.get(i);
				//Sytem.out.println("X: " + nodes.get(i).getX() + "Y: " + nodes.get(i).getY());
				//if(isPossibleMove(car, to, redPoints)) {
					nodes.get(0).addDistance(i, calculateDistance(nodes.get(0), to));
					nodes.get(i).addDistance(0, calculateDistance(nodes.get(0), to));
				//}
			}
			else {
				Node to = nodes.get(i).getHelperNode();
				//if(isPossibleMove(car, to, redPoints)) {
					nodes.get(0).addDistance(i, calculateDistance(nodes.get(0), to));
					nodes.get(i).addDistance(0, calculateDistance(nodes.get(0), to));
				//}
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

	private static void findBallTypesButThisOneTakesANodeAsArgument(Node ballNode, ArrayList<ShortPoint> redPoints) {

		double shortestDistance = Double.MAX_VALUE;
		ShortPoint closestPoint = null;
		ArrayList<ShortPoint> closeToBall = new ArrayList<ShortPoint>();
		int count = 0;
		for(short i = (short)(ballNode.getX()-2*TI_CM); i<(short)(ballNode.getX()+2*TI_CM); i++){

			for(short j = (short)(ballNode.getY()-2*TI_CM); j<(short)(ballNode.getY()+2*TI_CM); j++) {
				if(redPoints.contains(new ShortPoint(i, j))) {
					count++;

					double distance = calculateDistance(ballNode, new Node(i, j));
					if(distance < shortestDistance) {
						System.out.println("x,y: "+i+","+j+" dis: "+distance);
						shortestDistance = distance;
						closestPoint = new ShortPoint(i, j);
						closeToBall.add(closestPoint);
					}
				}

			}			
		}
		System.out.println("COUNT:" + count);

		if(shortestDistance == Double.MAX_VALUE) {
			ballNode.setType(BallTypes.NORMAL);
			return;
		}

		Vector shortestVector = new Vector(closestPoint.x - ballNode.getX(), closestPoint.y - ballNode.getY());
		shortestVector = shortestVector.getNormalizedVector();
		System.out.println("ClosestPoint " + closestPoint.x + "," + closestPoint.y);
		Vector crossVector = shortestVector.getTvaerVector();

		System.out.println(crossVector.getLength());
		Node helperNode = null; 

		for(int i = 1; i <= 2*TI_CM; i++) {
			ShortPoint aPoint = new ShortPoint((short) (ballNode.getX() + (i * crossVector.getX())),(short) (ballNode.getY() + (i * crossVector.getY())));
			if(redPoints.contains(aPoint)) {
				System.out.println("ISSA CORNER BALL NR 1");
				ballNode.setType(BallTypes.CORNER);
				System.out.println("Shortest vector: " + shortestVector.getX() + ", " + shortestVector.getY() );
				System.out.println("Shortest vector: " + crossVector.getX() + ", " + crossVector.getY() );

				//Udregner HelperNode til dette punkt - kan konfigureres. 
				//Udregner position 20 cm væk fra bolden
				helperNode = new Node((int)(ballNode.getX() - TI_CM*2*shortestVector.getX()), (int)(ballNode.getY() - TI_CM*2*shortestVector.getY()));
				//Udregner position 15 cm fordrejet fra bolden yderligere
				helperNode.setX((int)(helperNode.getX() - TI_CM*1.5*crossVector.getX()));
				helperNode.setY((int)(helperNode.getY() - TI_CM*1.5*crossVector.getY()));

				ballNode.setHelperNode(helperNode);
				return;
			}
		}

		Vector oppositeCrossVector = crossVector.getOppositeVector();		

		for(int i = 1; i <= 2*TI_CM; i++) {
			ShortPoint aPoint = new ShortPoint((short) (ballNode.getX() + (i * oppositeCrossVector.getX())),(short) (ballNode.getY() + (i * oppositeCrossVector.getY())));
			if(redPoints.contains(aPoint)) {
				System.out.println("ISSA CORNER BALL NR 2");
				ballNode.setType(BallTypes.CORNER);

				//Udregner HelperNode til dette punkt - kan konfigureres. 
				//Udregner position 20 cm v�k fra bolden
				helperNode = new Node((int)(ballNode.getX() - TI_CM*2*oppositeCrossVector.getX()), (int)(ballNode.getY() - TI_CM*2*oppositeCrossVector.getY()));
				//Udregner position 15 cm fordrejet fra bolden yderligere
				helperNode.setX((int)(helperNode.getX() - TI_CM*1.5*shortestVector.getX()));
				helperNode.setY((int)(helperNode.getY() - TI_CM*1.5*shortestVector.getY()));

				ballNode.setHelperNode(helperNode);
				return;
			}
		}

		ballNode.setType(BallTypes.WALL);

		helperNode = new Node((int)((ballNode.getX() - TI_CM*2.5*crossVector.x)), (int)((ballNode.getY() - TI_CM*2.5*crossVector.y)));
		helperNode.setX((int)(helperNode.getX() - TI_CM*1.25*shortestVector.getX()));
		helperNode.setY((int)(helperNode.getY() - TI_CM*1.25*shortestVector.getY()));

		ballNode.setHelperNode(helperNode);
	}

	public static ArrayList<Move> gotoWall(ArrayList<ShortPoint> redpoints, boolean firstMove, ArrayList<Point> car) {
		ArrayList<Move> moves = new ArrayList<Move>();
		ShortPoint middlePoint = new ShortPoint((short)((car.get(1).x + car.get(2).x)/2), (short)((car.get(1).y + car.get(2).y)/2));
	
		if(firstMove) {
			Move wallMove = new Move();
			double angle = calculateCarAngle(car);
			wallMove.setAngle(-angle);
			double pixelCount = 0;
			for(short i = middlePoint.getX(); i < 1920; i++ ) {
				if(redpoints.contains(new ShortPoint(i, middlePoint.getY()))) {
					break;
				}
				pixelCount++;
			}
			wallMove.setDistance((pixelCount/777)*100-20);
			moves.add(wallMove);
			return moves;
		}
		
		Move move = new Move();
		Vector carVector = new Vector(middlePoint.x - car.get(0).x, middlePoint.y - car.get(0).y);
		Vector crossVector = carVector.getTvaerVector().getNormalizedVector().getOppositeVector();
		
		if(firstMove) {
			crossVector  = new Vector(-1,0);
		}
		double maxDist = 0;
		for(short i = 0; i < 1920; i++) {
			if(redpoints.contains(new ShortPoint((short) (middlePoint.x + (i * crossVector.getX())),(short) (middlePoint.y - (i * crossVector.getY()))))) {
				break;
			}
			maxDist++;
		}
		System.out.println("MAXDIST: " + maxDist);
		move.setAngle(-90);
		move.setDistance(((maxDist/777)*100)-20);
		moves.add(move);
		System.out.print("GOING TO WALL");
		return moves;

	}


	private static void findBallType(Node ballNode, ArrayList<ShortPoint> redPoints) {
		int wallCount = 0;
		int pixelCount = 0;
		ArrayList<ShortPoint> savedPoints = new ArrayList<ShortPoint>();

		for(double i = ballNode.getX(); i < ballNode.getX() + 2*TI_CM; i++) {
			//Sytem.out.println("AHHHHHHHH");
			ShortPoint current = new ShortPoint((short)i, (short)ballNode.getY());
			if(redPoints.contains(current)) {
				pixelCount++;
				if(pixelCount  == 5) {
					System.out.println("WALL RIGHT");
					wallCount++;
					savedPoints.add(current);
					break;
				}
			}
		}
		pixelCount = 0;
		for(double i = ballNode.getX(); i > ballNode.getX() - 2*TI_CM; i--) {
			ShortPoint current = new ShortPoint((short)i, (short)ballNode.getY());
			if(redPoints.contains(current)) {
				pixelCount++;
				if(pixelCount   == 5) {
					System.out.println("WALL LEFT");
					wallCount++;
					savedPoints.add(current);
					break;
				}
			}
		}
		pixelCount = 0;
		for(double i = ballNode.getY(); i < ballNode.getY() + 2*TI_CM; i++) {
			ShortPoint current = new ShortPoint((short)ballNode.getX(), (short)i);
			if(redPoints.contains(current)) {
				pixelCount++;
				if(pixelCount  == 5) {
					System.out.println("WALL DOWN");
					wallCount++;
					savedPoints.add(current);
					break;
				}
			}
		}
		pixelCount = 0;
		for(double i = ballNode.getY(); i > ballNode.getY() - 2*TI_CM; i--) {
			ShortPoint current = new ShortPoint((short)ballNode.getX(), (short)i);
			if(redPoints.contains(current)) {
				pixelCount++;
				if(pixelCount   == 5) {
					System.out.println("WALL TOP");
					wallCount++;
					savedPoints.add(current);
					break;
				}
			}
		}
		pixelCount = 0;
		System.out.println("WallCount: " + wallCount);

		switch(wallCount) {
		case 0: ballNode.setType(BallTypes.NORMAL);
		break;

		case 1: ballNode.setType(BallTypes.WALL);
		ballNode.setHelperNode(calculateHelperNode(savedPoints, ballNode));
		break;

		case 2: ballNode.setType(BallTypes.CORNER);
		ballNode.setHelperNode(calculateHelperNode(savedPoints, ballNode));
		return;

		default: System.out.println("FEJL - MERE END 2 WALLS");
		ballNode.setType(BallTypes.CORNER);
		ballNode.setHelperNode(calculateHelperNode(savedPoints, ballNode));
		break;
		}



		/*
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
		 */
	}

	public static Node calculateHelperNode(ArrayList<ShortPoint> savedPoints, Node ballNode) {

		if(ballNode.getType().equals(BallTypes.CORNER)) {
			Vector v2 = new Vector(savedPoints.get(0).getX() - ballNode.getX(), savedPoints.get(0).getY() - ballNode.getY());
			Vector v1 = new Vector(savedPoints.get(1).getX() - ballNode.getX(), savedPoints.get(1).getY() - ballNode.getY());

			double angle = v2.calculateAbsoluteAngle(v1);
			System.out.println("ANLGE" + angle);

			if(angle > 135 || angle < 45) {
				Vector temp = v1;
				v1 = v2;
				v2 = temp;
			}


			Vector nv1 = v1.getNormalizedVector();
			Vector cv1 = v2.getNormalizedVector();
			System.out.println("NV1 " + nv1.getX() + "," + nv1.getY());
			System.out.println("NV1 " + cv1.getX() + "," + cv1.getY());

			int helperNodeX = (int) (ballNode.getX() - (TI_CM*2*nv1.getX()) - (TI_CM*cv1.getX()) );
			int helperNodeY = (int) (ballNode.getY() - (TI_CM*2*nv1.getY()) - (TI_CM*cv1.getY()) );
			Node helperNode = new Node(helperNodeX, helperNodeY);
			return helperNode;
		} else if(ballNode.getType().equals(BallTypes.WALL)) {

			Vector v = new Vector(savedPoints.get(0).getX() - ballNode.getX(), savedPoints.get(0).getY() - ballNode.getY());
			v= v.getNormalizedVector();

			Vector crossvector = v.getTvaerVector();

			Node helperNode = new Node((int)((ballNode.getX() - TI_CM*2.5*v.x)), (int)((ballNode.getY() - TI_CM*2.5*v.y)));
			helperNode.setX((int)(helperNode.getX() + TI_CM*1.25*crossvector.getX()));
			helperNode.setY((int)(helperNode.getY() + TI_CM*1.25*crossvector.getY()));
			System.out.println("HELPER POINT: " + helperNode.getX() + "," + helperNode.getY());
			return helperNode;
		}



		return null;
	}

	public static boolean isPossibleMove(ArrayList<Point> car, Node to, ArrayList<ShortPoint> redPoints) {
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
			for(ShortPoint point : getPoints(200, carnodes[i], ballnodes[i])) {
				if(redPoints.contains(point)) {
					//Sytem.out.println("NOT POSSIBLE");
					return false;
				}
			}
		}
		//Sytem.out.println("POSSIBLE");
		return true;		
	}

	public static ShortPoint[] getPoints(int howManyPoints, Node node1, Node node2) {
		ShortPoint[] points = new ShortPoint[howManyPoints];
		int yDiff = node2.getY() - node1.getY();
		int xDiff = node2.getX() - node1.getX();
		//Sytem.out.println("DIVIDER MED NOGET ANDET END  0, PLS");
		//Sytem.out.println(node2.getX());
		//Sytem.out.println(node1.getX());
		System.out.println(node1.getX() +", " + node2.getX());
		
		double slope;
		try {
		slope = (double)((node2.getY() - node1.getY())/(node2.getX() - node1.getX()));
		} catch(Exception e) {
			slope = (double)((node2.getY() - node1.getY())/0.0000000001);
		}
		double x, y;

		howManyPoints--;
		for(double i = 0; i < howManyPoints; i++) {
			y = slope == 0 ? 0 : yDiff * (i / howManyPoints);
			x = slope == 0 ? xDiff * (i / howManyPoints) : y / slope;
			points[(int)i] = new ShortPoint((short)(Math.round(x) + node1.getX()), (short)(Math.round(y) + node1.getY()));

		}
		points[howManyPoints] = new ShortPoint((short)node2.getX(), (short)node2.getY() );
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
	
	public static ArrayList<Move> calculateReturnToStartMove(ArrayList<Point> car, CarStartPoint csp){
		ArrayList<Move> moves = new ArrayList<Move>();
		Node startPoint = new Node((int)(csp.getCarPoints().get(0).x), (int) csp.getCarPoints().get(0).y);
		Node startMiddlePoint = new Node((int)((csp.getCarPoints().get(1).x + csp.getCarPoints().get(2).x)/2), (int)((csp.getCarPoints().get(1).y + csp.getCarPoints().get(2).y)/2));
		Node middlePoint = new Node((int)((car.get(1).x + car.get(2).x)/2), (int)((car.get(1).y + car.get(2).y)/2));
		double length = calculateDistance(startPoint, middlePoint);
		
		Vector carVector = new Vector(car.get(0).x - middlePoint.getX(), car.get(0).y - middlePoint.getY());
		Vector ballVector = new Vector(startPoint.getX() - middlePoint.getX(), startPoint.getY() - middlePoint.getY());
		double angle = carVector.calculateAngle(ballVector);
		Move move = new Move();
		move.setDistance((length/777)*100);
		if(carVector.crossProduct(ballVector) > 0) {
			angle = -angle;
		}
		move.setAngle(angle);
		moves.add(move);
		
		Vector carStartVector = new Vector(startPoint.getX() - startMiddlePoint.getX(), startPoint.getY() - startMiddlePoint.getY());
		angle = ballVector.calculateAngle(carStartVector);
		
		Move move2 = new Move();
		move2.setDistance(-15);
		move2.setAngle(angle);
		move2.setTwerk(true);
		moves.add(move2);
		return moves;
		
	}
	
	public static boolean isCarInRightPlace(ArrayList<Point> car, CarStartPoint csp) {
		if(calculateCarAngle(car) > csp.getCarAngle() + 5 || calculateCarAngle(car) < csp.getCarAngle() - 5) {
			return false;
		}
		if(!(arePointsSimilar(car.get(0), csp.carPoints.get(0)) && arePointsSimilar(car.get(1), csp.carPoints.get(1)) && arePointsSimilar(car.get(2), csp.carPoints.get(2)))){
			return false;
		}
		return true;
		
	}
	
	public static boolean arePointsSimilar(Point point1, Point point2) {
		if((point1.x - 10 < point2.x && point1.x < point2.x + 10) && (point2.y - 10 < point1.y && point1.y < point2.y + 10)) {
			return true;
		}
		return false;
	}

	public static ArrayList<Move> calculateMoveButThisOneIsBetterBecauseWeUseVectors(ArrayList<Node> graph, ArrayList<Point> car, int toIndex) {
		//Sytem.out.println(car.get(1).x);
		//Sytem.out.println(car.get(2).x);
		Point middlePoint = new Point((car.get(1).x + car.get(2).x)/2, (car.get(1).y + car.get(2).y)/2);
		//Sytem.out.println("middle" + middlePoint.x + ", " + middlePoint.y);
		Node ball = graph.get(toIndex);
		//Sytem.out.println("BALLTYOPE");
		//Sytem.out.println(ball.getType().toString());
		System.out.println(ball.getType());
		if(!ball.getType().equals(BallTypes.NORMAL)) {
			return calculateMoveButThisOneIsOnlyUsedWhenItIsAHelperNode(car, ball);
		}

		Vector carVector = new Vector(car.get(0).x - middlePoint.x, car.get(0).y - middlePoint.y);
		//Sytem.out.println(carVector.getX() + ", " + carVector.getY());
		Vector ballVector = new Vector(ball.getX() - middlePoint.x, ball.getY() - middlePoint.y);
		//Sytem.out.println(ballVector.getX() + ", " + ballVector.getY());
		double angle = carVector.calculateAngle(ballVector);
		double length = calculateDistance(ball, new Node(-1, (int) middlePoint.x, (int) middlePoint.y));

		if(carVector.crossProduct(ballVector) > 0) {
			angle = -angle;
		}

		Move move = new Move();
		move.setAngle(angle);
		//move.setDistance((length/777)*100-10);

		move.setDistance((length/777)*100-5);

		System.out.println(ball.getX() + ", " + ball.getY());
		System.out.println("DISTNACE: " + length);

		System.out.println("ANGLE: " + angle);

		ArrayList<Move> moves = new ArrayList<Move>();
		moves.add(move);
		return moves;

	}

	public static ArrayList<Move> calculateMoveByBallTypes(){
		return null;
	}

	public static ArrayList<Move> calculateMoveButThisOneIsOnlyUsedWhenItIsAHelperNode(ArrayList<Point> car, Node ball){

		for(int i = 0; i < car.size(); i++) {
			System.out.println(car.get(i).x +"," + car.get(i).y);
		}
		ArrayList<Move> moves = new ArrayList<Move>();
		Point middlePoint = new Point((car.get(1).x + car.get(2).x)/2, (car.get(1).y + car.get(2).y)/2);
		//Sytem.out.println("middle" + middlePoint.x + ", " + middlePoint.y);
		Node helperNode = ball.getHelperNode();

		Vector carVector = new Vector(car.get(0).x - middlePoint.x, car.get(0).y - middlePoint.y);
		//Sytem.out.println(carVector.getX() + ", " + carVector.getY());
		Vector helperVector = new Vector(helperNode.getX() - middlePoint.x, helperNode.getY() - middlePoint.y);
		//Sytem.out.println(helperVector.getX() + ", " + helperVector.getY());
		double angle = carVector.calculateAngle(helperVector);
		double length = calculateDistance(helperNode, new Node((int) middlePoint.x, (int) middlePoint.y));

		if(carVector.crossProduct(helperVector) > 0) {
			angle = -angle;
		}

		if(ball.getType().equals(BallTypes.WALL)) {
			Move move = new Move();
			move.setAngle(angle);
			move.setDistance((length/777)*100-5);
			moves.add(move);
			System.out.println(ball.getX() + ", " + ball.getY());
			System.out.println("ANGLE:"+move.getAngle());
			System.out.println("DISTANCE:"+move.getDistance());


			Vector ballVector = new Vector(ball.getX() - helperNode.getX(), ball.getY() - helperNode.getY());
			angle = ballVector.calculateAngle(helperVector);
			angle-=10;

			if(helperVector.crossProduct(ballVector) > 0) {
				angle = -angle;
			}

			Move move2 = new Move();
			move2.setAngle(angle);
			move2.setDistance(10);
			move2.setDriveSlowly(true);
			moves.add(move2);
			Move move3 = new Move();
			move3.setAngle(-45);
			move3.setDistance(-move2.getDistance());
			move3.setDriveSlowly(true);
			moves.add(move3);
			//Sytem.out.println("MANGE MANGE MOVES " + moves.size());
		} else {
			Move move = new Move();
			move.setAngle(angle);
			move.setDistance((length/777)*100-10);
			moves.add(move);
			System.out.println(ball.getX() + ", " + ball.getY());


			Vector ballVector = new Vector(ball.getX() - helperNode.getX(), ball.getY() - helperNode.getY());
			angle = ballVector.calculateAngle(helperVector);


			if(helperVector.crossProduct(ballVector) > 0) {
				angle = -angle;
			}
			angle-=10;

			System.out.println("Type: " + ball.getType().toString());

			System.out.println("Helper Node: " + ball.getHelperNode().getX() + ", " + ball.getHelperNode().getY());

			Move move2 = new Move();
			move2.setAngle(angle);
			move2.setDistance(13);
			move2.setDriveSlowly(true);
			moves.add(move2);
			Move move3 = new Move();
			move3.setAngle(-35);
			move3.setDistance(-move2.getDistance());
			move3.setDriveSlowly(true);
			moves.add(move3);
		}
		return moves;
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
		Vector carVector = new Vector(middlePoint.x - car.get(0).x, middlePoint.y - car.get(0).y);
		
		Vector horizontalVector = new Vector(1, 0);
		double v = Math.atan2(horizontalVector.y, horizontalVector.x) - Math.atan2(carVector.y, carVector.x);
		if(v < 0) {
			v += 2 * Math.PI;
		}
		System.out.println(Math.toDegrees(v));
		return Math.toDegrees(v);
		//double angle = carVector.calculateAngle(horizontalVector);

		/*
		 * double v = Math.atan2(otherVector.y, otherVector.x) - Math.atan2(this.y, this.x);
		if(v < 0) {
			v += 2 * Math.PI;
		}
		return Math.toDegrees(v);
		 */
		//New code should be better. Test before removing this.
		/*
	double a = car.get(0).x - middlePoint.x;
	double b = car.get(0).y - middlePoint.y;
	double dist = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
	double cosC = (Math.pow(a, 2) + Math.pow(dist, 2) - Math.pow(b, 2))/(2*a*dist);
		 */
		//return angle;
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

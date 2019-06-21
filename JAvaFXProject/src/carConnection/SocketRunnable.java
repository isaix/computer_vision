package carConnection;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.opencv.core.Algorithm;
import org.opencv.core.Point;

import algorithm.AlgorithmController;
import algorithm.CarStartPoint;
import algorithm.Move;
import algorithm.Node;
import algorithm.ShortPoint;
import objrecognition.CarDetector;
import objrecognition.ColorDetector;
import objrecognition.CircleDetector;
import objrecognition.*;

public class SocketRunnable implements Runnable{
	
	public static CarStartPoint carStartPoint;

	@Override
	public void run() {
		long startTime = System.currentTimeMillis();
			
		SocketClient client = new SocketClient();
		client.startConnection("192.168.43.174", 6666);
		
		boolean connected = true; 
		boolean moveToWall = true;
		
		int i = 0; 
		ArrayList<Move> moves;
			
		//Move[] moves = new Move[] {new Move(30, 90), new Move(30, 90), new Move(30, 90), new Move(30, 90)};
		ArrayList<Move> lastMove = null;
		
		while(connected) {
			
			
//			ArrayList<LineZ> lines = new ArrayList<>();
//			try {
//				lines = HoughLinesRun.getLines();
//			} catch(Exception e) {
//				//System.out.print("o shit");
//			}
//			
//			
//			//System.out.println("LINES" + lines.size());

			
			ArrayList<Point> points = CircleDetector.getvalidBallCoordinates();
			if(points == null || points.isEmpty()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			////System.out.println("points: " + points.size());

			ArrayList<Point> car = CarDetector.getvalidCarCoordinates();
			
			
			
			////System.out.println("car: " + car.size());

			if(car == null || car.isEmpty()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			if(carStartPoint == null) {
				//System.out.println("car = " + car);
				carStartPoint = new CarStartPoint(car, AlgorithmController.calculateCarAngle(car));
			} else{
				//System.out.println("startpoints " + carStartPoint.carAngle + ", " + carStartPoint.carPoints);
			}
			
			ArrayList<ShortPoint> foundWalls2 = null;
			try {
				foundWalls2 = ColorDetector.run();
				//System.out.println(foundWalls2.size());
			} catch(Exception e){
				//System.out.println("Intet frame, start kameraet.");				
			}
			/*
			for(ShortPoint shortpoint : foundWalls2) {
				pw.append("X:" + shortpoint.x +  ", Y:" + shortpoint.y + "\n");
			}
			*/
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			ArrayList<Node> nodes = AlgorithmController.ConvertToGraph(points, car, foundWalls2);
			
			if(points.isEmpty()) {
				Node carMiddle = new Node(-1, (int)(carStartPoint.carPoints.get(0).x), (int)(carStartPoint.carPoints.get(0).y));
				nodes.clear();
				nodes.add(carMiddle);
				if(AlgorithmController.isPossibleMove(car, carMiddle, foundWalls2)) {
					moves = AlgorithmController.calculateMoveButThisOneIsBetterBecauseWeUseVectors(nodes, car, 0);
					moves.get(0).setAngle(carStartPoint.carAngle);
					client.sendMoves(moves);
				}
				else {
					AlgorithmController.gotoWall(foundWalls2, moveToWall, car);
				}
			}
			
			foundWalls2 = null;
			//nodes = AlgorithmController.convertToMST(nodes, nodes.get(0));
			for (Node node : nodes) {
				////System.out.println("all nodes: " + node.getX()+ ","+node.getY());
			}
			int nearestBall = AlgorithmController.findNearestBall(nodes);
			
			if(nearestBall == -1) {
				moves = AlgorithmController.gotoWall(foundWalls2, moveToWall, car);
				String resp = client.sendMoves(moves);
				lastMove = moves;
				moves = null;
				points.clear();
				car.clear();
				moveToWall = false;
				continue;
				
			}
			//ArrayList<Integer> moves = AlgorithmController.performDFS(nodes, nodes.get(0));
			//Move move = AlgorithmController.calculateMove(nodes, car, nearestBall);
			moves = AlgorithmController.calculateMoveButThisOneIsBetterBecauseWeUseVectors(nodes, car, nearestBall);
			//System.out.println("move: " + nodes.get(nearestBall).getX() + ", " + nodes.get(nearestBall).getY());
			//System.out.println("angle: " + move.getAngle());
			////System.out.println("move: " + nodes.get(nearestBall).getX() + ", " + nodes.get(nearestBall).getY());
			////System.out.println("angle: " + move.getAngle());
			
			if(moves.equals(lastMove)) {
				continue;
			}
			
			String resp = client.sendMoves(moves);
			
			lastMove = moves;
			moves = null;
			points.clear();
			car.clear();
			CarDetector.resetCarCoordinates();
						
			if(resp.toLowerCase().equals("done")) {
				client.stopConnection();
				connected = false; 
			}
			moveToWall = true;
			
		}
		
	}

}

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
		long finishTime = 0;
			
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
			if(points == null) {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("ISSA NULL");
				continue;
			}
			////System.out.println("points: " + points.size());

			ArrayList<Point> car = CarDetector.getvalidCarCoordinates();
			
			
			
			////System.out.println("car: " + car.size());

			if(car == null || car.isEmpty()) {
				try {
					Thread.sleep(250);
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
				//for(int i = 0; i < 5; i++) {
					
				//}
				foundWalls2 = ColorDetector.run();
				System.out.println("IT HAS BEEN RUN");
				//System.out.println(foundWalls2.size());
			} catch(Exception e){
				//System.out.println("Intet frame, start kameraet.");
				e.printStackTrace();
			}
			
			ArrayList<Node> nodes = AlgorithmController.ConvertToGraph(points, car, foundWalls2);
			
			if(points.isEmpty()) {
				moves = AlgorithmController.calculateReturnToStartMove(car, carStartPoint);
				
				/*
				if(AlgorithmController.isPossibleMove(car, carMiddle, foundWalls2)) {
					moves = AlgorithmController.calculateMoveButThisOneIsBetterBecauseWeUseVectors(nodes, car, 0);
					moves.get(0).setAngle(carStartPoint.carAngle);
					client.sendMoves(moves);
				}
				else {
					moves = AlgorithmController.gotoWall(foundWalls2, moveToWall, car);
				}
				*/
				String resp = client.sendMoves(moves);
				
				lastMove = moves;
				moves = null;
				points.clear();
				car.clear();
				moveToWall = false;
				if(resp.equalsIgnoreCase("JATAKCHEF")) {
					finishTime = System.currentTimeMillis();
					break;
				}
				continue;
				
				
			}
			int nearestBall = AlgorithmController.findNearestBall(nodes);
			System.out.println("NR BALL" + nearestBall);
			
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
			
			foundWalls2 = null; 

			

			moves = AlgorithmController.calculateMoveButThisOneIsBetterBecauseWeUseVectors(nodes, car, nearestBall);
			
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
		long seconds =(finishTime - startTime)/1000;
		long minutes = seconds / 60;
		seconds %= 60;
		System.out.print("Det tog " + minutes + " minutter og " + seconds + "sekunder");
		client.stopConnection();
		System.exit(0);
		
	}

}

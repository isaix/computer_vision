package carConnection;

import java.util.ArrayList;

import org.opencv.core.Algorithm;
import org.opencv.core.Point;

import algorithm.AlgorithmController;
import algorithm.Move;
import algorithm.Node;
import objrecognition.Car;
import objrecognition.ColorDetector;
import objrecognition.HoughCirclesRun;
import objrecognition.*;

public class SocketRunnable implements Runnable{

	@Override
	public void run() {
			
		SocketClient client = new SocketClient();
		client.startConnection("192.168.43.174", 6666);
		
		
		boolean connected = true; 
		
		int i = 0; 

			
		//Move[] moves = new Move[] {new Move(30, 90), new Move(30, 90), new Move(30, 90), new Move(30, 90)};
		Move lastMove = null;
		
		while(connected) {
			
			
//			ArrayList<LineZ> lines = new ArrayList<>();
//			try {
//				lines = HoughLinesRun.getLines();
//			} catch(Exception e) {
//				System.out.print("o shit");
//			}
//			
//			
//			System.out.println("LINES" + lines.size());

			
			ArrayList<Point> points = HoughCirclesRun.getvalidBallCoordinates();
			if(points == null || points.isEmpty()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			System.out.println("points: " + points.size());

			ArrayList<Point> car = Car.getvalidCarCoordinates();
			
			System.out.println("car: " + car.size());

			if(car == null || car.isEmpty()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
//			try {
//				ArrayList<Point> foundWalls2 = ColorDetector.run();
//				System.out.println(foundWalls2.size());
//			} catch(Exception e){
//				System.out.println("Intet frame, start kameraet.");				
//			}
//			
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			
			ArrayList<Node> nodes = AlgorithmController.ConvertToGraph(points, car.get(0), null);
			//nodes = AlgorithmController.convertToMST(nodes, nodes.get(0));
			for (Node node : nodes) {
				System.out.println("all nodes: " + node.getX()+ ","+node.getY());
			}
			int nearestBall = AlgorithmController.findNearestBall(nodes);
			
			//ArrayList<Integer> moves = AlgorithmController.performDFS(nodes, nodes.get(0));
			//Move move = AlgorithmController.calculateMove(nodes, car, nearestBall);
			Move move = AlgorithmController.calculateMoveButThisOneIsBetterBecauseWeUseVectors(nodes, car, nearestBall);
			System.out.println("move: " + nodes.get(nearestBall).getX() + ", " + nodes.get(nearestBall).getY());
			System.out.println("angle: " + move.getAngle());
			
			if(move.equals(lastMove)) {
				continue;
			}
			
			String resp = client.sendMove(move);
			
			lastMove = move;
			move = null;
			points.clear();
			car.clear();
						
			if(resp.toLowerCase().equals("done")) {
				client.stopConnection();
				connected = false; 
			}
			
		}
		
	}

}

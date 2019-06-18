package carConnection;

import java.util.ArrayList;

import org.opencv.core.Algorithm;
import org.opencv.core.Point;

import algorithm.AlgorithmController;
import algorithm.Move;
import algorithm.Node;
import objrecognition.CarDetectorRun;
import objrecognition.ColorDetector;
import objrecognition.HoughCirclesRun;

public class SocketRunnable implements Runnable{

	@Override
	public void run() {
			
		SocketClient client = new SocketClient();
		client.startConnection("192.168.43.174", 6666);
		
		
		boolean connected = true; 
		
		int i = 0; 

			
		//Move[] moves = new Move[] {new Move(30, 90), new Move(30, 90), new Move(30, 90), new Move(30, 90)};
		
		
		while(connected) {
			
//			try {
//				ArrayList<Point> foundWalls2 = ColorDetector.run();
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

			
			ArrayList<Point> points = HoughCirclesRun.getvalidBallCoordinates();
			if(points == null || points.isEmpty()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			System.out.println("points: " + points.size());

			ArrayList<Point> car = CarDetectorRun.getvalidCarCoordinates();
			
			System.out.println("car: " + car.size());

			if(car == null || car.isEmpty()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			
			ArrayList<Node> nodes = AlgorithmController.ConvertToGraph(points, car.get(0), null);
			//nodes = AlgorithmController.convertToMST(nodes, nodes.get(0));
			for (Node node : nodes) {
				System.out.println("all nodes: " + node.getX()+ ","+node.getY());
			}
			int nearestBall = AlgorithmController.findNearestBall(nodes);
			
			//ArrayList<Integer> moves = AlgorithmController.performDFS(nodes, nodes.get(0));
			Move move = AlgorithmController.calculateMove(nodes, car, nearestBall);
			System.out.println("move: " + nodes.get(nearestBall).getX() + ", " + nodes.get(nearestBall).getY());
			System.out.println("angle: " + move.getAngle());
			
			String resp = client.sendMove(move);
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

package carConnection;

import java.util.ArrayList;

import org.opencv.core.Algorithm;
import org.opencv.core.Point;

import algorithm.AlgorithmController;
import algorithm.Move;
import algorithm.Node;
import objrecognition.Car;
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
			
			
			ArrayList<Point> points = HoughCirclesRun.getvalidBallCoordinates();
			if(points == null) {
				continue;
			}
			
			ArrayList<Point> car = Car.getvalidCarCoordinates();
			
			ArrayList<Node> nodes = AlgorithmController.ConvertToGraph(points, car.get(0));
			nodes = AlgorithmController.convertToMST(nodes, nodes.get(0));
			ArrayList<Integer> moves = AlgorithmController.performDFS(nodes, nodes.get(0));
			Move move = AlgorithmController.calculateMove(nodes, car, moves.get(1));
			
			String resp = client.sendMove(move);
						
			if(resp.toLowerCase().equals("done")) {
				client.stopConnection();
				connected = false; 
			}
			
		}
		
	}

}

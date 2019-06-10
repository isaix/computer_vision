package objrecognition;


import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList; // import the ArrayList class
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;






public class HoughCirclesRun {

	ArrayList<Point> ballCoordinates = new ArrayList<Point>();
	ArrayList<Point> foundCoordinates = new ArrayList<Point>();


	


	public Mat run(Mat frame) {

		Mat gray = new Mat();


		Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
		
		Imgproc.medianBlur(gray, gray, 5);
		Mat circles = new Mat();
		Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
				(double)gray.rows()/16, // change this value to detect circles with different distances to each other
				100.0, 30.0, 1, 30); // change the last two parameters
		// (min_radius & max_radius) to detect larger circles

			for (int x = 0; x < circles.cols(); x++) {
				double[] c = circles.get(0, x);
				Point center = new Point(Math.round(c[0]), Math.round(c[1]));
				// circle center
				Imgproc.circle(frame, center, 1, new Scalar(0,100,100), 3, 8, 0 );
				//System.out.println("center: " + center);
				// circle outline
				int radius = (int) Math.round(c[2]);
				//System.out.println("radius: " +radius );

				//System.out.println("in frame: " + frame);
				Imgproc.circle(frame, center, radius, new Scalar(255,0,255), 3, 8, 0 );
				
				
//				if (ballCoordinates.isEmpty()) {
//					
//					ballCoordinates.add(center);
//					
//					//System.out.println("new point found to empty array: " + center);
//
//					
//				} else {
					
					Boolean found = false;
										
					for (Point coordinates : ballCoordinates) {
						if ((coordinates.x - 5.0 < center.x  && center.x < coordinates.x + 5.0) || (coordinates.x - 5.0 < center.y && center.y < coordinates.y + 5.0)) {
//							ballCoordinates.remove(coordinates);
//							ballCoordinates.add(center);
//							System.out.println("point: " + coordinates + " replaced with : " + center);
							//foundCoordinates.add(coordinates);
							found = true;
							break;
						} 
					}
					if (!found) {
						foundCoordinates.add(center);
						//System.out.println("new point found : " + center);
					}
				//}

			}
		
//		
//		
//		
//		System.out.println("foundCoordinates: " + foundCoordinates);
//		
//		System.out.println("ballCoordinates: " + ballCoordinates);
//		
//		System.out.println(!ballCoordinates.containsAll(foundCoordinates));
//		

		
		if (ballCoordinates.isEmpty() && !foundCoordinates.isEmpty()) {
			System.out.println("changes registered 1");
			System.out.println("should be clear : "+ ballCoordinates);


			ballCoordinates.addAll(foundCoordinates);
		} else if (!ballCoordinates.containsAll(foundCoordinates)) {
			System.out.println("changes registered 2");

			ballCoordinates.clear();
			ballCoordinates.addAll(foundCoordinates);
			//ballCoordinates = foundCoordinates; 

		}
		
		
		
		System.out.println("before clear : "+ foundCoordinates);


		foundCoordinates.clear();
		System.out.println("after clear : "+ foundCoordinates);



		return frame;


	}    
}

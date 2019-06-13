package objrecognition;


import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import algorithm.AlgorithmController;

import java.io.IOException;
import java.util.ArrayList; // import the ArrayList class
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



public class HoughCirclesRun {

	int counter = 0;
	int frameDelay = 30;

	double tolerance = 10.0;

	Mat circles;

	ArrayList<Point> ballCoordinates = new ArrayList<Point>();
	ArrayList<Point> foundCoordinates = new ArrayList<Point>();

	AlgorithmController ac = new AlgorithmController();





	public Mat run(Mat frame) {

		Mat gray = new Mat();

		Mat oldFrame = frame;


		Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);

		Imgproc.medianBlur(gray, gray, 5);
		circles = new Mat();
		Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
				(double)gray.rows()/16, // change this value to detect circles with different distances to each other
				100.0, 30.0, 1, 25); // change the last two parameters
		// (min_radius & max_radius) to detect larger circles

		//System.out.println("number of points found " + circles.cols() );



		for (int x = 0; x < circles.cols(); x++) {
			double[] c = circles.get(0, x);
			Point center = new Point(Math.round(c[0]), Math.round(c[1]));
			//			// circle center
			//			Imgproc.circle(frame, center, 1, new Scalar(0,100,100), 3, 8, 0 );
			//			//System.out.println("center: " + center);
			//			// circle outline
			//			int radius = (int) Math.round(c[2]);
			//			//System.out.println("radius: " +radius );
			//
			//			//System.out.println("in frame: " + frame);
			//			Imgproc.circle(frame, center, radius, new Scalar(255,0,255), 3, 8, 0 );



			Boolean found = false;

			for (Point coordinates : foundCoordinates) {
				if ((coordinates.x - tolerance < center.x && center.x < coordinates.x + tolerance) && (coordinates.y - tolerance < center.y && center.y < coordinates.y + tolerance)) {


					found = true;
					break;
				} 
			}
			if (!found) {

				foundCoordinates.add(center);

	
			} 
			if (found) {

				Imgproc.circle(frame, center, 1, new Scalar(0,100,100), 3, 8, 0 );

				Imgproc.putText(frame, center.toString() , new Point(center.x+8, center.y), 1, 2, new Scalar(0,0,0), 4);

				//System.out.println("center: " + center);
				// circle outline
				int radius = (int) Math.round(c[2]);
				//System.out.println("radius: " +radius );

				//System.out.println("in frame: " + frame);
				Imgproc.circle(frame, center, radius, new Scalar(255,0,255), 3, 8, 0 );
			}

		}


		//				System.out.println("foundCoordinates: " + foundCoordinates);
		//				
		//				System.out.println("ballCoordinates: " + ballCoordinates);
		//		
		//		System.out.println(!ballCoordinates.containsAll(foundCoordinates));






		if(counter%frameDelay == 0) {


			if (ballCoordinates.isEmpty() && !foundCoordinates.isEmpty()) {
				
				System.out.println("change");

				ballCoordinates.addAll(foundCoordinates);
			} else if (!ballCoordinates.containsAll(foundCoordinates)) {
				
				System.out.println("change");

				
				//ballCoordinates = foundCoordinates; 
				
				if(!(ballCoordinates.size() == foundCoordinates.size())) {
					ballCoordinates.clear();
					ballCoordinates.addAll(foundCoordinates);
				}

			} else {
				if (ballCoordinates.size() > foundCoordinates.size()) {
					System.out.println("clearing ballCoordinates because");
					System.out.println(ballCoordinates);
					System.out.println("is bigger than");
					System.out.println(foundCoordinates);
					ballCoordinates.clear();
				}
				
			}

			System.out.println("\n found balls: " + ballCoordinates.size());
			System.out.println("and the coordinates are: " + ballCoordinates);

			System.out.println("END \n");


			foundCoordinates.clear();

		}


		if(counter%frameDelay == 0) {
			counter++;
			return frame;
		}else {
			counter++;
			return oldFrame;
		}


	}    



}







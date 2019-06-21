package objrecognition;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class CarDetector {

	int counter = 0;
	int frameDelay = 5;

	double tolerance = 10.0;

	int validCount = 0;

	Mat circles;

	ArrayList<Point> carCoordinates = new ArrayList<Point>();
	static ArrayList<Point> validCarCoordinates = new ArrayList<Point>();	
	ArrayList<Point> foundCoordinates = new ArrayList<Point>();	


	static public ArrayList<Point> getvalidCarCoordinates(){
		ArrayList<Point> temp = new ArrayList<Point>();	
		temp.addAll(validCarCoordinates);
		validCarCoordinates.clear();
		return temp;
	}
	
	static public void resetCarCoordinates() {
		validCarCoordinates.clear();
	}



	public Mat run(Mat frame) {

		try {
		Mat gray = new Mat();

		Mat oldFrame = frame;


		Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);

		Imgproc.medianBlur(gray, gray, 5);
		circles = new Mat();
		Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
				(double)gray.rows()/16, 
				100.0, 30.0, 30, 40);



		for (int x = 0; x < circles.cols(); x++) {
			double[] c = circles.get(0, x);
			Point center = new Point(Math.round(c[0]), Math.round(c[1]));

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

				// circle outline
				int radius = (int) Math.round(c[2]);

				Imgproc.circle(frame, center, radius, new Scalar(0,191,255), 3, 8, 0 );
			}

		}



		if(counter%frameDelay == 0) {


			if (carCoordinates.isEmpty() && !foundCoordinates.isEmpty()) {

				//				//System.out.println("change");

				carCoordinates.addAll(foundCoordinates);

			} else {

				if(!(carCoordinates.size() == foundCoordinates.size())) {
					//					//System.out.println("change, new size");
					carCoordinates.clear();
					carCoordinates.addAll(foundCoordinates);
				} else {
					boolean similar = false;

					for (Point foundCoordinate : foundCoordinates) {
						for (Point validCarCoordinate : carCoordinates) {
							similar = false;
							if ((foundCoordinate.x - tolerance < validCarCoordinate.x && validCarCoordinate.x < foundCoordinate.x + tolerance) && (foundCoordinate.y - tolerance < validCarCoordinate.y && validCarCoordinate.y < foundCoordinate.y + tolerance)) {
								similar = true;
								break;
							}
						}
						if(!similar) {
							//							//System.out.println("change, not similar");
							carCoordinates.clear();
							carCoordinates.addAll(foundCoordinates);
							validCount = 0;
							//							//System.out.println("valid count reset");
							break;
						} 
					}
					if (similar) {
						validCount++;
						//						//System.out.println("valid incremented: " + validCount);
					}
				}
			} 


			foundCoordinates.clear();

			if(validCount == 3) {

				boolean similar = false;

				for (Point validCarCoordinate : carCoordinates) {
					for (Point validCoordinate : validCarCoordinates) {
						similar = false;
						if ((validCarCoordinate.x - tolerance < validCoordinate.x && validCoordinate.x < validCarCoordinate.x + tolerance) && (validCarCoordinate.y - tolerance < validCoordinate.y && validCoordinate.y < validCarCoordinate.y + tolerance)) {
							similar = true;
							break;
						}
					}
					if(!similar) {
						//						//System.out.println("a new valid array");
						validCarCoordinates.clear();
						//						//System.out.println("unsorted Coordinates: " + carCoordinates);

						validCarCoordinates.addAll(comparePoints(carCoordinates.get(0), carCoordinates.get(1), carCoordinates.get(2)));
						validCount = 0;
						//System.out.println("valid car coordinates: " + validCarCoordinates);
						break;
					} 
				}
				if (similar) {
					//System.out.println("valid array is still the same");
				}

				validCount = 0;
			}
		}



		if(counter%frameDelay == 0) {
			counter++;
			return frame;
		}else {
			counter++;
			return oldFrame;
		}


		}catch(Exception e) {
			e.printStackTrace();
		}
		return frame;
	} 


	public ArrayList<Point> comparePoints(Point p1, Point p2, Point p3){
		ArrayList<Point> temp = new ArrayList<Point>();

		if ((calculateDistance(p1.x, p1.y, p2.x, p2.y) > calculateDistance(p2.x, p2.y, p3.x, p3.y)) && (calculateDistance(p1.x, p1.y, p3.x, p3.y) > calculateDistance(p2.x, p2.y, p3.x, p3.y)))
		{
			temp.add(p1);
			temp.add(p2);
			temp.add(p3);
		} else if  ((calculateDistance(p2.x, p2.y, p1.x, p1.y) > calculateDistance(p1.x, p1.y, p3.x, p3.y)) && (calculateDistance(p2.x, p2.y, p3.x, p3.y) > calculateDistance(p1.x, p1.y, p3.x, p3.y))) {
			temp.add(p2);
			temp.add(p1);
			temp.add(p3);
		} else {
			temp.add(p3);
			temp.add(p2);
			temp.add(p1);
		}
		return temp;

	}

	public double calculateDistance(
			double x1, 
			double y1, 
			double x2, 
			double y2) {

		double ac = Math.abs(y2 - y1);
		double cb = Math.abs(x2 - x1);

		return Math.hypot(ac, cb);
	}


}    









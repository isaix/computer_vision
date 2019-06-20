package objrecognition;


import java.util.ArrayList;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

public class LineDetector {
	static double errormagin = 8.0;
	int linecounter = 0;
	int delay = 30;
	int frameDelay = 15;
	int counter = 0;

	int validCount = 0;

	ArrayList<LineZ> foundLineCoord = new ArrayList<>();
	ArrayList<LineZ> linecoord = new ArrayList<>();

	public Mat runLine(Mat frame) {
		Mat dst = new Mat(), cdst = new Mat(), cdstP;

		Imgproc.Canny(frame, dst, 200, 500, 3, false);
		//Imgproc.Canny(frame, dst, 80, 150, 3, false);

		// Copy edges to the images that will display the results in BGR
		Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
		cdstP = cdst.clone();


		// Probabilistic Line Transform
		Mat linesP = new Mat(); // will hold the results of the detection
		Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 30); // runs the actual detection
		// Draw the lines
		ArrayList<LineZ> linesArray = new ArrayList<>();
		for (int x = 0; x < linesP.rows(); x++) {
			double[] l = linesP.get(x, 0);
			Imgproc.line(frame, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(103, 247, 12), 3, Imgproc.LINE_AA, 0);
			linesArray.add(new LineZ(new Point(l[0], l[1]), new Point(l[2], l[3])));

		}
		return frame;
	}

	public static ArrayList<LineZ> getLines(){
		ArrayList<LineZ> linesArray = new ArrayList<>();
		for(int i = 0; i<5; i++) {
			Mat frame = ObjRecognitionController.getLatestFrame();

			Mat dst = new Mat(), cdst = new Mat(), cdstP;

			Imgproc.Canny(frame, dst, 200, 500, 3, false);
			//Imgproc.Canny(frame, dst, 80, 150, 3, false);

			// Copy edges to the images that will display the results in BGR
			Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
			cdstP = cdst.clone();

			// Probabilistic Line Transform
			Mat linesP = new Mat(); // will hold the results of the detection

			Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 30); // runs the actual detection

			// Finds the lines coordinates

			for (int x = 0; x < linesP.rows(); x++) {
				double[] l = linesP.get(x, 0);
				LineZ newLine = new LineZ(new Point(l[0], l[1]), new Point(l[2], l[3]));
				if(!linesArray.contains(newLine)) {
					linesArray.add(new LineZ(new Point(l[0], l[1]), new Point(l[2], l[3])));
				}
			}
		}
		return linesArray;
	}
}
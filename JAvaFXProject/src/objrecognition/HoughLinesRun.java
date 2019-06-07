package objrecognition;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

class HoughLinesRun {

	public Mat runLine(Mat frame) {
		// Declare the output variables
		Mat dst = new Mat(), cdst = new Mat(), cdstP;

		// Edge detection
		Imgproc.Canny(frame, dst, 50, 200, 3, false);
		//! [edge_detection]

		// Copy edges to the images that will display the results in BGR
		Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
		cdstP = cdst.clone();

		// Probabilistic Line Transform
		Mat linesP = new Mat(); // will hold the results of the detection
		Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 10); // runs the actual detection
		for (int x = 0; x < linesP.rows(); x++) {
			double[] l = linesP.get(x, 0);
			Imgproc.line(frame, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
		}


		return frame;

	}

}

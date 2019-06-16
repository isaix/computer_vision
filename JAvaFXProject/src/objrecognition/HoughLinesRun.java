package objrecognition;


import java.util.ArrayList;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

class HoughLinesRun {
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
		// GAMMEL Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 10); // runs the actual detection
		Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 30); // runs the actual detection
		// Draw the lines
		ArrayList<LineZ> linesArray = new ArrayList<>();
		for (int x = 0; x < linesP.rows(); x++) {
			double[] l = linesP.get(x, 0);
			//Imgproc.line(frame, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
			Imgproc.line(frame, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(103, 247, 12), 3, Imgproc.LINE_AA, 0);
			linesArray.add(new LineZ(new Point(l[0], l[1]), new Point(l[2], l[3])));
			// System.out.println("Punkt et " + l[0] + " og " + l[1]);            
			// System.out.println("Punkt to " + l[2] + " og " + l[3]);

		}

		boolean linefound = false;

		for(LineZ line : linesArray) {
			if(!foundLineCoord.contains(line)) {
				foundLineCoord.add(line);
			}
		}
		if(linefound = true) {
			Imgproc.HoughLinesP(frame, linesP, 1, Math.PI/180, 50, 50, 30);
		}


		if(counter%frameDelay == 0) {
			if(linecoord.isEmpty() && !foundLineCoord.isEmpty()) {
				linecoord.addAll(foundLineCoord);
			} else {
				if(!(linecoord.size() == foundLineCoord.size())) {
					linecoord.clear();
					linecoord.addAll(foundLineCoord);

				} else {
					boolean similar = false;

					for (LineZ currentFoundLineCoord : foundLineCoord) {
						for (LineZ currentLineCoord : linecoord) {
							similar = false;
							if (foundLineCoord.contains(currentLineCoord)){
								similar = true;
								break;
							}
						}
						if(!similar) {
							linecoord.clear();
							linecoord.addAll(foundLineCoord);
							validCount = 0;

							break;
						} 
					}
					if (similar) {
						validCount++;

					}

				}

			} 
		}

		return frame;

	}

}
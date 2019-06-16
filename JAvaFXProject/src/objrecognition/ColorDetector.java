package objrecognition;

import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ColorDetector {

		 
	 Mat detectColor(Mat frame) {
	        Mat blurImg = new Mat();
	        Mat hsvImage = new Mat();
	        Mat color_range = new Mat();
	 
	        //bluring image to filter small noises.
	        Imgproc.GaussianBlur(frame, blurImg, new Size(5,5),0);
	 
	        //converting blured image from BGR to HSV
	        Imgproc.cvtColor(blurImg, hsvImage, Imgproc.COLOR_BGR2HSV);
	 
	        //filtering pixels based on given HSV color range
	        Core.inRange(hsvImage, new Scalar(0,50,50), new Scalar(5,255,255), frame);
	 
	        return frame;
	    }


}

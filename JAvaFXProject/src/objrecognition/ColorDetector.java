package objrecognition;

import java.util.ArrayList;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import algorithm.ShortPoint;

public class ColorDetector {
	ArrayList<Point> whiteCoordinates = new ArrayList<Point>();
	static ArrayList<ShortPoint> foundWalls = new ArrayList<ShortPoint>();
	Mat whitePoints = new Mat();
	
	
	public static ArrayList<ShortPoint> run() {

		Mat frame = ObjRecognitionController.getLatestFrame();
//		String defaultFile = "//Users//frederikhusted//git//computer_vision//etsjovtbillede.png";
//		Mat frame = Imgcodecs.imread(defaultFile);
		
		Mat blurImg = new Mat();
		Mat hsvImage = new Mat();
		Mat color_range_red_dark = new Mat();
		Mat color_range_red_light = new Mat();


		//bluring image to filter noises
		Imgproc.GaussianBlur(frame, blurImg, new Size(5,5),0);

		//converting blured image from BGR to HSV
		Imgproc.cvtColor(blurImg, hsvImage, Imgproc.COLOR_BGR2HSV);

		//filtering red pixels based on given opencv HSV color range
		Core.inRange(hsvImage, new Scalar(0, 70, 50), new Scalar(10, 255, 255), color_range_red_dark);
		Core.inRange(hsvImage, new Scalar(170, 70, 50), new Scalar(180, 255, 255), color_range_red_light);

		foundWalls.clear();

		//applying bitwise or to detect red.
		Core.bitwise_or(color_range_red_dark, color_range_red_light, frame);

		for(int i = 0; i < frame.rows(); i++) {
			for(int j = 0; j < frame.cols(); j++) {
				double[] returned = frame.get(i, j);
				int value = (int) returned[0];
				if(value == 255) {
					//System.out.println("x: " + i + "\ty: " + j);
					foundWalls.add(new ShortPoint((short) j,(short) i));
				}
			}
		}
				
		//System.out.println(foundWalls);
//		System.out.println(foundWalls.size());
//		System.out.println(foundWalls);
	
		return foundWalls;
	
	        
	        
	}  

//	public Mat getColor(Mat frame) {
//		Mat blurImg = new Mat();
//		Mat hsvImage = new Mat();
//		Mat color_range_red_dark = new Mat();
//		Mat color_range_red_light = new Mat();
//
//
//		//bluring image to filter noises
//		Imgproc.GaussianBlur(frame, blurImg, new Size(5,5),0);
//
//		//converting blured image from BGR to HSV
//		Imgproc.cvtColor(blurImg, hsvImage, Imgproc.COLOR_BGR2HSV);
//
//		//filtering red pixels based on given opencv HSV color range
//		Core.inRange(hsvImage, new Scalar(0, 70, 50), new Scalar(10, 255, 255), color_range_red_dark);
//		Core.inRange(hsvImage, new Scalar(170, 70, 50), new Scalar(180, 255, 255), color_range_red_light);
//
//		foundWalls.clear();
//
//		//applying bitwise or to detect red.
//		Core.bitwise_or(color_range_red_dark, color_range_red_light, frame);
//		
//	
//		return frame;
//	        
//	        
//	}  

}

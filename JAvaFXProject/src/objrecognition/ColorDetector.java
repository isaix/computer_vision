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
	
	
	
	public static ArrayList<ShortPoint> run() {
		System.out.println("ColorDetect Run");
		ArrayList<ShortPoint> foundWalls = new ArrayList<ShortPoint>();


	
		Mat frame = ObjRecognitionController.getLatestFrame();
		
		Mat blurImg = new Mat();
		Mat hsvImage = new Mat();
		Mat color_range_red_dark = new Mat();
		Mat color_range_red_light = new Mat();


		//bluring image to filter noises
		//Imgproc.GaussianBlur(frame, blurImg, new Size(5,5),0);
		Imgproc.medianBlur(frame, blurImg, 5);


		//converting blured image from BGR to HSV
		Imgproc.cvtColor(blurImg, hsvImage, Imgproc.COLOR_BGR2HSV);

		//filtering red pixels based on given opencv HSV color range
		Core.inRange(hsvImage, new Scalar(0, 70, 50), new Scalar(10, 255, 255), color_range_red_dark);
		Core.inRange(hsvImage, new Scalar(170, 70, 50), new Scalar(180, 255, 255), color_range_red_light);


		//applying bitwise_or to detect red from masks.
		Core.bitwise_or(color_range_red_dark, color_range_red_light, frame);

		for(int i = 0; i < frame.rows(); i++) {
			for(int j = 0; j < frame.cols(); j++) {
				double[] returned = frame.get(i, j);
				int value = (int) returned[0];
				if(value == 255) {
					foundWalls.add(new ShortPoint((short) j,(short) i));
				}
			}
		}
				
		
		return foundWalls;
	
	        
	        
	}  

}

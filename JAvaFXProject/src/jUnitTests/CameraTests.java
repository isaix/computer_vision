package jUnitTests;

import static org.junit.Assert.*;

import java.util.concurrent.ScheduledExecutorService;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import objrecognition.CarDetector;
import objrecognition.CircleDetector;

public class CameraTests {

	private CircleDetector circleDetector = new CircleDetector();
	private CarDetector carDetector = new CarDetector();


	@BeforeClass
	public static void loadAssets() {

		//Loading the OpenCV core library   
		nu.pattern.OpenCV.loadShared();


	}


	@Test
	public void testBallRecognition1() {	
		nu.pattern.OpenCV.loadShared();

		System.out.println("test one");
		//Reading the Image from the file  
		String file ="./testAssets/image1-0.jpg"; 
		Mat image = Imgcodecs.imread(file); 

		//Imgproc.resize(image, image, new Size(1080, 1920), 0, 0, Imgproc.INTER_AREA);

		if( image.empty() )  {
			System.out.println("empty");
		} else {
			for(int i = 0; i < 100 ; i++) {

				circleDetector.run(image);
				file ="./testAssets/image1-" + i + ".jpg"; 
				image = Imgcodecs.imread(file); 
			}
		}

		assertEquals(10, circleDetector.getvalidBallCoordinates().size());
	}


	@Test
	public void testBallRecognition2() {	
		nu.pattern.OpenCV.loadShared();

		System.out.println("test one");
		//Reading the Image from the file  
		String file ="./testAssets/image2-0.jpg"; 
		Mat image = Imgcodecs.imread(file); 

		//Imgproc.resize(image, image, new Size(1080, 1920), 0, 0, Imgproc.INTER_AREA);

		if( image.empty() )  {
			System.out.println("empty");
		} else {
			for(int i = 0; i < 100 ; i++) {

				circleDetector.run(image);
				file ="./testAssets/image1-" + i + ".jpg"; 
				image = Imgcodecs.imread(file); 
			}
		}

		assertEquals(10, circleDetector.getvalidBallCoordinates().size());
	}


	@Test
	public void testBallRecognition3() {	
		nu.pattern.OpenCV.loadShared();

		System.out.println("test one");
		//Reading the Image from the file  
		String file ="./testAssets/image3-0.jpg"; 
		Mat image = Imgcodecs.imread(file); 

		//Imgproc.resize(image, image, new Size(1080, 1920), 0, 0, Imgproc.INTER_AREA);

		if( image.empty() )  {
			System.out.println("empty");
		} else {
			for(int i = 0; i < 100 ; i++) {

				circleDetector.run(image);
				file ="./testAssets/image1-" + i + ".jpg"; 
				image = Imgcodecs.imread(file); 
			}
		}

		assertEquals(10, circleDetector.getvalidBallCoordinates().size());
	}

	@Test
	public void testCarRecognition1() {	
		nu.pattern.OpenCV.loadShared();

		System.out.println("test one");
		//Reading the Image from the file  
		String file ="./testAssets/image1-0.jpg"; 
		Mat image = Imgcodecs.imread(file); 

		//Imgproc.resize(image, image, new Size(1080, 1920), 0, 0, Imgproc.INTER_AREA);

		if( image.empty() )  {
			System.out.println("empty");
		} else {
			for(int i = 0; i < 100 ; i++) {

				carDetector.run(image);
				file ="./testAssets/image1-" + i + ".jpg"; 
				image = Imgcodecs.imread(file); 
			}
		}

		assertEquals(3, carDetector.getvalidCarCoordinates().size());
	}
	
	@Test
	public void testCarRecognition2() {	
		nu.pattern.OpenCV.loadShared();

		System.out.println("test one");
		//Reading the Image from the file  
		String file ="./testAssets/image2-0.jpg"; 
		Mat image = Imgcodecs.imread(file); 

		//Imgproc.resize(image, image, new Size(1080, 1920), 0, 0, Imgproc.INTER_AREA);

		if( image.empty() )  {
			System.out.println("empty");
		} else {
			for(int i = 0; i < 100 ; i++) {

				carDetector.run(image);
				file ="./testAssets/image2-" + i + ".jpg"; 
				image = Imgcodecs.imread(file); 
			}
		}

		assertEquals(3, carDetector.getvalidCarCoordinates().size());
	}
	
	@Test
	public void testCarRecognition3() {	
		nu.pattern.OpenCV.loadShared();

		System.out.println("test one");
		//Reading the Image from the file  
		String file ="./testAssets/image3-0.jpg"; 
		Mat image = Imgcodecs.imread(file); 

		//Imgproc.resize(image, image, new Size(1080, 1920), 0, 0, Imgproc.INTER_AREA);

		if( image.empty() )  {
			System.out.println("empty");
		} else {
			for(int i = 0; i < 100 ; i++) {

				carDetector.run(image);
				file ="./testAssets/image3-" + i + ".jpg"; 
				image = Imgcodecs.imread(file); 
			}
		}

		assertEquals(3, carDetector.getvalidCarCoordinates().size());
	}

}




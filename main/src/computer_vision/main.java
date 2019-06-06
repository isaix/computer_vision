package computer_vision;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		nu.pattern.OpenCV.loadShared();
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("mat = " + mat.dump());
	}

}

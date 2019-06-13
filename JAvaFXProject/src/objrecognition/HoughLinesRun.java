package objrecognition;


import java.util.ArrayList;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

class HoughLinesRun {

    public Mat runLine(Mat frame) {
        // Declare the output variables
        Mat dst = new Mat(), cdst = new Mat(), cdstP;

<<<<<<< HEAD
        // Check if image is loaded fine
//        if( frame.empty() ) {
//            System.out.println("Error opening image!");
//            System.out.println("Program Arguments: [image_name -- default "
//                 );
//            System.exit(-1);
//        }
        
        //! [edge_detection]
=======
>>>>>>> branch 'master' of https://github.com/isaix/computer_vision.git
        // Edge detection
<<<<<<< HEAD
        //Imgproc.Canny(frame, dst, 50, 200, 3, false);
        //Imgproc.Canny(frame, dst, 100, 800, 3, false); meget bred
        Imgproc.Canny(frame, dst, 200, 500, 3, false);
        //! [edge_detection]
=======
        Imgproc.Canny(frame, dst, 80, 150, 3, false);
>>>>>>> branch 'master' of https://github.com/isaix/computer_vision.git

        // Copy edges to the images that will display the results in BGR
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
        cdstP = cdst.clone();

        //! [hough_lines]
        // Standard Hough Line Transform
        Mat lines = new Mat(); // will hold the results of the detection
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 150); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];

            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            //Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
            Imgproc.line(cdst, pt1, pt2, new Scalar(103, 247, 12), 3, Imgproc.LINE_AA, 0);
                     
        }
        

        // Probabilistic Line Transform
        Mat linesP = new Mat(); // will hold the results of the detection
        // GAMMEL Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 10); // runs the actual detection
        Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 30); // runs the actual detection
        // Draw the lines
        ArrayList<Point> coordinatArray = new ArrayList<>();
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            //Imgproc.line(frame, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
            Imgproc.line(frame, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(103, 247, 12), 3, Imgproc.LINE_AA, 0);
            coordinatArray.add(new Point(l[0], l[1]));
            coordinatArray.add(new Point (l[2], l[3]));
            System.out.println("Punkt et " + l[0] + " og " + l[1]);            
            System.out.println("Punkt to " + l[2] + " og " + l[3]);
        }
        
       
        
        
		return frame;
       
    }
    
   
}
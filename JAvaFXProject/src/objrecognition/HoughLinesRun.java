package objrecognition;


import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

class HoughLinesRun {

    public Mat runLine(Mat frame) {
        // Declare the output variables
        Mat dst = new Mat(), cdst = new Mat(), cdstP;

        // Check if image is loaded fine
        if( frame.empty() ) {
            System.out.println("Error opening image!");
            System.out.println("Program Arguments: [image_name -- default "
                 );
            System.exit(-1);
        }
        
        //! [edge_detection]
        // Edge detection
        Imgproc.Canny(frame, dst, 50, 200, 3, false);
        //! [edge_detection]

        // Copy edges to the images that will display the results in BGR
        Imgproc.cvtColor(dst, cdst, Imgproc.COLOR_GRAY2BGR);
        cdstP = cdst.clone();

        //! [hough_lines]
        // Standard Hough Line Transform
        Mat lines = new Mat(); // will hold the results of the detection
        Imgproc.HoughLines(dst, lines, 1, Math.PI/180, 150); // runs the actual detection
        //! [hough_lines]
        //! [draw_lines]
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];

            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a*rho, y0 = b*rho;
            Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
            Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
            Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }

        // Probabilistic Line Transform
        Mat linesP = new Mat(); // will hold the results of the detection
        Imgproc.HoughLinesP(dst, linesP, 1, Math.PI/180, 50, 50, 10); // runs the actual detection
        //! [hough_lines_p]
        //! [draw_lines_p]
        // Draw the lines
        for (int x = 0; x < linesP.rows(); x++) {
            double[] l = linesP.get(x, 0);
            Imgproc.line(cdstP, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }
           
//        // Show results
//        HighGui.imshow("Source", frame);
//        HighGui.imshow("Detected Lines (in red) - Standard Hough Line Transform", cdst);
//        HighGui.imshow("Detected Lines (in red) - Probabilistic Line Transform", cdstP);
//       
//        // Wait and Exit
//        HighGui.waitKey();
//        System.exit(0);

        
		return frame;
       
    }
   
}
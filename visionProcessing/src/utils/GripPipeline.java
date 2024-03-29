package utils;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cscore.CvSource;
import edu.wpi.first.vision.VisionPipeline;
import edu.wpi.first.cameraserver.*;
import org.opencv.core.*;
import org.opencv.imgproc.*;

/**
* GripPipeline class.
*
* <p>An OpenCV pipeline generated by GRIP, with a bunch of other stuff grafted onto it after the fact
*
* @author GRIP
*/
public class GripPipeline implements VisionPipeline
{
	//Outputs (autogenerated)
	private Mat hsvThresholdOutput = new Mat(); 
	private Mat cvErodeOutput = new Mat();
	private Mat cvDilateOutput = new Mat();
	private ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
	private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();

	//networktable outputs; variables are organized by where they're used
	private boolean isTargeting; //TODO Find a better name for "foundTarget"
	private int PCSX, PCSY; //PCS coordinates (pixels)
	private double ACSX, ACSY; //ACS coordinates (relative distance from center scaling from -1 to 1)
	private double yaw, pitch; //pitch and yaw (radians)
	private double distance; //'as the crow flies' distance to target (inches)
	
	//annotated video outputs
	private Rect boxOfOblivion; //bounding box for annotated dashboard output
	private Point boxTL, boxBR = new Point(0, 0); //top left and bottom right of bounding box for annotated dashboard output
	private Scalar boxColor; //current color of bounding box for annotated dashboard output
	private Mat annotated; //copy of image to annotate and put onto output
	public CvSource output; //annotated dashboard output

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public GripPipeline() {
		output = CameraServer.getInstance().putVideo("annotatedFeed", 640, 360); //create the annotated videofeed intended for dashboard usage
	}

	/**
	 * This is the primary method that runs the entire pipeline and updates the outputs.
	 */
	public void process(Mat source0) {
		// Step HSV_Threshold0:
		Mat hsvThresholdInput = source0;
		double[] hsvThresholdHue = {50.17985611510791, 103.20819112627986};
		double[] hsvThresholdSaturation = {136.67266187050356, 255.0};
		double[] hsvThresholdValue = {96.31294964028774, 255.0};
		hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);

		// Step CV_erode0:
		Mat cvErodeSrc = hsvThresholdOutput;
		Mat cvErodeKernel = new Mat();
		Point cvErodeAnchor = new Point(-1, -1);
		double cvErodeIterations = 1.0;
		int cvErodeBordertype = Core.BORDER_CONSTANT;
		Scalar cvErodeBordervalue = new Scalar(-1);
		cvErode(cvErodeSrc, cvErodeKernel, cvErodeAnchor, cvErodeIterations, cvErodeBordertype, cvErodeBordervalue, cvErodeOutput);

		// Step CV_dilate0:
		Mat cvDilateSrc = cvErodeOutput;
		Mat cvDilateKernel = new Mat();
		Point cvDilateAnchor = new Point(-1, -1);
		double cvDilateIterations = 1.0;
		int cvDilateBordertype = Core.BORDER_CONSTANT;
		Scalar cvDilateBordervalue = new Scalar(-1);
		cvDilate(cvDilateSrc, cvDilateKernel, cvDilateAnchor, cvDilateIterations, cvDilateBordertype, cvDilateBordervalue, cvDilateOutput);

		// Step Find_Contours0:
		Mat findContoursInput = cvDilateOutput;
		boolean findContoursExternalOnly = false;
		findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

		// Step Filter_Contours0:
		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
		double filterContoursMinArea = 0.0;
		double filterContoursMinPerimeter = 0.0;
		double filterContoursMinWidth = 10.0;
		double filterContoursMaxWidth = 1000.0;
		double filterContoursMinHeight = 5.0;
		double filterContoursMaxHeight = 100.0;
		double[] filterContoursSolidity = {0, 100};
		double filterContoursMaxVertices = 1000000.0;
		double filterContoursMinVertices = 0.0;
		double filterContoursMinRatio = 0.0;
		double filterContoursMaxRatio = 1000.0;
		filterContours(filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter,
			filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight,
			filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices,
			filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio,
			filterContoursOutput);
		
		// Step Filter by size
		
		MatOfPoint largest;
		if(filterContoursOutput.size() > 0) {
			isTargeting = true;
			largest = filterContoursOutput.get(0);
			for (MatOfPoint contour : filterContoursOutput)
			{
				if(Imgproc.contourArea(contour) > Imgproc.contourArea(largest))
				{
					largest = contour;
				}
			}
			
			// Step utilize largest to find distance
			
			// First finds center rectangle and coordinates
			boxOfOblivion = Imgproc.boundingRect(largest);
			int centerX = boxOfOblivion.x + (boxOfOblivion.width / 2);
			int centerY = boxOfOblivion.y + (boxOfOblivion.height / 2); // In PCS
			PCSX = centerX;
			PCSY = centerY;

			ACSX = VisionMath.pcsXToAcsX(centerX);
			ACSY = VisionMath.pcxYtoAcsY(centerY);
			
			// Process to yaw
			yaw = VisionMath.acsToYaw(ACSX);
			
			// Process to pitch
			pitch = VisionMath.acsToPitch(ACSY);
			
			// Process to distance
			distance = VisionMath.distanceToTarget(pitch);
		} else {
			//outputs null values if there are no targets
			boxOfOblivion = null;
			isTargeting = false;
			PCSX = PCSY = 0;
			ACSX = ACSY = 0;
			yaw = 0;
			pitch = 0;
			distance = 0;
		}	

		//oh god writing in straight openCV from here on out I can't do this (you see it's funny because I'm not straight laugh now or i will eliminate you)
		//the most efficient way i could find to do it is to draw a copy of the original
		//Use rectangle around target and annotate it onto a new output

		//(if there's a memory leak blame this block of code)
		
		annotated = source0.clone();
		if (boxOfOblivion != null) {
			//using these buffer values lets us visually indicate when it's lost its target
			boxTL = boxOfOblivion.tl();
			boxBR = boxOfOblivion.br();
			boxColor = Constants.PipelineConstants.COLOR_LOCATED_BOUNDING_RECT; //set it to 'found target' color
		} else {
			boxTL = new Point(0, 0);
			boxBR = new Point(640, 360);
			boxColor = Constants.PipelineConstants.COLOR_MISSING_BOUNDING_RECT; //set it to 'missing target' color
		}
		Imgproc.rectangle(annotated, boxTL, boxBR, boxColor, Constants.PipelineConstants.THICKNESS_BOUNDING_RECT);
		output.putFrame(annotated);

		//sidestep OOM crash, hopefully
		annotated = null;
		boxTL = boxBR = null;
		boxColor = null;
		source0 = null;
	}

	/**
	 * This method is a generated getter for the output of a HSV_Threshold.
	 * @return Mat output from HSV_Threshold.
	 */
	public Mat hsvThresholdOutput() {
		return hsvThresholdOutput;
	}

	/**
	 * This method is a generated getter for the output of a CV_erode.
	 * @return Mat output from CV_erode.
	 */
	public Mat cvErodeOutput() {
		return cvErodeOutput;
	}

	/**
	 * This method is a generated getter for the output of a CV_dilate.
	 * @return Mat output from CV_dilate.
	 */
	public Mat cvDilateOutput() {
		return cvDilateOutput;
	}

	/**
	 * This method is a generated getter for the output of a Find_Contours.
	 * @return ArrayList<MatOfPoint> output from Find_Contours.
	 */
	public ArrayList<MatOfPoint> findContoursOutput() {
		return findContoursOutput;
	}

	/**
	 * This method is a generated getter for the output of a Filter_Contours.
	 * @return ArrayList<MatOfPoint> output from Filter_Contours.
	 */
	public ArrayList<MatOfPoint> filterContoursOutput() {
		return filterContoursOutput;
	}


	/**
	 * Segment an image based on hue, saturation, and value ranges.
	 *
	 * @param input The image on which to perform the HSL threshold.
	 * @param hue The min and max hue
	 * @param sat The min and max saturation
	 * @param val The min and max value
	 * @param out The image in which to store the output.
	 */
	private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val,
	    Mat out) {
		Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
		Core.inRange(out, new Scalar(hue[0], sat[0], val[0]),
			new Scalar(hue[1], sat[1], val[1]), out);
	}

	/**
	 * Expands area of lower value in an image.
	 * @param src the Image to erode.
	 * @param kernel the kernel for erosion.
	 * @param anchor the center of the kernel.
	 * @param iterations the number of times to perform the erosion.
	 * @param borderType pixel extrapolation method.
	 * @param borderValue value to be used for a constant border.
	 * @param dst Output Image.
	 */
	private void cvErode(Mat src, Mat kernel, Point anchor, double iterations,
		int borderType, Scalar borderValue, Mat dst) {
		if (kernel == null) {
			kernel = new Mat();
		}
		if (anchor == null) {
			anchor = new Point(-1,-1);
		}
		if (borderValue == null) {
			borderValue = new Scalar(-1);
		}
		Imgproc.erode(src, dst, kernel, anchor, (int)iterations, borderType, borderValue);
	}

	/**
	 * Expands area of higher value in an image.
	 * @param src the Image to dilate.
	 * @param kernel the kernel for dilation.
	 * @param anchor the center of the kernel.
	 * @param iterations the number of times to perform the dilation.
	 * @param borderType pixel extrapolation method.
	 * @param borderValue value to be used for a constant border.
	 * @param dst Output Image.
	 */
	private void cvDilate(Mat src, Mat kernel, Point anchor, double iterations,
	int borderType, Scalar borderValue, Mat dst) {
		if (kernel == null) {
			kernel = new Mat();
		}
		if (anchor == null) {
			anchor = new Point(-1,-1);
		}
		if (borderValue == null){
			borderValue = new Scalar(-1);
		}
		Imgproc.dilate(src, dst, kernel, anchor, (int)iterations, borderType, borderValue);
	}
	
	
	// NOT AT ALL THE RIGHT DEFINITION *******
	private void findContours(Mat input, boolean externalOnly,
		List<MatOfPoint> contours) {
		Mat hierarchy = new Mat();
		contours.clear();
		int mode;
		if (externalOnly) {
			mode = Imgproc.RETR_EXTERNAL;
		}
		else {
			mode = Imgproc.RETR_LIST;
		}
		int method = Imgproc.CHAIN_APPROX_SIMPLE;
		Imgproc.findContours(input, contours, hierarchy, mode, method);
	}


	/**
	 * Filters out contours that do not meet certain criteria.
	 * @param inputContours is the input list of contours
	 * @param output is the the output list of contours
	 * @param minArea is the minimum area of a contour that will be kept
	 * @param minPerimeter is the minimum perimeter of a contour that will be kept
	 * @param minWidth minimum width of a contour
	 * @param maxWidth maximum width
	 * @param minHeight minimum height
	 * @param maxHeight maximimum height
	 * @param solidity the minimum and maximum solidity of a contour
	 * @param minVertexCount minimum vertex Count of the contours
	 * @param maxVertexCount maximum vertex Count
	 * @param minRatio minimum ratio of width to height
	 * @param maxRatio maximum ratio of width to height
	 */
	private void filterContours(List<MatOfPoint> inputContours, double minArea,
		double minPerimeter, double minWidth, double maxWidth, double minHeight, double
		maxHeight, double[] solidity, double maxVertexCount, double minVertexCount, double
		minRatio, double maxRatio, List<MatOfPoint> output) {
		final MatOfInt hull = new MatOfInt();
		output.clear();
		//operation
		for (int i = 0; i < inputContours.size(); i++) {
			final MatOfPoint contour = inputContours.get(i);
			final Rect bb = Imgproc.boundingRect(contour);
			if (bb.width < minWidth || bb.width > maxWidth) continue;
			if (bb.height < minHeight || bb.height > maxHeight) continue;
			final double area = Imgproc.contourArea(contour);
			if (area < minArea) continue;
			if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) < minPerimeter) continue;
			Imgproc.convexHull(contour, hull);
			MatOfPoint mopHull = new MatOfPoint();
			mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
			for (int j = 0; j < hull.size().height; j++) {
				int index = (int)hull.get(j, 0)[0];
				double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1]};
				mopHull.put(j, 0, point);
			}
			final double solid = 100 * area / Imgproc.contourArea(mopHull);
			if (solid < solidity[0] || solid > solidity[1]) continue;
			if (contour.rows() < minVertexCount || contour.rows() > maxVertexCount)	continue;
			final double ratio = bb.width / (double)bb.height;
			if (ratio < minRatio || ratio > maxRatio) continue;
			output.add(contour);
		}
	}
	
	public boolean isTargeting()
	{
		return isTargeting;
	}
	
	public int getPCSX() {
		return PCSX;
	}

	public int getPCSY() {
		return PCSY;
	}

	public double getACSX() {
		return ACSX;
	}

	public double getACSY() {
		return ACSY;
	}
	
	public double getPitch()
	{
		return pitch;
	}
	
	public double getYaw()
	{
		return yaw;
	}
	
	public double getDistance()
	{
		return distance;
	}
}


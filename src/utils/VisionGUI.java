package utils;

import edu.wpi.cscore.CvSource;
import org.opencv.core.*;
import org.opencv.imgproc.*;

public class VisionGUI {
    public static void annotateFrame(Rect boundingBox, Mat sourceFrame, boolean isTargeting, CvSource output) {    
        //oh god writing in straight openCV from here on out I can't do this (you see it's funny because I'm not straight)

		//(if there's a memory leak blame this block of code)
		
		Mat annotated = sourceFrame.clone();
        Point boxTL;
        Point boxBR;
        Scalar boxColor;

		if (isTargeting) {
			//using these buffer values lets us visually indicate when it's lost its target
			boxTL = boundingBox.tl();
			boxBR = boundingBox.br();
			boxColor = Constants.PipelineConstants.COLOR_LOCATED_BOUNDING_RECT; //set it to 'found target' color
		} else {
			boxTL = new Point(Constants.PipelineConstants.OFFSET_BORDER_MISSING, Constants.PipelineConstants.OFFSET_BORDER_MISSING);
			boxBR = new Point(Constants.CameraConstants.RESOLUTION_X - Constants.PipelineConstants.OFFSET_BORDER_MISSING, Constants.CameraConstants.RESOLUTION_Y - Constants.PipelineConstants.OFFSET_BORDER_MISSING);
			boxColor = Constants.PipelineConstants.COLOR_MISSING_BOUNDING_RECT; //set it to 'missing target' color
		}
		Imgproc.rectangle(annotated, boxTL, boxBR, boxColor, Constants.PipelineConstants.THICKNESS_BOUNDING_RECT);
		output.putFrame(annotated);

		//sidestep OOM crash, hopefully
		annotated = null;
		boxTL = boxBR = null;
		boxColor = null;
		sourceFrame = null;
    }
}

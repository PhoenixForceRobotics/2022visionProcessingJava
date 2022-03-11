package utils;

import java.lang.Math;
import org.opencv.core.Scalar;

public class Constants {
    //In the interest of avoiding magic numbers, all constants are here.
    public static String CONFIG_FILE_PATH = "/boot/frc.json";
    
    public static class CameraConstants {
        //Constants related strictly to the camera itself.
        public static int[] RESOLUTION = {640, 360}; //camera resolution, it's just 16:9 360p because that makes the FOV equation not a lovecraftian nightmare
        public static int X_RESOLUTION = RESOLUTION[0];
        public static int Y_RESOLUTION = RESOLUTION[1];
        
        public static double FOV_HORIZONTAL = 61.372724804; //more convenient access variables
        public static double FOV_VERTICAL = 36.9187407266;
        
        //TODO: get better values for these
        public static double HEIGHT_CAMERA = 39; //how far the camera is off the ground (not the robot), measured in inches
        
        //TODO: get actual value for this from design/build
        public static double ANGLE_CAMERA = 36; //angle the camera is oriented at in degrees, with 0 being oriented parallel to the floor
    }

    public static class PipelineConstants {
        //Constants related to
        public static int MEDIAN_FILTER_COUNT = 11; //how many entries the medianFilter holds at once; more entries means more resistance to errors but also more latency
        public static Scalar COLOR_LOCATED_BOUNDING_RECT = new Scalar(255, 0, 0); //color of the bounding rectangle for vision targets on the dashboard; color in BGR order (NOT RGB)
        public static Scalar COLOR_MISSING_BOUNDING_RECT = new Scalar(0, 0, 255); //color of the rectangle if no vision targets are found; color in BGR order (NOT RGB)
        public static int THICKNESS_BOUNDING_RECT = 3; //thickness of the bounding rectangle for vision targets on the dashboard, measured in pixels
    }

    public static class TargetConstants {
        //Constants related strictly to the target itself.
        //103 is practice target height, 104 is FRC target height
        public static double HEIGHT_TARGET = 104; //how far the vision target is off the ground, also measured in inches
    }

    public static class TurretConstants {
        //Constants related strictly to the shooter itself.
        public static double GRAVITY = 386.0892; //force of earth's gravity at sea level in inches/second/second; it's in inches to stay consistent with everything else
        public static double HEIGHT_TURRET = 0; //height of the turret off the ground in inches
    }
}

package utils;

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
        public static double HEIGHT_CAMERA = 0; //how far the camera is off the ground (not the robot), measured in feet
        
        //TODO: get actual value for this from design/build
        public static double ANGLE_CAMERA = 0; //angle the camera is oriented at in degrees, with 0 being oriented parallel to the floor
    }

    public static class TargetConstants {
        //Constants related strictly to the target itself.
        
        public static double HEIGHT_TARGET = .2; //how far the vision target is off the ground, also measured in feet
    }

    public static class TurretConstants {
        //Constants related strictly to the shooter itself.
        public static double Gravity = 32.1741; //force of earth's gravity at sea level in feet/second/second; it's in feet to stay consistent with everything else
    }
}

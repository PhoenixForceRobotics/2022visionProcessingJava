package utils;

public class Constants {
    //In the interest of avoiding magic numbers, all constants are here.
    public static String CONFIG_FILE_PATH = "/boot/frc.json";
    
    public static class CameraConstants {
        //Constants related strictly to the camera itself.
        public static int[] RESOLUTION = {640, 360}; //camera resolution, it's just 16:9 360p because that makes the FOV equation not a lovecraftian nightmare
        
        public static double[] FOV = {61.372724804, 36.9187407266}; //camera field of view, measured in degrees (and it's also actually precise)
        public static double FOV_HORIZONTAL = FOV[0]; //more convenient access variables
        public static double FOV_VERTICAL = FOV[1];
        
        //TODO: get better values for these
        public static double HEIGHT_CAMERA = 4.33333333; //how far the camera is off the ground (not the robot), measured in feet
        
        //TODO: get actual value for this from design/build
        public static double ANGLE_CAMERA = 0; //angle the camera is oriented at in degrees, with 0 being oriented parallel to the floor
    }
    public static class TargetConstants {
        //Constants related strictly to the target itself.
        
        public static double HEIGHT_TARGET = 8.44791666; //how far the vision target is off the ground, also measured in feet
    }
}

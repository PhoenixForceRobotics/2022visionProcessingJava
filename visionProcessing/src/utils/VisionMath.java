package utils;

/* A port of all the python functions. */

public class VisionMath {
    
    public static double pcsXToAcsX(double coordinate)
    {
        return pcsToAcs(coordinate, Constants.CameraConstants.X_RESOLUTION); // inverts the value
    }
    
    public static double pcxYtoAcsY(double coordinate)
    {
        return -pcsToAcs(coordinate, Constants.CameraConstants.Y_RESOLUTION);
    }
    
    private static double pcsToAcs(double coordinate, double resolution) {
        /*
        Converts from Pixel Coordinate System ((0, 0) in top left, (640, 360) in bottom right; the actual pixel coordinates)
        to Aiming Coordinate System ((-1.0, 1.0) in top left, (1.0, -1.0) in bottom right).\n

        Takes an ordered pair in PCS as argument and returns an ordered pair in ACS.\n
        Optional args: resolution is the resolution the camera is working in.
        */
        double halfOfResolution = resolution / 2;
        return (coordinate - halfOfResolution) / halfOfResolution; //thing from FRC
    }

    public static double acsToPitch(double ACSY) {
        /*
        Finds pitch from ACS position.\n\n
    
        Takes an ordered pair in ACS as argument and returns the pitch in degrees.\n
        Optional args: fov is the vertical FOV of the camera.
        */
        double fov = Constants.CameraConstants.FOV_VERTICAL;
        double pitch = (ACSY / 2) * fov; //more FRC magic that is a bit confusing
        return pitch;
    }

    public static double acsToYaw(double ACSX) {
        /*
        Finds yaw from ACS position.\n

        Takes an ordered pair in ACS as argument and returns the yaw in degrees.
        Optional args: FOV is the horizontal FOV of the camera.
        */
        double fov = Constants.CameraConstants.FOV_HORIZONTAL;
        double yaw = (ACSX / 2) * fov; //the same FRC arcane sorcery
        return yaw;
    }
    public static double distanceToTarget(double pitch) {
        /*
        Finds distance to the target, assuming the vision target is within view.\n
        Takes the pitch in degrees as argument and returns the distance to the target in inches.\n
        Optional args: cam_height is how high the camera is off the ground, cam_angle is the angle the camera is oriented at, target_height is how high the target is off the ground.
        */
        double camHeight = Constants.CameraConstants.HEIGHT_CAMERA;
        double camAngle = Constants.CameraConstants.ANGLE_CAMERA;
        double targetHeight = Constants.TargetConstants.HEIGHT_TARGET;
        
        return (targetHeight - camHeight) / Math.tan(camAngle + pitch); //a magic thing from frc that we worked out for ourselves (but first from frc) :3
    }
}
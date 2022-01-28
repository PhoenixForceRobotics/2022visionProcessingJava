package utils;

/* A port of all the python functions. */

public class VisionMath {
    public static double[] pcs_to_acs(int[] pixelcoords) {
        /*
        Converts from Pixel Coordinate System ((0, 0) in top left, (640, 360) in bottom right; the actual pixel coordinates)
        to Aiming Coordinate System ((-1.0, 1.0) in top left, (1.0, -1.0) in bottom right).\n

        Takes an ordered pair in PCS as argument and returns an ordered pair in ACS.\n
        Optional args: resolution is the resolution the camera is working in.
        */
        double[] acs = {0, 0};
        int resolution;
        for(int i = 0; i < pixelcoords.length; i++) { //calculate ACS for both horizontal and vertical
            double halfofres = Constants.CameraConstants.RESOLUTION[i] / 2; //pre-calculate half of resolution for that 1% SPEEEEEEEEED
            acs[i] = (double) (pixelcoords[i] - halfofres) / halfofres; //thing from FRC
        }
        acs[1] *= -1; //un-mirrors y axis
        return acs; //returns values
    }

    public static double acs_to_pitch(double[] acscoords) {
        /*
        Finds pitch from ACS position.\n\n
    
        Takes an ordered pair in ACS as argument and returns the pitch in degrees.\n
        Optional args: fov is the vertical FOV of the camera.
        */
        double fov = Constants.CameraConstants.FOV_VERTICAL;
        double pitch = (acscoords[0] / 2) * fov; //more FRC magic that is a bit confusing
        return pitch;
    }

    public static double acs_to_yaw(double[] acscoords) {
        /*
        Finds yaw from ACS position.\n

        Takes an ordered pair in ACS as argument and returns the yaw in degrees.
        Optional args: FOV is the horizontal FOV of the camera.
        */
        double fov = Constants.CameraConstants.FOV_HORIZONTAL;
        double yaw = (acscoords[1] / 2) * fov; //the same FRC arcane sorcery
        return yaw;
    }
    public static double distance_to_target(double pitch) {
        /*
        Finds distance to the target, assuming the vision target is within view.\n
        Takes the pitch in degrees as argument and returns the distance to the target in inches.\n
        Optional args: cam_height is how high the camera is off the ground, cam_angle is the angle the camera is oriented at, target_height is how high the target is off the ground.
        */
        double cam_height = Constants.CameraConstants.HEIGHT_CAMERA;
        double cam_angle = Constants.CameraConstants.ANGLE_CAMERA;
        double target_height = Constants.TargetConstants.HEIGHT_TARGET;
        
        return (target_height - cam_height) / Math.tan(cam_angle + pitch); //a magic thing from frc that we worked out for ourselves (but first from frc) :3
    }
}
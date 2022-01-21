package utils;

public class Camera extends VideoSource
{
    public Camera(CameraConfig cameraConfig)
    {
        // Start Running The Camera when initialized
        System.out.println("Starting camera '" + cameraConfig.name + "' on " + cameraConfig.path);
        CameraServer cameraServer = CameraServer.getInstance(); /* important */
        UsbCamera camera = new UsbCamera(cameraConfig.name, cameraConfig.path); /* important */
        MjpegServer server = cameraServer.startAutomaticCapture(camera); //important
    
        Gson gson = new GsonBuilder().create();
    
        camera.setConfigJson(gson.toJson(cameraConfig.config));
        camera.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
    
        if ( cameraConfig.streamConfig != null) {
            server.setConfigJson(gson.toJson(config.streamConfig));
        }
    }
}

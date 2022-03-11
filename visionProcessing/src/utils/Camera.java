package utils;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import org.json.JSONObject;

public class Camera extends UsbCamera
{
    private CameraData cameraData;

    public Camera(CameraData cameraData) {
        super(cameraData.getName(), cameraData.getPath());
        this.cameraData = cameraData;
    }
    
    public static CameraData readCameraConfig(JSONObject cameraJSONObj) {
        CameraData outputData = new CameraData();
        
        // name
        outputData.setName(cameraJSONObj.getString("name"));
        
        // path
        outputData.setPath(cameraJSONObj.getString("path"));
        
        // stream properties
        outputData.setStreamConfig(cameraJSONObj.getJSONObject("stream"));
    
        // general JSON Obj for debugging and initializing the camera
        outputData.setConfig(cameraJSONObj);

        return outputData;
    }

    // Start Running The Camera when told to 
    public void startCamera()
    {
        System.out.println("Starting camera '" + cameraData.getName() + "' on " + cameraData.getPath());
        
        CameraServer.getInstance().startAutomaticCapture(this);

        setConfigJson(cameraData.getConfig().toString());
        setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
    }   
}

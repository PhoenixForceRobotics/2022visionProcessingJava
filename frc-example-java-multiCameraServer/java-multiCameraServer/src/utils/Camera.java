package utils;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import org.json.JSONObject;

public class Camera extends UsbCamera
{
    private CameraData cameraData;

    public Camera(CameraData cameraData)
    {
        super(cameraData.getName(), cameraData.getPath());
        
    }
    
    private static void parseError(String str)
    {
        System.err.println("config error in '" + Constants.CONFIG_FILE_PATH + "': " + str);
    }
    
    public static CameraData readCameraConfig(JSONObject cameraJSONObj)
    {
        CameraData outputData = new CameraData();
        
        // name
        outputData.setName(cameraJSONObj.getJSONObject("name").getString());
    
        // path
        outputData.setPath(cameraJSONObj.getJSONObject("path").getString());
        
        // stream properties
        outputData.setStreamConfig(cameraJSONObj.get("stream"));
    
        // general JSON Obj for debugging
        outputData.setConfig(cameraJSONObj);

        return outputData;
    }

    // Start Running The Camera when told to 
    public void startCamera()
    {
        System.out.println("Starting camera '" + cameraData.getName() + "' on " + cameraData.getPath());
        
        CameraServer.getInstance().startAutomaticCapture(this);

        this.setConfigJson(cameraData.getConfig().toString());
        this.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
    }   
}

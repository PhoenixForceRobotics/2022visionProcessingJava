package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;

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
    
    public static CameraData readCameraConfig(JsonObject cameraJSONObj)
    {
        CameraData outputData = new CameraData();
        
        // name
        JsonElement nameElement = cameraJSONObj.get("name");
        if (nameElement == null) {
            parseError("could not read camera name");
            System.exit(1);
        }
        outputData.setName(nameElement.getAsString());
    
        // path
        JsonElement pathElement = cameraJSONObj.get("path");
        if (pathElement == null) {
            parseError("camera '" + outputData.getName() + "': could not read path");
            System.exit(1);
        }
        
        outputData.setPath(pathElement.getAsString());
    
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

        Gson gson = new GsonBuilder().create();

        this.setConfigJson(gson.toJson(cameraData.getConfig()));
        this.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
    }   
}

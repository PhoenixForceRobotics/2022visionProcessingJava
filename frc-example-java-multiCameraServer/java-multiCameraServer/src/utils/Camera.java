package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Camera extends UsbCamera
{
    public String name;
    public String path;
    public JsonObject config;
    public JsonElement streamConfig;
    public CameraConfig cameraConfig;

    public Camera(JsonObject cameraJSONObj)
    {
        readCameraConfig(cameraJSONObj);
    }
    public boolean readCameraConfig(JsonObject cameraJSONObj) 
    {
        CameraConfig cam = new CameraConfig();
    
        // name
        JsonElement nameElement = cameraJSONObj.get("name");
        if (nameElement == null) {
          parseError("could not read camera name");
          return false;
        }
        name = nameElement.getAsString();
    
        // path
        JsonElement pathElement = cameraJSONObj.get("path");
        if (pathElement == null) {
          parseError("camera '" + cam.name + "': could not read path");
          return false;
        }
        path = pathElement.getAsString();
    
        // stream properties
        streamConfig = cameraJSONObj.get("stream");
    
        this.config = cameraJSONObj;

        return true;
    }

    // Start Running The Camera when told to 
    public VideoSource startCamera()
    {
        System.out.println("Starting camera '" + name + "' on " + path);
        
        CameraServer.getInstance().startAutomaticCapture(camera);

        Gson gson = new GsonBuilder().create();

        this.setConfigJson(gson.toJson(this.config))    ;
        this.setConnectionStrategy(VideoSource.ConnectionStrategy.kKeepOpen);
    }   
}

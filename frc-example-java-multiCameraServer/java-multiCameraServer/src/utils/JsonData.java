package utils;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.* ;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JsonData
{
    private String jsonString;
    private String configFilePath;
    private int team;
    private boolean isServer;
    private ArrayList<Camera> cameras;

    public JsonData()
    {
        this(Constants.CONFIG_FILE_PATH);
    }
    
    public JsonData(String configFilePath)
    {
        cameras = new ArrayList<Camera>();
        this.configFilePath = configFilePath;
        jsonString = "";
    }
    
    // Report parse error
    private void parseError(String str)
    {
        System.err.println("config error in '" + configFilePath + "': " + str);
    }
    
    // Read configuration files
    public void readConfig() throws IOException
    {
        // Find file
        jsonString = Files.readString(Paths.get(getConfigFilePath()));
    
        System.out.println(jsonString);
        JSONObject visionConfig = new JSONObject(jsonString);
        
        // Team number
        setTeam(visionConfig.getInt("team"));
        
        // Network Table Mode (optional)
        if (visionConfig.has("ntmode"))
        {
            String NTModeString = visionConfig.getString("ntmode");
            if ("client".equalsIgnoreCase(NTModeString))
            {
                setServer(false);
            }
            else if ("server".equalsIgnoreCase(NTModeString))
            {
                setServer(true);
            }
            else
            {
                parseError("could not understand ntmode value '" + NTModeString + "'");
            }
        } 
        else 
        {
            setServer(false);
        }
        
        // Cameras
        JSONArray cameraJSONs = visionConfig.getJSONArray("cameras");
        for (int i = 0; i < cameraJSONs.length(); i++)
        {
            addCamera(new Camera(Camera.readCameraConfig(cameraJSONs.getJSONObject(i))));
        }
    }
    
    public void setJsonString(String jsonString)
    {
        this.jsonString = jsonString;
    }
    
    public void setConfigFilePath(String configFilePath)
    {
        this.configFilePath = configFilePath;
    }

    public void setTeam(int team)
    {
        this.team = team;
    }

    public void setServer(boolean server)
    {
        isServer = server;
    }
    
    public void addCamera(Camera camera)
    {
        cameras.add(camera);
    }
    
    public String getConfigFilePath()
    {
        return configFilePath;
    }
    
    public String getJsonString()
    {
        return jsonString;
    }
    
    public int getTeam()
    {
        return team;
    }

    public boolean isServer()
    {
        return isServer;
    }

    public Camera[] getCameraArray()
    {
        return cameras.toArray(new Camera[cameras.size()]);
    }
}







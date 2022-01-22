package utils;

import utils.* ;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonData
{
    private static String configFilePath= Constants.CONFIG_FILE_PATH;
    private int team;
    private boolean isServer;
    private ArrayList<Camera> cameras;

    public JsonData()
    {
        cameras = new ArrayList<Camera>();
    }
    
    // Report parse error
    public static void parseError(String str)
    {
        System.err.println("config error in '" + configFile + "': " + str);
    }
    
    // Read configuration files
    @SuppressWarnings("PMD.CyclomaticComplexity")
    public void readConfig(String input)
    {              
        // Store config file path
        setConfigFilePath(input);
        
        // Parse file
        JsonElement topElement;
        try
        {
            topElement = new JsonParser().parse(Files.newBufferedReader(Paths.get(this.configFilePath)));
        } catch (IOException ex) {
            System.err.println("could not open '" + configFilePath + "': " + ex);
            System.exit(1);
        }
        
        // Top level must be an object
        if (!topElement.isJsonObject())
        {
            parseError("must be JSON object");
            System.exit(1);
        }
        JsonObject JSONObj = topElement.getAsJsonObject();
        
        // Team number
        JsonElement teamElement = JSONObj.get("team");
        if (teamElement == null)
        {
            parseError("could not read team number");
            System.exit(1);
        }
        setTeam(teamElement.getAsInt());
        
        // Network Table Mode (optional)
        if (JSONObj.has("ntmode"))
        {
            String NTModeStr = JSONObj.get("ntmode").getAsString();
            if ("client".equalsIgnoreCase(NTModeStr))
            {
                setServer(false);
            }
            else if ("server".equalsIgnoreCase(NTModeStr))
            {
                setServer(true);
            }
            else
            {
                parseError("could not understand ntmode value '" + NTModeStr + "'");
            }
        } 
        else 
        {
            setServer(false);
        }
        
        // Cameras
        JsonElement camerasElement = JSONObj.get("cameras");
        if (camerasElement == null)
        {
            parseError("could not read cameras");
            System.exit(1);
        }
        JsonArray cameraJSONs = camerasElement.getAsJsonArray();
        for (JsonElement camera : cameras)
        {
            addCamera(new Camera(camera.getAsJsonObject()));
        }
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







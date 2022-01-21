package utils;

import utils.*;

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

public class JSONUtils
{
    
    public JSONUtils()
    {
    }
    
    // Report parse error.
    
    public static void parseError(String str)
    {
        System.err.println("config error in '" + configFile + "': " + str);
    }
    
    //Read configuration files
    
    @SuppressWarnings("PMD.CyclomaticComplexity")
    public static ConfigFields readConfig(String configFilePath)
    {
        ConfigFields outputConfigFields = new ConfigFields();
        
        // parse file
        JsonElement top;
        try
        {
            top = new JsonParser().parse(Files.newBufferedReader(Paths.get(configFilePath)));
        } catch (IOException ex)
        {
            System.err.println("could not open '" + configFilePath + "': " + ex);
            System.exit(1);
        }
        
        // top level must be an object
        if (!top.isJsonObject())
        {
            parseError("must be JSON object");
            System.exit(1);
        }
        JsonObject obj = top.getAsJsonObject();
        
        // team number
        JsonElement teamElement = obj.get("team");
        if (teamElement == null)
        {
            parseError("could not read team number");
            System.exit(1);
        }
        outputConfigFields.setTeam(teamElement.getAsInt());
        
        // ntmode (optional)
        if (obj.has("ntmode"))
        {
            String str = obj.get("ntmode").getAsString();
            if ("client".equalsIgnoreCase(str))
            {
                outputConfigFields.setServer(false);
            }
            else if ("server".equalsIgnoreCase(str))
            {
                outputConfigFields.setServer(true);
            }
            else
            {
                parseError("could not understand ntmode value '" + str + "'");
            }
        }
        
        // cameras
        JsonElement camerasElement = obj.get("cameras");
        if (camerasElement == null)
        {
            parseError("could not read cameras");
            System.exit(1);
        }
        JsonArray cameras = camerasElement.getAsJsonArray();
        for (JsonElement camera : cameras)
        {
            outputConfigFields.addCameraConfig(readCameraConfig(camera.getAsJsonObject()));
        }
        return outputConfigFields; //passed all tests if able to get here, must be completed
    }
}


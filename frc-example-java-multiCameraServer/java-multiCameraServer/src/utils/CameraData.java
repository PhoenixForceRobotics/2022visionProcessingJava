package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CameraData
{
    private String name;
    private String path;
    private JsonObject config;
    private JsonElement streamConfig;
    
    public CameraData()
    {
    
    }
    public CameraData(String name, String path, JsonObject config, JsonElement streamConfig)
    {
        this.name = name;
        this.path = path;
        this.config = config;
        this.streamConfig = streamConfig;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getPath()
    {
        return path;
    }
    
    public JsonObject getConfig()
    {
        return config;
    }
    
    public JsonElement getStreamConfig()
    {
        return streamConfig;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void setPath(String path)
    {
        this.path = path;
    }
    
    public void setConfig(JsonObject config)
    {
        this.config = config;
    }
    
    public void setStreamConfig(JsonElement streamConfig)
    {
        this.streamConfig = streamConfig;
    }
}

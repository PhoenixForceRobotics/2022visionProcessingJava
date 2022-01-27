package utils;

import org.json.JSONObject;

public class CameraData
{
    private String name;
    private String path;
    private JSONObject config;
    private JSONObject streamConfig;
    
    public CameraData()
    {
    
    }
    public CameraData(String name, String path, JSONObject config, JSONObject streamConfig)
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
    
    public JSONObject getConfig()
    {
        return config;
    }
    
    public JSONObject getStreamConfig()
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
    
    public void setConfig(JSONObject config)
    {
        this.config = config;
    }
    
    public void setStreamConfig(JSONObject streamConfig)
    {
        this.streamConfig = streamConfig;
    }
}

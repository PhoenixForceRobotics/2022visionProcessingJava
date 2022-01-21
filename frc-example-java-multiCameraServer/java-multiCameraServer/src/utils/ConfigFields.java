package utils;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

public class ConfigFields
{
    private int team;
    private boolean isServer;
    private List<CameraConfig> cameraConfigs;
    private List<VideoSource> cameras;

    public ConfigFields(int team, boolean isServer, List<CameraConfig> cameraConfigs, List<VideoSource> cameras, boolean isComplete)
    { 
        this.team = team;
        this.isServer = isServer;
        this.cameraConfigs = cameraConfigs;
        this.cameras = cameras;
        this.isComplete = isComplete;
    }

    public ConfigFields(int team, boolean isServer, boolean isComplete)
    {
        this(team, isServer, new ArrayList(), new ArrayList(), isComplete);
    }
    
    public ConfigFields()
    {
    
    }
    
    public void setTeam(int team)
    {
        this.team = team;
    }
    
    public void setServer(boolean server)
    {
        isServer = server;
    }
    
    public void addCameraConfig(CameraConfig cameraConfig)
    {
        cameraConfigs.add(cameraConfig);
    }
    
    public void setCameras(List<VideoSource> cameras)
    {
        this.cameras = cameras;
    }
    
    public int getTeam()
    {
        return team;
    }
    
    public boolean isServer()
    {
        return isServer;
    }
    
    public List<CameraConfig> getCameraConfigs()
    {
        return cameraConfigs;
    }
    
    public List<VideoSource> getCameras()
    {
        return cameras;
    }
}
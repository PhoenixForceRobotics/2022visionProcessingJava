// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

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

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.vision.VisionPipeline;
import edu.wpi.first.vision.VisionThread;

import org.opencv.core.Mat;

import static utils.JSONUtils.*;

public final class Main
{
  // Runs the actual program
  public static void main(String... args) {
    JsonData jsonData = new JsonData();
  
    if (args.length > 0) {
      configFilePath = args[0];
    }

    // read configuration
    jsonData.readConfig(Constants.CONFIG_FILE_PATH);

    // start NetworkTables
    NetworkTableInstance networkTableInstance = NetworkTableInstance.getDefault();
    if (isServer) {
      System.out.println("Setting up NetworkTables server");
      networkTableInstance.startServer();
    } else {
      System.out.println("Setting up NetworkTables client for team " + jsonData.getTeam());
      networkTableInstance.startClientTeam(jsonData.getTeam());
      networkTableInstance.startDSClient();
    }

    // start cameras
    for (Camera camera : jsonData.getCameraArray()) {
      camera.startCamera();
    }

    // start image processing on camera 0 if present
    if (cameras.size() > 0) {
      VisionThread visionThread = new VisionThread(
        cameras.get(0),
        new GripPipeline(), 
        pipeline -> 
        {
        // do something with pipeline results (contours in our case)
        //TODO: publish to network table here?
        }
      );
      visionThread.start();
    }

    // loop forever
    while (true) {
      try {
        Thread.sleep(10000);
      } catch (InterruptedException ex) {
        return;
      }
    }
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import utils.*;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.vision.VisionThread;

import org.opencv.core.*;
import org.opencv.core.Core.*;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.*;
import org.opencv.objdetect.*;


public final class Main {
  
  private static JsonData json = new JsonData();
  private static final Object imgLock = new Object();
  
  // Runs the actual program
  public static void main(String... args) {
    if (args.length > 0) {
      json.setConfigFilePath(args[0]);
    }

    // read configuration
    json.readConfig(Constants.CONFIG_FILE_PATH);

    // start NetworkTables
    NetworkTableInstance networkTableInstance = NetworkTableInstance.getDefault();
    if (json.isServer()) {
      System.out.println("Setting up NetworkTables server");
      networkTableInstance.startServer();
    } else {
      System.out.println("Setting up NetworkTables client for team " + json.getTeam());
      networkTableInstance.startClientTeam(json.getTeam());
      networkTableInstance.startDSClient();
    }

    // start cameras
    for (Camera camera : json.getCameraArray()) {
      camera.startCamera();
    }

    // start image processing on camera 0 if present
    if (json.getCameraArray().length > 0) {
      VisionThread visionThread = new VisionThread(
        json.getCameraArray()[0], //TODO: sort through cameras, find the one of specified path
        new GripPipeline(),
        pipeline->
        {
          
          // do something with pipeline results (upload to network table)
        // TODO: publish to network table here?
        }
      );
      visionThread.start();
    }

    // loop forever
    while (true) {
      try {
        Thread.sleep(10000);
      } catch (InterruptedException ex) {
        System.exit(1);
      }
    }
  }
}

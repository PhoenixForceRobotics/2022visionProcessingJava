// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.MedianFilter;
import utils.*;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.vision.VisionThread;

import java.io.IOException;

public final class Main {
  
  // Runs the actual program
  public static void main(String... args) throws IOException
  {
    JsonData json;
  
    if (args.length > 0) {
      json = new JsonData(args[0]);
    }
    else
    {
      json = new JsonData(Constants.CONFIG_FILE_PATH);
    }
    
    // read configuration
    json.readConfig();

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
    // Defines every table entry that we use
    NetworkTable table = networkTableInstance.getTable("PiVisionData");
    
    NetworkTableEntry hasTarget = table.getEntry("hasTarget");
    NetworkTableEntry ACSXCoordinate = table.getEntry("ACSX");
    NetworkTableEntry ACSYCoordinate = table.getEntry("ACSY");
    NetworkTableEntry PCSXCoordinate = table.getEntry("PCSX");
    NetworkTableEntry PCSYCoordinate = table.getEntry("PCSY");
    NetworkTableEntry yawEntry = table.getEntry("yaw");
    NetworkTableEntry pitchEntry = table.getEntry("pitch");
    NetworkTableEntry distanceEntry = table.getEntry("distance");
    
    // Create medianFilters
    MedianFilter ACSFilterX = new MedianFilter(9);
    MedianFilter ACSFilterY = new MedianFilter(9);
    MedianFilter PCSFilterX = new MedianFilter(9);
    MedianFilter PCSFilterY = new MedianFilter(9);
    MedianFilter yawFilter = new MedianFilter(9);
    MedianFilter pitchFilter = new MedianFilter(9);
    MedianFilter distanceFilter = new MedianFilter(9);
    
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
          hasTarget.setBoolean(pipeline.isTargeting());
          ACSXCoordinate.setDouble(ACSFilterX.calculate(pipeline.getACS()[0]));
          ACSYCoordinate.setDouble(ACSFilterY.calculate(pipeline.getACS()[1]));
          PCSXCoordinate.setDouble(PCSFilterX.calculate(pipeline.getPCS()[0]));
          PCSYCoordinate.setDouble(PCSFilterY.calculate(pipeline.getPCS()[1]));
  
          PCSXCoordinate.setDouble(PCSFilterX.calculate(pipeline.getPCS()[0]));
          yawEntry.setDouble(yawFilter.calculate(pipeline.getYaw()));
          pitchEntry.setDouble(pitchFilter.calculate(pipeline.getPitch()));
          distanceEntry.setDouble(distanceFilter.calculate(pipeline.getDistance()));
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

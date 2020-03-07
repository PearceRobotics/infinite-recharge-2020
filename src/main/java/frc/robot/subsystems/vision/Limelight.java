/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.vision;

import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * Add your docs here.
 */
public class Limelight {


  /*
   * Vertical Offset From Crosshair To Target (-20.5 degrees to 20.5 degrees)
   */
  public double getVerticalTargetOffset() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0.0);
  }

  /*
   * Horizontal Offset From Crosshair To Target (-27 degrees to 27 degrees)
   */
  public double getHorizontalTargetOffset() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tx").getDouble(0.0);
  }

  /*
   * Skew or rotational offset between 0 and -90
   */
  public double getRotation() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ts").getDouble(0.0);
  }

  /*
   * Results of a 3D position solution, 6 numbers: Translation (x,y,z)
   * Rotation(pitch,yaw,roll)
   */
  public double get3DInputs() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("camtran").getDouble(0.0);
  }

  /*
   * The pipeline’s latency contribution (ms) Add at least 11ms for image capture
   * latency.
   */
  public double latency() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tl").getDouble(0.0);
  }

  /*
   * Sidelength of shortest side of the fitted bounding box (pixels)
   */
  public double shortSide() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tshort").getDouble(0.0);
  }

  /*
   * Sidelength of longest side of the fitted bounding box (pixels)
   */
  public double longSide() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tlong").getDouble(0.0);
  }

  /*
   * Horizontal sidelength of the rough bounding box (0 - 320 pixels)
   */
  public double boundBoxHorizontal() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("thor").getDouble(0.0);
  }

  /*
   * Vertical sidelength of the rough bounding box (0 - 320 pixels)
   */
  public double boundBoxVertical() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tvert").getDouble(0.0);
  }

  /*
   * True active pipeline index of the camera (0 ... 9)
   */
  public double getPipeline() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("getpipe").getDouble(0.0);
  }

  /*
   * Whether the limelight has any valid targets (0 or 1)
   */
  public boolean hasValidTarget() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("tv").getDouble(0.0) > 0;
  }

  /*
   * Target Area (0% of image to 100% of image)
   */
  public double getTargetArea() {
    return NetworkTableInstance.getDefault().getTable("limelight").getEntry("ta").getDouble(0.0);
  }

  /*
   * Set pipeline index for the camera (0 ... 9)
   */
  public void setPipeline(Number pipeline) {
    NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(pipeline);
  }

  public void setHighGoalPipeline(){
    setPipeline(0);
  }
  public void setLowGoalPipeline(){
    setPipeline(1);
  }
}
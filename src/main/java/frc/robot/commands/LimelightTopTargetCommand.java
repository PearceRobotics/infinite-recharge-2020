/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.DrivingDeltas;

/**
 * Add your docs here.
 */
public class LimelightTopTargetCommand extends CommandBase {
    private final double KpAIM = 0.005;
    private final double KpDISTANCE = 0.025; // larger than kpAim in initial
    private final double DEADBAND_DEGREES = 0;
    private Drive drive;
    private double steeringAdjust = 0;
    private double distanceAdjust = 0;

    public LimelightTopTargetCommand(private Drive drive) {
        this.drive = drive;

    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        if (hasValidTarget()) {
            if (Math.abs(getHorizontalTargetOffset()) > DEADBAND_DEGREES) {
                steeringAdjust = KpAIM * getHorizontalTargetOffset();
            }

            if (Math.abs(getVerticalTargetOffset()) > DEADBAND_DEGREES) {
                distanceAdjust = KpDISTANCE * getVerticalTargetOffset();
            }
        }

        drive.arcadeDrive(-distanceAdjust, steeringAdjust);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if (getHorizontalTargetOffset() < DEADBAND_DEGREES) {
            return true;
        }
        return false;
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {

    }

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
     * Results of a 3D position solution, 6 numbers: Translation (x,y,y)
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
     * True active pipeline index of the camera (0 .. 9)
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
}

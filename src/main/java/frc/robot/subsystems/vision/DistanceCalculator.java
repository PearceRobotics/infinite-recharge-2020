package frc.robot.subsystems.vision;

import frc.robot.Constants;

public class DistanceCalculator{

    private static final double CAMERA_HEIGHT = 24.0; //inches
    private static final double CAMERA_ANGLE_DEGREES = 26.85; //degrees 
    private static final double CAMERA_ANGLE_RADIANS = Math.toRadians(CAMERA_ANGLE_DEGREES); 
    private static final double VISION_TARGET_HEIGHT = 90.5; //inches 

public static double getDistanceFromTarget(double targetAngleRadians)
{
    //d = 161.5
    //TODO need to aim at inner goal, currently aimed at outer goal
    return (VISION_TARGET_HEIGHT - CAMERA_HEIGHT) / Math.tan(CAMERA_ANGLE_RADIANS + targetAngleRadians);
}



}
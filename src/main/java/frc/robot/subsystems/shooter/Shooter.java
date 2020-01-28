/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.shooter;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * Add your docs here.
 */
public class Shooter {

    private final int leftMotorCanId = 11;
    private final int rightMotorCanId = 12;
    private final CANSparkMax leftMotor = new CANSparkMax(leftMotorCanId, MotorType.kBrushless);
    private final CANSparkMax rightMotor = new CANSparkMax(rightMotorCanId,MotorType.kBrushless);

    private final double wheelDiameter = 4; //inch
    private final double Gravity = 386.0886;
    private final double maxRpm = 7640.0; 
    private final double launcherHeight = 24.0; //inch
    private final double energyLost = 0.66; 
    private final double wheelCircumference = wheelDiameter*Math.PI;
    private final double maxTangentialSpeed = ( wheelCircumference * (maxRpm /60));
    private final double initialUpperBound = 1.0;
    private final double initalLowerBound = 0.0;
    private double launchPower = initialUpperBound;
    private final double degrees = 58.0;
    private final double radians = degrees * (Math.PI/180);
    private double desiredHeight = 88.0;
    private double distanceToTarget;
    private double maxLaunchSpeed = maxTangentialSpeed*(1 - energyLost);
    private double horizontalSpeed = Math.cos(radians) * maxLaunchSpeed * launchPower;;
    private double travelTime = distanceToTarget/horizontalSpeed;;
    private double heightAtTargetDistance;
    private boolean shouldShoot;

    public void determineHeightAtTargetDistance(){
        heightAtTargetDistance = launcherHeight + (Math.sin(radians)* maxLaunchSpeed * launchPower *travelTime) -(0.5 * Gravity *Math.pow(travelTime, 2 ));
        if(heightAtTargetDistance < desiredHeight){
            shouldShoot = false;
        }
        else{
            shouldShoot = true;
        }
    }
    public void iteration(){
        double upperBound = initialUpperBound;
        double lowerBound = initalLowerBound;
        if(shouldShoot == true){
            while (heightAtTargetDistance != desiredHeight){
                if(heightAtTargetDistance > desiredHeight){
                    launchPower = launchPower - (Math.abs(upperBound -lowerBound)/2);
                    lowerBound = upperBound - (Math.abs(upperBound - lowerBound)/2);
                }
                    else if(heightAtTargetDistance < desiredHeight){
                        launchPower = launchPower + (Math.abs(upperBound - lowerBound)/2);
                        upperBound = upperBound + (Math.abs(upperBound - lowerBound)/2);
                    }
                else{
                    horizontalSpeed = Math.cos(radians) * maxLaunchSpeed * launchPower;
                travelTime = distanceToTarget/ horizontalSpeed;
                heightAtTargetDistance = launcherHeight + (Math.sin(radians)*maxLaunchSpeed*launchPower*travelTime)-(0.5 * Gravity * Math.pow(travelTime,2));
                }
            }
        }
        else{
        //Shot is not possible
        }
    }
}

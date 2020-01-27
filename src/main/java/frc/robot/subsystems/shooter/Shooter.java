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
    private final double wheelCircumference = 6*Math.PI;
    private final double maxTangentialSpeed = (2048* wheelCircumference)/60;
    private final double Gravity = 386.0886;
    private double launcherHeight; //make final 

    private double desiredHeight = 98;
    private double distanceToTarget;

    private double deadzone;
    private double upperBound;
    private double lowerBound;

    private double maxLaunchSpeed;
    private double horizontalSpeed;
    private double travelTime;
    private double launchPower;
    private double heightAtTargetDistance;
    private boolean shouldShoot;
    private double degrees;
    private double radians;

    private final CANSparkMax leftMotor = new CANSparkMax(leftMotorCanId, MotorType.kBrushless);
    private final CANSparkMax rightMotor = new CANSparkMax(rightMotorCanId,MotorType.kBrushless);

    public void setDegrees(double degrees){
        this.degrees = degrees;
    }

    public void ConvertDegreestoRadians(double degrees){
        this.radians = degrees * (Math.PI/180);
    }

    public void MaxLaunchSpeed(double energyLost)
    {
        this.maxLaunchSpeed = maxTangentialSpeed*(1 - energyLost);
    }

    public void horizontalSpeed(){
     horizontalSpeed = Math.cos(radians) * maxLaunchSpeed;
    }

    public void travelTime(){
        travelTime = distanceToTarget/horizontalSpeed;
    }

    public void determineHeightAtTargetDistance(){
        heightAtTargetDistance = launcherHeight + (Math.sin(radians)* maxLaunchSpeed * launchPower *travelTime) -(0.5 * Gravity *Math.pow(travelTime, 2 ));
        if(heightAtTargetDistance < desiredHeight - deadzone){
            shouldShoot = false;
        
        }
        else{
            shouldShoot = true;
        }
    }
    public void iteration(){
        if(shouldShoot == true){
            while (heightAtTargetDistance != desiredHeight){
             if(heightAtTargetDistance > desiredHeight){
            launchPower = launchPower - Math.abs(upperBound -lowerBound);
            lowerBound = upperBound - Math.abs(upperBound - lowerBound);
            }
            else if(heightAtTargetDistance < desiredHeight){
            launchPower = launchPower + Math.abs(upperBound - lowerBound);
            upperBound = upperBound + Math.abs(upperBound - lowerBound);
            }

            horizontalSpeed = Math.cos(radians) * maxLaunchSpeed * launchPower;
            travelTime = distanceToTarget/ horizontalSpeed;
            heightAtTargetDistance = launcherHeight + (Math.sin(radians)*maxLaunchSpeed*launchPower*travelTime)-(0.5 * Gravity * Math.pow(travelTime,2));
         }


        }
        else{

        }
    }


}

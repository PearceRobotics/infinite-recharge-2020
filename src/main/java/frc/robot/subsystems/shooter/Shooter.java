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
    private final double initialUpperBound = 1.0; //%100 speed
    private final double initialLowerBound = 0.0;//%0 speed
    private final double degrees = 58.0; 
    private final double radians = Math.toRadians(degrees);
    private double targetHeight = 88.0;
    private double maxLaunchSpeed = maxTangentialSpeed*(1 - energyLost);

    public double determineLaunchSpeed(double distanceToTarget){
        //declaration of % bounds
        double upperBound = initialUpperBound;
        double lowerBound = initialLowerBound;
        double launchPower = initialUpperBound;
        double heightAtTargetDistance;

        //Determine height at target distance
     heightAtTargetDistance= getHeightAtTargetDistance(distanceToTarget,launchPower);
   
        if(heightAtTargetDistance < targetHeight){
            return -1.0;
            //shot not possible at current distance
        }
        else if(heightAtTargetDistance == targetHeight){
            return maxLaunchSpeed*launchPower; // percent of max speed we should be going times the max speed 
        }
        //iteration
        else{
            for (int i = 0; i< 15; i++){//binary search...gets close enough to what we want
                if(heightAtTargetDistance > targetHeight){//if our current height is greater than our target height raise lowerbound
                    launchPower = launchPower - (Math.abs(upperBound -lowerBound)/2); //decreases launchPower
                    upperBound = upperBound - (Math.abs(upperBound - lowerBound)/2);//lowers upper bound to middle of upper and lower 
                }
                    else if(heightAtTargetDistance < targetHeight){
                        launchPower = launchPower + (Math.abs(upperBound - lowerBound)/2);//increases launchPower
                        lowerBound = lowerBound + (Math.abs(upperBound - lowerBound)/2);// raises lower bound to middle of upper and lower 
                    }
                else{
                 break;
                }
                heightAtTargetDistance = getHeightAtTargetDistance(distanceToTarget,launchPower);
            }
        }
        return launchPower * maxLaunchSpeed; 

        

    }
    private double getHeightAtTargetDistance(double distanceToTarget, double launchPower){
        double horizontalSpeed;
        double travelTime;
        //if you have a triangle, with the hypnotonuse being the angled velocity vector, the adjacent being the horizontal velocity vector, and the opposite side being the height
        //horizontal speed = adjacent side -> cos(angle) = a/h -> hcos(angle) = a -> (maxLaunchSpeed * launchPower)cos(angle) = a
        horizontalSpeed = Math.cos(radians) * maxLaunchSpeed * launchPower;
        //v = d/t -> vt=d -> t = d/v
        travelTime = distanceToTarget/horizontalSpeed;;
        //current distance = original distance +vt + .5at^2
        double heightAtTargetDistance = launcherHeight + (Math.sin(radians)* maxLaunchSpeed * launchPower *travelTime) -(0.5 * Gravity *Math.pow(travelTime, 2 ));
        return heightAtTargetDistance;
    }

   
}

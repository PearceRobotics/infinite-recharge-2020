/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.shooter;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Config.Configs;

/**
 * Add your docs here.
 */
public class Shooter {
    private final double wheelDiameter = 4; //inch
    private final double Gravity = 386.0886;
    private final double maxRpm = 7640.0; 
    private final double launcherHeight = 24.0; //inch
    private double energyLostBase = 0.58; //(0.58 works from initiation line) 
    private final double wheelCircumference = wheelDiameter*Math.PI;
    private final double maxTangentialSpeed = ( wheelCircumference * (maxRpm /60.0));
    private final double initialUpperBound = 1.0; //%100 speed
    private final double initialLowerBound = 0.0;//%0 speed
    private final double degrees = 58.0; 
    private final double radians = Math.toRadians(degrees);
    private double targetHeight = 98.0;

    private double maxLaunchSpeed = maxTangentialSpeed*(1.0 - energyLostBase);

    public Shooter()
    {
        //empty contructor
    }

    @Config
    public void setEnergyLost(double energyLostBase)
    {
        this.energyLostBase = energyLostBase;
    }

    public double determineLaunchSpeed(double distanceToTarget){
        //declaration of % bounds
        double range = initialUpperBound - initialLowerBound;
        double launchPower = initialUpperBound;
        double heightAtTargetDistance;

        //Determine height at target distance
     heightAtTargetDistance= getHeightAtTargetDistance(distanceToTarget,launchPower);
   
        if(heightAtTargetDistance < targetHeight){
            return -1.0;
            //shot not possible at current distance
        }
        else if(heightAtTargetDistance == targetHeight){
            return maxTangentialSpeed*launchPower; // percent of max speed we should be going times the max speed 
        }
        //iteration
        else{
            for (int i = 0; i< 15; i++){//binary search...gets close enough to what we want
                if(heightAtTargetDistance > targetHeight){//if our current height is greater than our target height raise lowerbound
                    launchPower = launchPower - (range/(2*Math.pow(2,i))); //decreases launchPower
                }
                    else if(heightAtTargetDistance < targetHeight){
                        launchPower = launchPower + (range/(2*Math.pow(2,i)));//increases launchPower
                    }
                else{
                 break;
                }
                heightAtTargetDistance = getHeightAtTargetDistance(distanceToTarget,launchPower);
            }
        }
        return launchPower * maxTangentialSpeed; 

        

    }
    private double getHeightAtTargetDistance(double distanceToTarget, double launchPower){
        double horizontalSpeed;
        double travelTime;

        double density = .0027 * 12.0 * 12.0 * 12.0;
        double area = 3.5 * 3.5 * Math.PI;
        double Cd = 0.5;
        double dragForce = 0.5 * density * (maxLaunchSpeed * launchPower) * (maxLaunchSpeed * launchPower) * area * Cd;

        //155 inches is 9977017 //speed 1122

        //184 inches is 9,244,452 //speed 1080

        System.out.println("dragForce " + dragForce);
        //if you have a triangle, with the hypnotonuse being the angled velocity vector, the adjacent being the horizontal velocity vector, and the opposite side being the height
        //horizontal speed = adjacent side -> cos(angle) = a/h -> hcos(angle) = a -> (maxLaunchSpeed * launchPower)cos(angle) = a
        horizontalSpeed = Math.sin(radians) * maxLaunchSpeed * launchPower;
        //v = d/t -> vt=d -> t = d/v
        travelTime = distanceToTarget/horizontalSpeed;;
        //current distance = original distance +vt + .5at^2
        double heightAtTargetDistance = launcherHeight + (Math.cos(radians)* maxLaunchSpeed * launchPower *travelTime) -(0.5 * Gravity *Math.pow(travelTime, 2 ));
        return heightAtTargetDistance;
    }

   
}

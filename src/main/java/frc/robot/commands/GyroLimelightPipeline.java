/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drive.Gyroscope;
import frc.robot.subsystems.vision.Limelight;

/**
 * Add your docs here.
 */
public class GyroLimelightPipeline extends SubsystemBase {

    private Gyroscope gyro;
    private Limelight limelight;

    public GyroLimelightPipeline(Gyroscope gyro, Limelight limelight){
        this.limelight = limelight;
        this.gyro = gyro;
    }

    public void gyroLimelightPipeline(){
        double side = gyro.getGyroAngle()%360;
        if(side <= 180){
            limelight.setHighGoalPipeline();
        }
        else{
            limelight.setLowGoalPipeline();
        }
    }
}

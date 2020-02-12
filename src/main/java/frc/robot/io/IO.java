/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.io;

import frc.robot.io.Controls;
import frc.robot.commands.GyroTurnCommand;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Gyroscope;

/**
 * Add your docs here.
 */
public class IO {


    public IO(Controls controls, Drive drive, Gyroscope gyro, double kP, double kI, double kD, double kF){

     //   controls.getLeftJoystickTrigger().whenPressed(new GyroTurnCommand(gyro, -180, drive, kP, kI, kD, kF));
       // controls.getRightJoystickTrigger().whenPressed(new GyroTurnCommand(gyro, 180, drive, kP, kI, kD, kF));
       // controls.getLeftJoystickBumper().whenPressed(new GyroTurnCommand(gyro, -90, drive, kP, kI, kD, kF));
         controls.getRightJoystickBumper().whenPressed(new GyroTurnCommand(gyro, 90, drive, kP, kI, kD));
    }
}

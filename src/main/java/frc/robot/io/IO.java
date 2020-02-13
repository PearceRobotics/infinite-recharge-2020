package frc.robot.io;

import frc.robot.io.Controls;
import frc.robot.commands.GyroTurnCommand;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Gyroscope;

public class IO {

    public IO(Controls controls, Drive drive, Gyroscope gyro, double kP, double kI, double kD, double kF){

      controls.getJoystickAButton().whenPressed(new GyroTurnCommand(gyro, -180, drive, kP, kI, kD));
      controls.getJoystickXButton().whenPressed(new GyroTurnCommand(gyro, 180, drive, kP, kI, kD));
      controls.getLeftJoystickBumper().whenPressed(new GyroTurnCommand(gyro, -90, drive, kP, kI, kD));
      controls.getRightJoystickBumper().whenPressed(new GyroTurnCommand(gyro, 90, drive, kP, kI, kD));
    }
}

package frc.robot.io;

import frc.robot.commands.GyroTurnCommand;
import frc.robot.commands.ReorientToFieldCommand;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Gyroscope;

public class IO {

  public IO(Controls controls, Drive drive, Gyroscope gyro){
    controls.getJoystickXButton().whenPressed(new GyroTurnCommand(gyro, drive, 180));
    controls.getRightJoystickBumper().whenPressed(new GyroTurnCommand(gyro, drive, -90));
    controls.getLeftJoystickBumper().whenPressed(new GyroTurnCommand(gyro, drive, 90));
    controls.getJoystickBButton().whenPressed(new ReorientToFieldCommand(gyro, drive));
  }
}
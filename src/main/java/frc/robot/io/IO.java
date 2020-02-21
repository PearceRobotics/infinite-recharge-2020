package frc.robot.io;

import frc.robot.commands.GyroTurnCommand;
import frc.robot.commands.ReorientToFieldCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.TurnToTopTargetCommand;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Gyroscope;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterSpeedController;
import frc.robot.subsystems.vision.Limelight;

public class IO {

  public IO(Controls controls, Drive drive, Gyroscope gyro, Shooter shooter, ShooterSpeedController shooterSpeedController,
      HopperController hopperController, IndexerController indexController,Limelight limelight) {
    controls.getJoystickXButton().whenPressed(new GyroTurnCommand(gyro, drive, 180));
    controls.getRightJoystickBumper().whenPressed(new GyroTurnCommand(gyro, drive, -90));
    controls.getLeftJoystickBumper().whenPressed(new GyroTurnCommand(gyro, drive, 90));
    controls.getJoystickBButton().whenPressed(new ReorientToFieldCommand(gyro, drive));
    controls.getJoystickAButton()
      .whenPressed(new ShooterCommand(shooter, shooterSpeedController, hopperController, indexController));
      controls.getJoystickYButton().whenPressed(new TurnToTopTargetCommand(drive, limelight));
  }
}

package frc.robot.operatorInputs;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.GyroLimelightPipeline;
import frc.robot.commands.IndexerOutakeCommand;
import frc.robot.commands.climbingCommands.ClimbingCommandGroup;
import frc.robot.commands.climbingCommands.ElevatorMidpointCommand;
import frc.robot.commands.climbingCommands.ElevatorUpCommand;
import frc.robot.commands.powerCellScoringCommands.PowerCellScoringCommandGroup;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Gyroscope;
import frc.robot.subsystems.lights.LightsController;
import frc.robot.subsystems.shooter.ShooterSpeedController;
import frc.robot.subsystems.vision.Limelight;

public class OperatorInputs {

  private final double JOYSTICK_DEADZONE = 0.1;
  public OperatorInputs(Controls controls, Drive drive, Gyroscope gyro, ShooterSpeedController shooterSpeedController,
      HopperController hopperController, IndexerController indexerController, 
      Limelight limelight, Climber climber, LightsController lightsController, GyroLimelightPipeline gyroLimelightPipeline) {
    //controls.getJoystickXButton().whenPressed(new GyroTurnCommand(gyro, drive, 180));
    //controls.getRightJoystickBumper().whenPressed(new GyroTurnCommand(gyro, drive, -90));
    //controls.getLeftJoystickBumper().whenPressed(new GyroTurnCommand(gyro, drive, 90));
    controls.getJoystickBButton().whenPressed(new IndexerOutakeCommand(indexerController, shooterSpeedController));
    controls.getJoystickAButton().whileHeld(new PowerCellScoringCommandGroup(drive, limelight, shooterSpeedController,
        hopperController, indexerController));
    controls.getJoystickYButton().whenPressed(new ElevatorMidpointCommand(climber));
    controls.getLeftStick().whenPressed(new ClimbingCommandGroup(climber));
    drive.setDefaultCommand(new RunCommand(() -> {
      drive.curvatureDrive(controls.getLeftY(JOYSTICK_DEADZONE), controls.getRightX(JOYSTICK_DEADZONE));
    }, drive));
    lightsController.setDefaultCommand(new RunCommand(() -> lightsController.checkTargetLock(), lightsController));
    gyroLimelightPipeline.setDefaultCommand(new RunCommand(() -> gyroLimelightPipeline.gyroLimelightPipeline(), gyroLimelightPipeline));
  }
}
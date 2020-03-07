package frc.robot.operatorInputs;

import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.commands.IndexerOutakeCommand;
import frc.robot.commands.climbingCommands.ClimbingCommandGroup;
import frc.robot.commands.climbingCommands.ElevatorMidpointCommand;
import frc.robot.commands.climbingCommands.ElevatorUpCommand;
import frc.robot.commands.climbingCommands.WinchCommand;
import frc.robot.commands.climbingCommands.ElevatorDownCommand;
import frc.robot.commands.powerCellScoringCommands.PowerCellScoringCommandGroup;
import frc.robot.commands.powerCellScoringCommands.ShooterCommandNoAim;
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

  public OperatorInputs(Controls driverControls, Controls operatorControls, Drive drive, Gyroscope gyro,
      ShooterSpeedController shooterSpeedController, HopperController hopperController,
      IndexerController indexerController, Limelight limelight, Climber climber, LightsController lightsController) {

    // Driver commands
    driverControls.getJoystickBButton().whileHeld(new IndexerOutakeCommand(indexerController));
    driverControls.getJoystickAButton().whileHeld(new PowerCellScoringCommandGroup(drive, limelight,
        shooterSpeedController, hopperController, indexerController));

    driverControls.getJoystickYButton()
        .whileHeld(new ShooterCommandNoAim(shooterSpeedController, hopperController, indexerController, limelight));

    // operator commands
    operatorControls.getJoystickYButton().whenPressed(new ElevatorMidpointCommand(climber));
    operatorControls.getJoystickXButton().whenPressed(new ClimbingCommandGroup(climber));
    operatorControls.getLeftJoystickBumper().whenPressed(new ElevatorDownCommand(climber));
    operatorControls.getRightJoystickBumper().whenPressed(new ElevatorUpCommand(climber));
    operatorControls.getLeftStick().whenPressed(new WinchCommand(climber));

    // Always running commands
    drive.setDefaultCommand(new RunCommand(() -> {
      drive.curvatureDrive(driverControls.getLeftY(JOYSTICK_DEADZONE), driverControls.getRightX(JOYSTICK_DEADZONE));
    }, drive));
    lightsController.setDefaultCommand(new RunCommand(() -> lightsController.checkTargetLock(), lightsController));
  }
}
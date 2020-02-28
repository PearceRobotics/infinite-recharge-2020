package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.ShooterSpeedController;
import frc.robot.subsystems.vision.Limelight;

/**
 * A complex scoring command that drives forward, aims the robot to the target
 * then Launches up to five power cells at the inner zone
 */
public class PowerCellScoringCommandGroup extends SequentialCommandGroup {

    public PowerCellScoringCommandGroup(Drive drive, Limelight limelight, ShooterSpeedController shooterSpeedController,
            HopperController hopperController, IndexerController indexerController) {
        addCommands(new TurnToTopTargetCommand(drive, limelight),
                new ShooterCommand(shooterSpeedController, hopperController, indexerController));
        addRequirements(drive);
    }
}
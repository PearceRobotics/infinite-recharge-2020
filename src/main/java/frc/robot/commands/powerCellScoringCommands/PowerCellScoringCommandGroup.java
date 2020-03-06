package frc.robot.commands.powerCellScoringCommands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.ShooterSpeedController;

/**
 * A complex scoring command that drives forward, aims the robot to the target
 * then Launches up to five power cells at the inner zone
 */
public class PowerCellScoringCommandGroup extends ParallelCommandGroup {

    public PowerCellScoringCommandGroup(Drive drive, Limelight limelight, ShooterSpeedController shooterSpeedController,
            HopperController hopperController, IndexerController indexerController) {
        addCommands(new AimCommand(drive, limelight),
                new ShooterCommand(shooterSpeedController, hopperController, indexerController, limelight));
        addRequirements(drive);
    }
}
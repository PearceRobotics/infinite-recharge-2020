/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.powerCellScoringCommands.TurnToTopTargetCommand;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.subsystems.shooter.ShooterSpeedController;

/**
 * Add your docs here.
 */
public class AutonomousCommandGroup extends SequentialCommandGroup {

    public AutonomousCommandGroup(Drive drive, ShooterSpeedController shooterSpeedController, HopperController hopperController, IndexerController indexerController, Limelight limelight, double distance, double maxSpeed){
        addCommands( new TurnToTopTargetCommand(drive, limelight),
                new AutonomousShooterCommand(drive, shooterSpeedController, hopperController, indexerController),
                new DriveForwardCommand(distance, maxSpeed, drive));

        addRequirements(drive);
    }
}
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.ShooterSpeedController;
import frc.robot.subsystems.shooter.ShooterMath;
import edu.wpi.first.wpilibj.Timer;

/**
 * Add your docs here.
 */
public class AutonomousShooterCommand extends CommandBase {

    private ShooterSpeedController shooterSpeedController;
    private HopperController hopperController;
    private IndexerController indexerController;

    // variables
    private double commandStartTime;
    private double distanceToGoal = 165.0;

    // constants
    private final double INNER_DISTANCE_FROM_TARGET = 29.0;
    private final double COMMAND_RUN_LIMIT = 8.0;

    public AutonomousShooterCommand(Drive drive, ShooterSpeedController shooterSpeedController,
            HopperController hopperController, IndexerController indexerController) {
        this.indexerController = indexerController;
        this.shooterSpeedController = shooterSpeedController;
        this.hopperController = hopperController;
        this.commandStartTime = Timer.getFPGATimestamp();
    }

    @Override
    public void initialize() {
        shooterSpeedController
                .setLaunchSpeed(ShooterMath.determineLaunchSpeed(distanceToGoal + INNER_DISTANCE_FROM_TARGET));
        commandStartTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        if (shooterSpeedController.isAtSpeed()) {// if the shooter is at speed and has given time for last ball to shoot
            indexerController.intake();
            hopperController.start();
        } else {
            indexerController.stop();
            hopperController.stop();
        }
    }

    @Override
    public boolean isFinished() {
        return ((Timer.getFPGATimestamp() - commandStartTime) > COMMAND_RUN_LIMIT);
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        shooterSpeedController.setLaunchSpeed(0.0);
        indexerController.stop();
        hopperController.stop();
    }
}
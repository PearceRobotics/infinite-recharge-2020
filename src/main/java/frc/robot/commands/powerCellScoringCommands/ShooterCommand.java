/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 1745 JJ Pearce Robotics. All Rights Reserved.           */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.powerCellScoringCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.shooter.ShooterMath;
import frc.robot.subsystems.shooter.ShooterSpeedController;
import frc.robot.subsystems.vision.DistanceCalculator;
import frc.robot.subsystems.vision.Limelight;
import io.github.oblarg.oblog.annotations.Config;

public class ShooterCommand extends CommandBase {

    private ShooterSpeedController shooterSpeedController;
    private HopperController hopperController;
    private IndexerController indexerController;
    private Limelight limelight;
    private boolean shooterChoice = true;
    public final double TOP_GOAL_DEADBAND = 2.0;

    private final double INNER_DISTANCE_FROM_TARGET = 29.0;

    private static final double CAMERA_DISTANCE_FROM_LAUNCHER = 8.0;

    private double fixedGoalDistance = 149.0;

    // Constructor.
    public ShooterCommand(ShooterSpeedController shooterSpeedController, HopperController hopperController,
            IndexerController indexerController, Limelight limelight) {
        this.shooterSpeedController = shooterSpeedController;
        this.hopperController = hopperController;
        this.indexerController = indexerController;
        this.limelight = limelight;

        addRequirements(shooterSpeedController);
    }

    public void setShooterChoice(boolean shooterChoice) {
        this.shooterChoice = shooterChoice;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        // nothing to be initialized at this time
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        double distance = fixedGoalDistance;
        if (shooterChoice) {
            double targetAngleRadians = Math.toRadians(limelight.getVerticalTargetOffset());
            distance = DistanceCalculator.getDistanceFromTarget(targetAngleRadians);
        }

        shooterSpeedController.setLaunchSpeed(ShooterMath
                .determineLaunchSpeed(distance + INNER_DISTANCE_FROM_TARGET - CAMERA_DISTANCE_FROM_LAUNCHER));

        double offset = limelight.getHorizontalTargetOffset();
        // Make sure the shooter is at speed before loading a power cell
        if (shooterSpeedController.isAtSpeed() && Math.abs(offset) <= TOP_GOAL_DEADBAND) {
            // turn on the indexer and hopper
            this.indexerController.intake();
            this.hopperController.start();
        }

        // turn off the indexer and hopper
        else {
            // turn off the indexer and hopper
            indexerController.stop();
            hopperController.stop();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        // TODO this will need to test if a power cell has been launched.
        // Need a power cell presence sensor of some type for that.
        return false;
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        indexerController.stop();
        hopperController.stop();
        shooterSpeedController.setLaunchSpeed(0.0);
    }

    // Use this for the speed finding algorithm
    @Config
    public void setDistanceToGoal(double distanceToGoal) {
        this.fixedGoalDistance = distanceToGoal;
    }
}

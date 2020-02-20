/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 1745 JJ Pearce Robotics. All Rights Reserved.           */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterSpeedController;
import io.github.oblarg.oblog.annotations.Config;

public class ShooterCommand extends CommandBase {

    private Shooter shooter;
    private ShooterSpeedController shooterSpeedController;
    private HopperController hopperController;
    private IndexerController indexerController;

    private final double BALL_SHOOT_TIME = 1.0;
    private final double INNER_DISTANCE_FROM_TARGET = 29.0;

    private double distanceToGoal = 123.0; // TODO set back to 0 when distance is working
    private double ballShootStartTime;
    private boolean shotRequested = false;
    private boolean shotInProgress = false;

    // Default Constructor. Set initial launch speed to 0 inches/second.
    public ShooterCommand(Shooter shooter, ShooterSpeedController shooterSpeedController,
            HopperController hopperController, IndexerController indexerController) {
        this.shooter = shooter;
        this.shooterSpeedController = shooterSpeedController;
        this.hopperController = hopperController;
        this.indexerController = indexerController;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        // call shooter.determineLaunchSpeed
        // use it to set the shooterSpeedController
        // TODO Limelight might take inner distance to account, revisit this
        shooterSpeedController
                .setLaunchSpeed(shooter.determineLaunchSpeed(distanceToGoal + INNER_DISTANCE_FROM_TARGET));

        // Set the bool to know that a shot is requested
        shotRequested = true;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {

        // Make sure the shooter is at speed before loading a power cell
        if (shooterSpeedController.isAtSpeed() && !shotInProgress) {
            // record the start time so we can turn off the indexer after a period of time
            ballShootStartTime = Timer.getFPGATimestamp();

            // Set the bool to know that the indexer is spinning
            shotInProgress = true;

            // turn on the indexer and hopper
            this.indexerController.intake();
            this.hopperController.start();
        }

        // turn off the indexer after a set amount of time
        if (shotInProgress && ((Timer.getFPGATimestamp() - ballShootStartTime) > BALL_SHOOT_TIME)) {
            indexerController.stop();
            hopperController.stop();

            shotInProgress = false;
            shotRequested = false;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return (shotRequested && shotInProgress);
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        // do nothing right now, might want something later on.
    }

    // Use this for the speed finding algorithm
    @Config
    public void setDistanceToGoal(double distanceToGoal) {
        this.distanceToGoal = distanceToGoal;
        // TODO Limelight might take inner distance to account, revisit this
        shooterSpeedController
                .setLaunchSpeed(shooter.determineLaunchSpeed(distanceToGoal + INNER_DISTANCE_FROM_TARGET));
    }
}
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 1745 JJ Pearce Robotics. All Rights Reserved.           */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

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
    private boolean shooterChoice;
    

    private final double INNER_DISTANCE_FROM_TARGET = 29.0;

    private static final double CAMERA_DISTANCE_FROM_LAUNCHER = 8.0;

    private double distanceToGoal = 149.0; // TODO set back to 0 when distance is working

    // Constructor.
    public ShooterCommand(ShooterSpeedController shooterSpeedController, HopperController hopperController,
            IndexerController indexerController, Limelight limelight) {
        this.shooterSpeedController = shooterSpeedController;
        this.hopperController = hopperController;
        this.indexerController = indexerController;
        this.limelight = limelight;
    }

    public void setShooterChoice(boolean shooterChoice){
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
        // TODO Change distanceToGoal to be a call to the limelight.
        // TODO Limelight might take inner distance into account, revisit this

        // 4.2 129
        //4.5 132
        //angle 2.04 distance 163
        // -2.4 240
        System.out.println("vertical offset degrees " + limelight.getVerticalTargetOffset());

        double targetAngleRadians = Math.toRadians(limelight.getVerticalTargetOffset());
        distanceToGoal = DistanceCalculator.getDistanceFromTarget(targetAngleRadians);

        System.out.println("Distance to target " + distanceToGoal);

        shooterSpeedController.setLaunchSpeed(ShooterMath
                .determineLaunchSpeed(distanceToGoal + INNER_DISTANCE_FROM_TARGET - CAMERA_DISTANCE_FROM_LAUNCHER));

        // Make sure the shooter is at speed before loading a power cell
        if (shooterSpeedController.isAtSpeed()) {
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
        this.distanceToGoal = distanceToGoal;
    }
}

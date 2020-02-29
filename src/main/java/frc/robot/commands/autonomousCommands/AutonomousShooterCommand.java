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

/**
 * Add your docs here.
 */
public class AutonomousShooterCommand extends CommandBase{

    private Drive drive;
    private ShooterSpeedController shooterSpeedController;
    private HopperController hopperController;
    private IndexerController indexerController;
    //variables
    private double ballsShot = 0;
    private double distanceToGoal = 149.0; 
    //constants
    private final double INNER_DISTANCE_FROM_TARGET = 29.0;

    public AutonomousShooterCommand(Drive drive, ShooterSpeedController shooterSpeedController,
    HopperController hopperController, IndexerController indexerController){
        this.drive = drive;
        this.shooterSpeedController = shooterSpeedController;
        this.hopperController = hopperController;
        this.indexerController = indexerController;

        addRequirements(drive);
    }

    @Override
    public void initialize(){

    }

    @Override
    public void execute(){
        shooterSpeedController.setLaunchSpeed(ShooterMath.determineLaunchSpeed(distanceToGoal + INNER_DISTANCE_FROM_TARGET));

        for(int ballsShot = 0 ; ballsShot < 3 ; ballsShot ++){
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
    }

    @Override
    public boolean isFinished() {
        if(ballsShot == 5) {
            return true;
        }
        return false;
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {

    }
}
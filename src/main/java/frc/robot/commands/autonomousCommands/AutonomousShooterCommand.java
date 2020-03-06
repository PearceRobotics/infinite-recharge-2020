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
import frc.robot.subsystems.DistanceSensorDetector;
import frc.robot.commands.IndexerIntakeCommand;

/**
 * Add your docs here.
 */
public class AutonomousShooterCommand extends CommandBase{

    private Drive drive;
    private ShooterSpeedController shooterSpeedController;
    private HopperController hopperController;
    private IndexerController indexerController;
    private DistanceSensorDetector distanceSensor;

    //variables
    private double ballsShot = 0;
    private double startTime = 0;
    private double commandStartTime;
    private boolean ballinIndexer = false;
    private double distanceToGoal = 149.0; 

    //constants
    private final double INNER_DISTANCE_FROM_TARGET = 29.0;
    private final double LAUNCH_TIME = 0.5;
    private final double AUTONOMOUS_SPEED = 0.6;
    private final double COMMAND_RUN_LIMIT = 8.0;

    public AutonomousShooterCommand(Drive drive, ShooterSpeedController shooterSpeedController,
    HopperController hopperController, IndexerController indexerController){
        this.drive = drive;
        this.indexerController = indexerController;
        this.shooterSpeedController = shooterSpeedController;
        this.hopperController = hopperController;
        this.commandStartTime = Timer.getFPGATimestamp();

        addRequirements(drive);
    }

    @Override
    public void initialize(){
    }

    @Override
    public void execute(){
        shooterSpeedController.setLaunchSpeed(ShooterMath.determineLaunchSpeed(distanceToGoal + INNER_DISTANCE_FROM_TARGET));

        if(shooterSpeedController.isAtSpeed() && Timer.getFPGATimestamp() - startTime > LAUNCH_TIME ){// if the shooter is at speed and has given time for last ball to shoot
            indexerController.intake();
            hopperController.start();
        }
            if(distanceSensor.isPowerCellLoaded() && !ballinIndexer){
                hopperController.stop();
                ballinIndexer = true;
            }
            if(!distanceSensor.isPowerCellLoaded() && ballinIndexer){ //if ball is no longer visible first time
                startTime = Timer.getFPGATimestamp();
                ballsShot++;
                indexerController.stop();
                ballinIndexer = false;
            }
    }

    @Override
    public boolean isFinished() {
        if(ballsShot >= 3 || Timer.getFPGATimestamp() - commandStartTime > COMMAND_RUN_LIMIT) {
            return true;
        }
        return false;
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        indexerController.stop();
        hopperController.stop();
    }
}
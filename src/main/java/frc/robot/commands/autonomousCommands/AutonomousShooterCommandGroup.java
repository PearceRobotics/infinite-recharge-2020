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
public class AutonomousShooterCommandGroup extends CommandBase {

    private Drive drive;
    private ShooterSpeedController shooterSpeedController;
    private HopperController hopperController;
    private IndexerController indexerController;
    // private DistanceSensorDetector distanceSensor;

    // variables
    private double ballsShot = 0;
    private double startTime = 0;
    private double commandStartTime;
    private boolean ballinIndexer = false;
    private double distanceToGoal = 165.0;

    // constants
    private final double INNER_DISTANCE_FROM_TARGET = 29.0;
    private final double LAUNCH_TIME = 0.5;
    private final double AUTONOMOUS_SPEED = 0.6;
    private final double COMMAND_RUN_LIMIT = 8.0;

    public AutonomousShooterCommandGroup(Drive drive, ShooterSpeedController shooterSpeedController,
            HopperController hopperController, IndexerController indexerController) {

        addRequirements(drive);
    }

}
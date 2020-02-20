/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Limelight;

/**
 * Add your docs here.
 */
public class TurnToTopTargetCommand extends CommandBase {
    private Drive drive;
    private Limelight limelight;

    public TurnToTopTargetCommand(Drive drive,Limelight limelight) {
        this.drive = drive;
        this.limelight = limelight;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        double steeringAdjust = 0;
        final double KpAIM = 0.005;

        if (limelight.hasValidTarget()) {
            if (Math.abs(limelight.getHorizontalTargetOffset()) > Constants.TOP_GOAL_DEADBAND) {
                steeringAdjust = KpAIM * limelight.getHorizontalTargetOffset();
                drive.arcadeDrive(0, steeringAdjust);
            }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if (limelight.getHorizontalTargetOffset() <= Constants.TOP_GOAL_DEADBAND) {
            return true;
        }
        return false;
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {

    }
}

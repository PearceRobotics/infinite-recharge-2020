/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.operatorInputs.Controls;
import frc.robot.subsystems.drive.Drive;

/**
 * Add your docs here.
 */
public class NotStraightArcadeDriveCommand extends CommandBase{

    //classes
    private Drive drive;
    private Controls controls;

    //constants
    private final double DEADZONE = 0.2;
  
    public NotStraightArcadeDriveCommand(Controls controls, Drive drive) {
        this.controls = controls;
        this.drive = drive;

        addRequirements(drive);
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        drive.arcadeDrive(controls.getLeftY(DEADZONE), controls.getRightX(DEADZONE));
    }
}

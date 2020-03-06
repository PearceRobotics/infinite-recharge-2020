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
public class CurvatureDriveCommand extends CommandBase {

    private Drive drive;
    private Controls controls;
    //constants
    private final double JOYSTICK_DEADZONE = .15;

    public CurvatureDriveCommand(Drive drive, Controls controls){
        this.drive = drive;
    }

    @Override
    public void initialize(){

    }

    @Override
    public void execute(){
        drive.curvatureDrive(controls.getLeftY(JOYSTICK_DEADZONE), controls.getRightX(JOYSTICK_DEADZONE));
    }

}

/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.io;

import frc.robot.io.IO;


import frc.robot.commands.TeleopCommand;
/**
 * Add your docs here.
 */
public class IO {

private Controls controls;

    public IO(Controls controls,TeleopCommand teleopCommand, double DEADZONE) {
        this.controls = controls;

        if(controls.getLeftY(DEADZONE) > 0){
            teleopCommand.start();
        }
        if(controls.getRightX(DEADZONE) > 0){
            teleopCommand.start();
        }
   
   
    }
}


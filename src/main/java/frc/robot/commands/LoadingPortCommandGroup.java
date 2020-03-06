/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.operatorInputs.Controls;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Limelight;

/**
 * Add your docs here.
 */
public class LoadingPortCommandGroup extends ParallelCommandGroup{

    public LoadingPortCommandGroup(Drive drive, Limelight limelight, Controls controls) {
        addCommands(new AimCommand(drive, limelight),
                new curvatureDrive(drive, controls));
    }
}
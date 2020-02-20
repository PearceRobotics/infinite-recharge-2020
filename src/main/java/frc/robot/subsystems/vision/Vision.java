/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.vision;

import frc.robot.subsystems.drive.DrivingDeltas;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.Constants;
import frc.robot.subsystems.Lights;

/**
 * Add your docs here.
 */
public class Vision {
    private Limelight limelight;
    private Lights lights;

    public Vision(Limelight limelight, Lights lights){
        this.limelight = limelight;
        this.lights = lights;
    }
    public DrivingDeltas targetDelta() {
        if(limelight.hasValidTarget()) {
            lights.allLimeGreen();
        } else {
            lights.allBlue();
        }
        
        return limelight.calculateDeltas(Constants.TOP_GOAL_DEADBAND);
    }
}

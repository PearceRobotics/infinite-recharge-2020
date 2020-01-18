/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drive;

/**
 * Add your docs here.
 */
public class DrivingDeltas {
    private double forwardPower = 0;
    private double steeringPower = 0;

    public DrivingDeltas(double forwardPower, double steeringPower) {
        this.forwardPower = forwardPower;
        this.steeringPower = steeringPower;
    }

    public double getForwardPower() {
        return this.forwardPower;
    }

    public void setForwardPower(double forwardPower) {
        this.forwardPower = forwardPower;
    }

    public double getSteeringPower() {
        return this.steeringPower;
    }

    public void setSteeringPower(double steeringPower) {
        this.steeringPower = steeringPower;
    }
}

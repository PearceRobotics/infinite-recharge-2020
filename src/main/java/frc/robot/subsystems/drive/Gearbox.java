/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANError;
/**
 * Add your docs here.
 */
public class Gearbox {
    private final CANSparkMax frontContoller;
    private final CANSparkMax backController;
    private final CANSparkMax middleController;


    public Gearbox(final CANSparkMax backController, final CANSparkMax middleController,
            final CANSparkMax frontController) {
        this.backController = backController;
        this.middleController = middleController;
        this.frontContoller = frontController;
        this.setLeaderToFront();
    }

    private void setLeaderToFront() {
        /**
         * Slave controllers_only_mirror the voltage, no other settings
         */
        final CANError backSlave = this.backController.follow(this.frontContoller);
        final CANError middleSlave = this.middleController.follow(this.frontContoller);
        if (backSlave != CANError.kOk || middleSlave != CANError.kOk) {
            throw new IllegalStateException("Unsuccessful in setting leader, BackSlave error status: "
                    + backSlave.name() + " MiddleSlave Error status: " + middleSlave.name());
        }
    }

    public void setSpeed(double rate) {
        if (rate < -1.0) {
            rate = -1.0;
        } else if (rate > 1.0) {
            rate = 1.0;
        }
        this.frontContoller.set(rate);
    }

    public void setRampRate(final double rate) {
        this.frontContoller.setOpenLoopRampRate(rate);   
    }

    public CANSparkMax getBackController() {
        return this.backController;
        }
    
    public CANSparkMax getMiddleController() {
        return this.middleController;
        }
    public CANSparkMax getFrontController() {
        return this.frontContoller;
        }
}
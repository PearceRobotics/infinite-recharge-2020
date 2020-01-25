/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax.IdleMode;
/**
 * Add your docs here.
 */
public class Gearbox {
    private final CANSparkMax frontController;
    private final CANSparkMax backController;
    private final CANSparkMax middleController;



    public Gearbox (final CANSparkMax frontController, final CANSparkMax middleController, final CANSparkMax backController) {
        this.backController = backController;
        this.middleController = middleController;
        this.frontController = frontController;
        this.setLeaderToFront();
    }

    private void setLeaderToFront() {
        /**
         * Slave controllers_only_mirror the voltage, no other settings
         */
        final CANError backSlave = this.backController.follow(this.frontController);
        final CANError middleSlave = this.middleController.follow(this.frontController);
        if (backSlave != CANError.kOk || middleSlave != CANError.kOk) {
            throw new IllegalStateException("Unsuccessful in setting leader, BackSlave error status: "
                    + backSlave.name() +  " MiddleSlave Error status: " + middleSlave.name());
        }
    }

    public void setSpeed(double rate) {
        if (rate < -1.0) {
            rate = -1.0;
        } else if (rate > 1.0) {
            rate = 1.0;
        }
        this.frontController.set(rate);
    }

    public void setRampRate(final double rate) {
        this.frontController.setOpenLoopRampRate(rate);   
    }

    public CANSparkMax getBackController() {
       return this.backController;
        }
    
    public CANSparkMax getMiddleController() {
        return this.middleController;
        }
    public CANSparkMax getFrontController() {
        return this.frontController;
        }
    public void setBrakeMode() {
        this.frontController.setIdleMode(IdleMode.kBrake);
    }
    public void setCoastMode() {
        this.frontController.setIdleMode(IdleMode.kCoast);
    }
}

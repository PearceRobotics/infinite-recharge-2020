package frc.robot.subsystems.drive;

import java.util.Arrays;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class Gearbox {
    private CANSparkMax[] controllers;
   /* private CANSparkMax canSparkMaxFront;
    private CANSparkMax canSparkMaxMiddle;
    private CANSparkMax canSparkMaxBack;*/

    private SpeedControllerGroup speedControllerGroup;

    public Gearbox(CANSparkMax controller, CANSparkMax... controllers /*CANSparkMax canSparkMaxFront,  CANSparkMax canSparkMaxMiddle, CANSparkMax canSparkMaxBack*/) {
        this.controllers = controllers;
        this.speedControllerGroup = new SpeedControllerGroup(controller, controllers);
        this.controllers = Arrays.copyOf(controllers, controllers.length + 1);
        this.controllers[this.controllers.length-1] = controller;
        /*this.canSparkMaxFront = canSparkMaxFront;
        this.canSparkMaxMiddle = canSparkMaxMiddle;
        this.canSparkMaxBack = canSparkMaxBack;*/
        
       // this.speedControllerGroup = new SpeedControllerGroup(this.canSparkMaxFront, this.canSparkMaxMiddle, this.canSparkMaxBack);
    }

    public void setSpeed(double rate) {
        if (rate < -1.0) {
            rate = -1.0;
        } else if (rate > 1.0) {
            rate = 1.0;
        }
        this.speedControllerGroup.set(rate);
    }

    public SpeedControllerGroup getSpeedControllerGroup() {
        return this.speedControllerGroup;
    }

    public void setRampRate(final double rate) {
        for(CANSparkMax controller: this.controllers) {
            controller.setOpenLoopRampRate(rate);
        }
        /*canSparkMaxFront.setOpenLoopRampRate(rate);
        canSparkMaxMiddle.setOpenLoopRampRate(rate);        
        canSparkMaxMiddle.setOpenLoopRampRate(rate); */    
    }

    public void setBrakeMode() {
        for(CANSparkMax controller: this.controllers) {
            controller.setIdleMode(IdleMode.kBrake);
        }
        /*canSparkMaxFront.setIdleMode(IdleMode.kBrake);
        canSparkMaxMiddle.setIdleMode(IdleMode.kBrake);      
        canSparkMaxMiddle.setIdleMode(IdleMode.kBrake);*/
    }

    public void setCoastMode() {
        for(CANSparkMax controller: this.controllers) {
            controller.setIdleMode(IdleMode.kCoast);
        }
        /*canSparkMaxFront.setIdleMode(IdleMode.kCoast);
        canSparkMaxMiddle.setIdleMode(IdleMode.kCoast);      
        canSparkMaxMiddle.setIdleMode(IdleMode.kCoast);*/
    }
}
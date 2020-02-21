package frc.robot.subsystems;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import io.github.oblarg.oblog.annotations.Config;

public class HopperController {

    private final int LEFT_CAN_ID = 9;
    private final int RIGHT_CAN_ID = 10;

    private CANSparkMax leftController;
    private CANSparkMax rightController;

    private double speed = 0.3;

    public HopperController() {
        this.leftController = new CANSparkMax(LEFT_CAN_ID, MotorType.kBrushless);
        this.rightController = new CANSparkMax(RIGHT_CAN_ID, MotorType.kBrushless);
        this.setMasterSlave();
    }

    public void setMasterSlave() {
        final CANError slave = this.rightController.follow(this.leftController);

        if (slave != CANError.kOk) {
            throw new IllegalStateException(
                    "Unsuccessful in setting hopper leader, error status: " + slave.name());
        }
    }

    public void start() {
        this.leftController.set(speed);
    }

    public void stop() {
        this.leftController.set(0.0);
    }

    @Config
    public void setHopperSpeed(double speed){
        this.speed = speed;
        this.start();
    }
}
package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import io.github.oblarg.oblog.annotations.Config;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IndexerController extends SubsystemBase{
    //Constants
    private final int CAN_ID = 15;
    private final int SPARK_550_MAXAMPS = 30;

    private CANSparkMax controller;

    private double speed = 0.5;

    public IndexerController() {
        this.controller = new CANSparkMax(CAN_ID, MotorType.kBrushless);
        controller.setSmartCurrentLimit(SPARK_550_MAXAMPS);
    }

    public void intake() {
        this.controller.set(speed);
    }

    public void stop() {
        this.controller.set(0.0);
    }

    public void outtake() {
        this.controller.set(-speed);
    }

    @Config
    public void setSpeed(double speed){
        this.speed = speed;
        this.intake();
    }

}
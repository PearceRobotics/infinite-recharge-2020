/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;

import com.revrobotics.CANSparkMax.IdleMode;

/**
 * Add your docs here.
 */
public class Climber extends SubsystemBase {

    private CANSparkMax winchController;
    private CANSparkMax elevatorController;
    private Encoder elevatorEncoder;
    private AnalogPotentiometer climbingFlexSensor;
    private PIDController elevatorPIDController;

    private final int WINCH_CAN_ID = 9;
    private final int ELEVATOR_CAN_ID = 15;
    private final int CLIMBING_FLEX_SENSOR_PORT = 0;
    private final double Kp = 0.15;
    private final double Ki = 0.0;
    private final double Kd = 0.0;
    private final double TOLERANCE = 0.5; // in pulses? TODO
    private final double SLOWING_CONSTANT = -0.35;
    private final double SPROCKET_DIAMETER = 1.273;

    private final double MIDPOINT_POSITION = 12.0;
    private final double UP_POSITION = 17.0;
    private final double DOWN_POSITION = 3.0;

    private final int SPARK_550_MAXAMPS = 30;

    private MotorType CLIMBING_MOTOR_TYPE = MotorType.kBrushless;

    public Climber() {
        // this.winchController = new CANSparkMax(WINCH_CAN_ID, CLIMBING_MOTOR_TYPE);
        this.elevatorController = new CANSparkMax(ELEVATOR_CAN_ID, CLIMBING_MOTOR_TYPE);
        this.climbingFlexSensor = new AnalogPotentiometer(CLIMBING_FLEX_SENSOR_PORT, 180, 90);

        elevatorController.setIdleMode(IdleMode.kBrake);
        this.elevatorEncoder = new Encoder(4, 5);
        this.elevatorEncoder.setReverseDirection(true);

        this.elevatorEncoder.setDistancePerPulse((SPROCKET_DIAMETER * Math.PI) / 2048.0);

        this.elevatorEncoder.reset();

        elevatorPIDController = new PIDController(Kp, Ki, Kd);
        elevatorController.setSmartCurrentLimit(SPARK_550_MAXAMPS);
        setElevatorPIDTolerance();
    }

    public void gotoElevatorMidpoint() {
        setElevatorPIDSetpoint(MIDPOINT_POSITION);

    }

    public void gotoElevatorUppoint() {
        setElevatorPIDSetpoint(UP_POSITION);
    }

    public void gotoElevatorDownpoint() {
        setElevatorPIDSetpoint(DOWN_POSITION);
    }

    @Override
    public void periodic() {
        double speed = SLOWING_CONSTANT * elevatorPIDController.calculate(elevatorEncoder.getDistance());

        if (Math.abs(speed) < 0.15) {
            speed = speed * 5.0;
        }

        speed = Math.min(0.15, speed);
        setElevatorSpeed(speed);

    }

    public void setElevatorPIDSetpoint(double position) {
        elevatorPIDController.setSetpoint(position);
    }

    public double getElevatorPIDSetpoint() {
        return this.elevatorPIDController.getSetpoint();
    }

    public boolean isElevatorAtSetPoint() {
        return this.elevatorPIDController.atSetpoint();
    }

    public boolean isElevatorAtDistance() {
        return Math.abs(this.elevatorEncoder.getDistance() - this.elevatorPIDController.getSetpoint()) < TOLERANCE;
    }

    public void setElevatorPIDTolerance() {
        elevatorPIDController.setTolerance(TOLERANCE);
    }

    public void setElevatorSpeed(double speed) {
        elevatorController.set(speed);
    }

    public void setWinchSpeed(double speed) {
        winchController.set(speed);
    }

    public void getFlexSensorPosition() {
        double flexSensorPosition = climbingFlexSensor.get();
    }
}
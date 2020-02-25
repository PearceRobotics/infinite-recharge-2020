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
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Add your docs here.
 */
public class Climber extends SubsystemBase{

    private CANSparkMax winchController;
    private CANSparkMax elevatorController;
    CANEncoder elevatorEncoder = new CANEncoder(elevatorController);
    private AnalogPotentiometer climbingFlexSensor;
    private PIDController climbPIDController;

    private final int WINCH_CAN_ID = 12;
    private final int ELEVATOR_CAN_ID = 13;
    private final int CLIMBING_FLEX_SENSOR_PORT = 1;
    private final double Kp = 0.5;
    private final double Ki = 0.0;
    private final double Kd = 0.0;
    private final double TOLERANCE = 10;
    
    private MotorType CLIMBING_MOTOR_TYPE = MotorType.kBrushless;

    public Climber() {
        this.winchController = new CANSparkMax(WINCH_CAN_ID, CLIMBING_MOTOR_TYPE);
        this.elevatorController = new CANSparkMax(ELEVATOR_CAN_ID, CLIMBING_MOTOR_TYPE);
        this.climbingFlexSensor = new AnalogPotentiometer(CLIMBING_FLEX_SENSOR_PORT);

        climbPIDController = new PIDController(Kp, Ki, Kd);
    }

    public void gotoElevatorPosition(double position, double speed){
        climbPIDController.setSetpoint(position);
        climbPIDController.setTolerance(TOLERANCE);
        while(climbPIDController.atSetpoint() == false){
        elevatorController.set(climbPIDController.calculate(elevatorEncoder.getPosition(),(climbPIDController.calculate(position))));
        }
        elevatorController.set(0.0);
    }

    public void startWinch(double speed){
        winchController.set(speed);
    }

    public double getFlexSensorPosition(){
        double flexSensorPosition = climbingFlexSensor.get();
        return flexSensorPosition;
    }
}

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
import com.revrobotics.CANSparkMax.IdleMode;

/**
 * Add your docs here.
 */
public class Climber extends SubsystemBase{

    private CANSparkMax winchController;
    private CANSparkMax elevatorController;
    private Encoder elevatorEncoder;
    private AnalogPotentiometer climbingFlexSensor;
    private PIDController climbPIDController;

    private final int WINCH_CAN_ID = 20;
    private final int ELEVATOR_CAN_ID = 6;
    private final int CLIMBING_FLEX_SENSOR_PORT = 0;
    private final double Kp = 0.5;
    private final double Ki = 0.0;
    private final double Kd = 0.0;
    private final double TOLERANCE = 10;
    private final double TESTING_CONSTANT = 0.1;
    private final double SPROCKET_RADIUS = .6 ;
    
    private MotorType CLIMBING_MOTOR_TYPE = MotorType.kBrushless;

    public Climber() {
        this.winchController = new CANSparkMax(WINCH_CAN_ID, CLIMBING_MOTOR_TYPE);
        this.elevatorController = new CANSparkMax(ELEVATOR_CAN_ID, CLIMBING_MOTOR_TYPE);
        this.climbingFlexSensor = new AnalogPotentiometer(CLIMBING_FLEX_SENSOR_PORT, 180, 90);

        this.elevatorEncoder = new Encoder(4, 5);
        elevatorEncoder.setDistancePerPulse((SPROCKET_RADIUS * 2 * Math.PI) / (4* (2048.0)));

        this.elevatorController.set //FIX THIS

        climbPIDController = new PIDController(Kp, Ki, Kd);
    }

    public void gotoElevatorPosition(double position, double speed){
        System.out.println("starting climbing code");
        setClimberPIDSetpoint(position);
        setClimberPIDTolerance();
        while(elevatorEncoder.getDistance() < position)
        {
            this.elevatorController.set
        }
        // while(climbPIDController.atSetpoint() == false){
        //     System.out.println("going to position");
        //     setElevatorSpeed(position);
        // }

        System.out.println("at position " + this.elevatorEncoder.getDistance());
        elevatorController.setIdleMode(IdleMode.kBrake);
        elevatorController.set(0.0);
    }
    public void setClimberPIDSetpoint(double position){
        climbPIDController.setSetpoint(position);
    }

    public void setClimberPIDTolerance(){
        climbPIDController.setTolerance(TOLERANCE);
    }

    public void setElevatorSpeed(double position){
        elevatorController.set(TESTING_CONSTANT*(climbPIDController.calculate(elevatorEncoder.getDistance(),(climbPIDController.calculate(position)))));
    }


    public void startWinch(double speed){
        winchController.set(speed);
    }

    public void getFlexSensorPosition(){
        double flexSensorPosition = climbingFlexSensor.get();
        System.out.println("flex Sensor: " + flexSensorPosition);
    }
}
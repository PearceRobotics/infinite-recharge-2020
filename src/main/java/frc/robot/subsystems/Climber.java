/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Add your docs here.
 */
public class Climber extends SubsystemBase{

    private final CANSparkMax winchController;
    private final CANSparkMax elevatorController;

    private final int WINCH_CAN_ID = 12;
    private final int ELEVATOR_CAN_ID = 13;
    
    private MotorType CLIMBING_MOTOR_TYPE = MotorType.kBrushless;

    public Climber() {
        this.winchController = new CANSparkMax(WINCH_CAN_ID, CLIMBING_MOTOR_TYPE);
        this.elevatorController = new CANSparkMax(ELEVATOR_CAN_ID, CLIMBING_MOTOR_TYPE);
    }

    public void setElevatorPosition(double position, double speed){
        
    }

    public void startWinch(double speed){
        winchController.set(speed);
    }

    public double getFlexSensorPosition(){
        double flexSensorPosition;
        return flexSensorPosition;
    }
}

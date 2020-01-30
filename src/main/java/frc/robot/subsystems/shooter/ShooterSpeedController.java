/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 1745 JJ Pearce Robotics. All Rights Reserved.           */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.shooter;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class ShooterSpeedController
{
    
    private final int LEFT_SHOOTER_CAN_ID = 9;
    private final int RIGHT_SHOOTER_CAN_ID = 8;
    private MotorType DRIVE_MOTOR_TYPE = MotorType.kBrushless;

    private final double RATIO = (22.0/16.0); //gear ratio
    private final double WHEEL_DIAMETER = 4.0; //inches
    private final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI; //inches

    private final double ACCEPTABLE_DIFFERENCE = 5.0; //inches per second allowable difference for shot to be made

    private final CANSparkMax leftController;
    private final CANSparkMax rightController;

    private final double FULL_SPEED = 1.0; //percent of motor power

    private double setLaunchSpeed; //in inches/second

    //Default Constructor.  Set initial launch speed to 0 inches/second.
    public ShooterSpeedController() 
    {
        this.leftController = new CANSparkMax(LEFT_SHOOTER_CAN_ID, DRIVE_MOTOR_TYPE);
        this.rightController = new CANSparkMax(RIGHT_SHOOTER_CAN_ID, DRIVE_MOTOR_TYPE);
        setLaunchSpeed = 0.0;
    }


    public void setFullForward()
    {
        this.leftController.set(-FULL_SPEED);
        this.rightController.set(FULL_SPEED);
    }    

    public void setFullReverse()
    {
        this.leftController.set(0.0);
        this.rightController.set(0.0);
    }    

    //Set the speed that the shooter speed controller will attempt to get to.
    public void setLaunchSpeed(double speed)
    {
        this.setLaunchSpeed = speed;
    }

    //Get the set launch speed in inches/second
    public double getLaunchSpeed()
    {
        return this.setLaunchSpeed;
    }

    //Get the current speed of the shooter in inches/second
    public double getCurrentSpeed()
    {
        System.out.println("leftVelocity " + this.leftController.getEncoder().getVelocity());
        System.out.println("rightVelocity " + this.rightController.getEncoder().getVelocity());
        System.out.println("tangential speed " +  (((-this.leftController.getEncoder().getVelocity() + this.rightController.getEncoder().getVelocity()) / 2) * WHEEL_CIRCUMFERENCE * RATIO / 60.0));
        //Assuming that the shooter wheel speed is the average of the two motors times wheel circumference and gear ratio
        return (((-this.leftController.getEncoder().getVelocity() + this.rightController.getEncoder().getVelocity()) / 2) * WHEEL_CIRCUMFERENCE * RATIO / 60.0);
    }

    public boolean isAtSpeed()
    {
        if(Math.abs(getCurrentSpeed() - setLaunchSpeed) < ACCEPTABLE_DIFFERENCE)
        {
            return true;
        }
        else
        {
            return false;
        }
    }








}
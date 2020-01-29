/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 1745 JJ Pearce Robotics. All Rights Reserved.           */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class ShooterSpeedController
{
    
    private final int LEFT_SHOOTER_CAN_ID = 9;
    private final int RIGHT_SHOOTER_CAN_ID = 10;
    private MotorType DRIVE_MOTOR_TYPE = MotorType.kBrushless;

    private final double RATIO = (22.0/16.0); //gear ratio
    private final double WHEEL_DIAMETER = 4.0; //inches
    private final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI; //inches

    private final double ACCEPTABLE_DIFFERENCE = 10.0; //inches per second allowable difference for shot to be made

    private final CANSparkMax leftController;
    private final CANSparkMax rightController;

    private double setLaunchSpeed; //in inches/second

    //Default Constructor.  Set initial launch speed to 0 inches/second.
    public ShooterSpeedController()
    {
        this.leftController = new CANSparkMax(LEFT_SHOOTER_CAN_ID, DRIVE_MOTOR_TYPE);
        this.rightController = new CANSparkMax(RIGHT_SHOOTER_CAN_ID, DRIVE_MOTOR_TYPE);
        setLaunchSpeed = 0.0;
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
        //Assuming that the shooter wheel speed is the average of the two motors times wheel circumference and gear ratio
        return (((this.leftController.getEncoder().getVelocity() + this.rightController.getEncoder().getVelocity()) / 2) * WHEEL_CIRCUMFERENCE * RATIO);
    }

    //This method must be called every time through the periodic loop.  
    // It will use a 'bang-bang' style control scheme to get the shooter wheel to the set launch speed.
    public void execute()
    {
        if(getCurrentSpeed() < setLaunchSpeed)
        {
            //If speed is too low, set controllers to maximum speed
            this.leftController.set(1.0);
            this.rightController.set(-1.0);
        }
        else
        {            
            //If speed is too high, set controllers to 0
            this.leftController.set(0.0);
            this.rightController.set(0.0);
        }
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
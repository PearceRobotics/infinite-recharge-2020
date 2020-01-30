/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 1745 JJ Pearce Robotics. All Rights Reserved.           */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.shooter;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class ShooterSpeedController {

    private final int LEFT_SHOOTER_CAN_ID = 9;
    private final int RIGHT_SHOOTER_CAN_ID = 8;
    private MotorType DRIVE_MOTOR_TYPE = MotorType.kBrushless;

    private final double RATIO = (22.0 / 16.0); // gear ratio

    // bang bang variables below
    private final double WHEEL_DIAMETER = 4.0; // inches
    private final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI; // inches
    private final double FULL_SPEED = 1.0; // percent of motor power
    private final double ACCEPTABLE_DIFFERENCE = 5.0; // inches per second allowable difference for shot to be made

    private final CANSparkMax leftController;
    private final CANSparkMax rightController;

    private double setLaunchSpeed; // in inches/second

    // Pid controller variables below
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;

    // Default Constructor. Set initial launch speed to 0 inches/second.
    public ShooterSpeedController() {
        this.leftController = new CANSparkMax(LEFT_SHOOTER_CAN_ID, DRIVE_MOTOR_TYPE);
        this.rightController = new CANSparkMax(RIGHT_SHOOTER_CAN_ID, DRIVE_MOTOR_TYPE);
        setLaunchSpeed = 0.0;

        // PID calls
        /**
         * The RestoreFactoryDefaults method can be used to reset the configuration
         * parameters in the SPARK MAX to their factory default state. If no argument is
         * passed, these parameters will not persist between power cycles
         */
        this.leftController.restoreFactoryDefaults();
        this.rightController.restoreFactoryDefaults();

        // PID coefficients
        kP = 5e-5;
        kI = 1e-6;
        kD = 0;
        kIz = 0;
        kFF = 0;
        kMaxOutput = 1;
        kMinOutput = -1;

        // set PID coefficients
        this.leftController.getPIDController().setP(kP);
        this.rightController.getPIDController().setP(kP);

        this.leftController.getPIDController().setI(kI);
        this.rightController.getPIDController().setI(kI);

        this.leftController.getPIDController().setD(kD);
        this.rightController.getPIDController().setD(kD);

        this.leftController.getPIDController().setIZone(kIz);
        this.rightController.getPIDController().setIZone(kIz);

        this.leftController.getPIDController().setFF(kFF);
        this.rightController.getPIDController().setFF(kFF);

        this.leftController.getPIDController().setOutputRange(kMinOutput, kMaxOutput);
        this.rightController.getPIDController().setOutputRange(kMinOutput, kMaxOutput);
    }

    private void setPidSpeed(double rpm) {
        this.leftController.getPIDController().setReference(-rpm, ControlType.kVelocity);
        this.rightController.getPIDController().setReference(rpm, ControlType.kVelocity);
    }

    public void setFullForward() {
        this.leftController.set(-FULL_SPEED);
        this.rightController.set(FULL_SPEED);
    }

    public void setFullReverse() {
        this.leftController.set(0.0);
        this.rightController.set(0.0);
    }

    // Set the speed that the shooter speed controller will attempt to get to.
    public void setLaunchSpeed(double speed) {
        this.setLaunchSpeed = speed;
        setPidSpeed(((speed * 60) / WHEEL_CIRCUMFERENCE)/RATIO);
    }

    // Get the set launch speed in inches/second
    public double getLaunchSpeed() {
        return this.setLaunchSpeed;
    }

    // Get the current speed of the shooter in inches/second
    public double getCurrentSpeed() {
        System.out.println("tangential speed "
                + (((-this.leftController.getEncoder().getVelocity() + this.rightController.getEncoder().getVelocity())
                        / 2) * WHEEL_CIRCUMFERENCE * RATIO / 60.0));
        // Assuming that the shooter wheel speed is the average of the two motors times
        // wheel circumference and gear ratio
        return (((-this.leftController.getEncoder().getVelocity() + this.rightController.getEncoder().getVelocity())
                / 2) * WHEEL_CIRCUMFERENCE * RATIO / 60.0);
    }

    public boolean isAtSpeed() {
        if (Math.abs(getCurrentSpeed() - setLaunchSpeed) < ACCEPTABLE_DIFFERENCE) {
            return true;
        } else {
            return false;
        }
    }
}
package frc.robot.subsystems.shooter;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ShooterSpeedController extends SubsystemBase{

    private final int LEFT_SHOOTER_CAN_ID = 14;
    private final int RIGHT_SHOOTER_CAN_ID = 7;
    private MotorType DRIVE_MOTOR_TYPE = MotorType.kBrushless;
    private final CANSparkMax leftController;
    private final CANSparkMax rightController;

    private final double RATIO = (22.0 / 16.0); // gear ratio
    private final double WHEEL_DIAMETER = 4.0; // inches
    private final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI; // inches
    private final double ACCEPTABLE_DIFFERENCE = 50.0; // inches per second allowable difference for shot to be made
    private final double SECONDS_PER_MINUTE = 60.0;

    private double setLaunchSpeed; // in inches/second

    // PID coefficients
    public double kP = 0.000050; // 5e-5;
    public double kI = 0.0000004/2; // 4e-7;
    public double kD = 0.0000004*2; // 0.0
    public double kIz = 0.0; // 0.0
    public double kFF = 0.0; // 0.0
    public double kMaxOutput = 1;
    public double kMinOutput = -1;

    // Default Constructor. Set initial launch speed to 0 inches/second.
    public ShooterSpeedController() {
        this.leftController = new CANSparkMax(LEFT_SHOOTER_CAN_ID, DRIVE_MOTOR_TYPE);
        this.rightController = new CANSparkMax(RIGHT_SHOOTER_CAN_ID, DRIVE_MOTOR_TYPE);
        this.leftController.setSmartCurrentLimit(60);
        this.rightController.setSmartCurrentLimit(60);
        setLaunchSpeed = 0.0;

        // PID calls
        /**
         * The RestoreFactoryDefaults method can be used to reset the configuration
         * parameters in the SPARK MAX to their factory default state. If no argument is
         * passed, these parameters will not persist between power cycles
         */
        this.leftController.restoreFactoryDefaults();
        this.rightController.restoreFactoryDefaults();

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

    // Set the speed that the shooter speed controller will attempt to get to.
    public void setLaunchSpeed(double speed) {
        this.setLaunchSpeed = speed;
        setPidSpeed(((speed * SECONDS_PER_MINUTE) / WHEEL_CIRCUMFERENCE) / RATIO);
    }

    // Get the set launch speed in inches/second
    public double getLaunchSpeed() {
        return this.setLaunchSpeed;
    }

    // Get the current speed of the shooter in inches/second
    public double getCurrentSpeed() {
        // Assuming that the shooter wheel speed is the average of the two motors times
        // wheel circumference and gear ratio
        return (((-this.leftController.getEncoder().getVelocity() + this.rightController.getEncoder().getVelocity())
                / 2) * WHEEL_CIRCUMFERENCE * RATIO / SECONDS_PER_MINUTE);
    }

    public boolean isAtSpeed() {
        return (Math.abs(getCurrentSpeed() - setLaunchSpeed) < ACCEPTABLE_DIFFERENCE);
    }
}
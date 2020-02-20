package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Gyroscope;
import frc.robot.subsystems.drive.Drive;

public class GyroTurnCommand extends CommandBase {
    // Classes
    private final Gyroscope gyro;
    private final Drive drive;
    // Variables
    private final double turnAngle;// Angle we want the robot to rotate in degrees
    private double newAngle;// Angle gyroscope will see new angle as
    private double error; // continuous error the robot is off from its desired position
    private double speed;// speed the robot turns
    // Constants
    private final double P_VALUE = 0.0027 * 2.25;
    private final double minSpeed = 0.15;// minimum speed the robot will drive
    private final double DEADBAND = 1.0;

    public GyroTurnCommand(final Gyroscope gyro, final double turnAngle, final Drive drive) {
        this.gyro = gyro;
        this.turnAngle = turnAngle;
        this.drive = drive;

        addRequirements(gyro);
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        final double currentAngle = gyro.getGyroAngle();
        newAngle = currentAngle + turnAngle;
        drive.setBrakeMode();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {

        error = newAngle - gyro.getGyroAngle();
        speed = -error * P_VALUE;

        if(Math.abs(speed) < minSpeed) {
            if (speed < 0.0) {
                speed = -minSpeed;
            }
            else if(speed >= 0.0) {
                speed = minSpeed;
            } else {
                 // do nothing 
            } 
        }
         
        drive.arcadeDrive(0.0, speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if (Math.abs(gyro.getGyroAngle() - newAngle) > DEADBAND) {
            return false;
        } else {
            drive.arcadeDrive(0.0, 0.0);
            return true;
        }
    }

    // Called once after isFinished returns true
    @Override
    public void end(final boolean interrupted) {

    }
}
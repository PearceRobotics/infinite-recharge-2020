package frc.robot.commands.autonomousCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;

public class DriveForwardCommand extends CommandBase {

    private Drive drive;
    private double distance;
    private double maxSpeed;
    private double startTime;
    private final double DRIVE_TIME = 2.0;

    public DriveForwardCommand(double distance, double maxSpeed, Drive drive) {
        this.distance = distance;
        this.maxSpeed = maxSpeed;
        this.drive = drive;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        drive.setBrakeMode();
        drive.resetEncoders();
        startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        double forwardSpeed = maxSpeed - ((drive.getLeftEncoderDistance() / distance) * maxSpeed);
        drive.arcadeDrive(-forwardSpeed, 0);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return (Timer.getFPGATimestamp() - startTime) > DRIVE_TIME;
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        drive.arcadeDrive(0.0, 0.0);
        drive.setCoastMode();
    }
}
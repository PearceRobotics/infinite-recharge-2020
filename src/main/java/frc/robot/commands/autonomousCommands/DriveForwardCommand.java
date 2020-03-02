package frc.robot.commands.autonomousCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;

public class DriveForwardCommand extends CommandBase {

    private Drive drive;
    private double distance;
    private double maxSpeed;

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
        System.out.println("driving forward");
        drive.setBrakeMode();
        drive.resetEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        double fowardSpeed = maxSpeed - ((drive.getLeftEncoderDistance() / distance) * maxSpeed);
        drive.arcadeDrive(-fowardSpeed, 0);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if (drive.getLeftEncoderDistance() <= -distance && drive.getRightEncoderDistance() <= -distance) {
            drive.setRightSpeed(0);
            drive.setLeftSpeed(0);
            return true;
        }
        return false;
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        drive.setCoastMode();
    }
}
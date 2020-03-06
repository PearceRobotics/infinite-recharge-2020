package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;

public class AutonomousCommand extends CommandBase {

    private Drive drive;
    private double pValue;
    private double distance;
    private double maxSpeed;

    public AutonomousCommand(double distance, double maxSpeed, Drive drive, double pValue) {
        this.distance = distance;
        this.pValue = pValue;
        this.maxSpeed = maxSpeed;
        this.drive = drive;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setPValue(double pValue) {
        this.pValue = pValue;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        drive.setBrakeMode();
        drive.resetEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        double turnPower = drive.straightTurnPower(pValue);
        double fowardSpeed = maxSpeed - ((drive.getLeftEncoderDistance() / distance) * maxSpeed);
        drive.arcadeDrive(-fowardSpeed, -turnPower);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if (drive.getLeftEncoderDistance() <= -distance && drive.getRightEncoderDistance() <= -distance) {
            drive.arcadeDrive(0.0, 0.0);
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
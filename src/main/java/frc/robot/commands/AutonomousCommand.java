package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;

public class AutonomousCommand extends CommandBase {

    private Drive drive;
    double distance;
    private double maxSpeed;

    public AutonomousCommand(double distance, double maxSpeed, Drive drive) {
        this.distance = distance;
        this.maxSpeed = maxSpeed;
        this.drive = drive;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        System.out.println("Initialized");
        drive.setBrakeMode();
        drive.resetEncoders();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        System.out.println("executing");
        double turnPower = drive.straightTurnPower();
        double fowardSpeed = maxSpeed - ((drive.getLeftEncoderDistance() / distance) * maxSpeed);
        drive.arcadeDrive(fowardSpeed, turnPower);
        System.out.println("Current Distance " + drive.getLeftEncoderDistance());
        System.out.println("Desired Distance " + distance);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if (drive.getLeftEncoderDistance() >= distance && drive.getRightEncoderDistance() >= distance) {
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

package frc.robot.commands.autonomousCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.lights.Lights;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.subsystems.vision.LimelightAim;

public class AimAndDriveBackCommand extends CommandBase {

    private Drive drive;
    private Limelight limelight;
    private Lights lights;

    private double distance;
    private double maxSpeed;
    private double startTime;
    private final double DRIVE_TIME = 2.0;

    public AimAndDriveBackCommand(double distance, double maxSpeed, Limelight limelight, Drive drive, Lights lights) {
        this.distance = distance;
        this.maxSpeed = maxSpeed;
        this.lights = lights;

        this.limelight = limelight;
        this.drive = drive;
        addRequirements(drive);
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
        lights.setIsAutonOn(true);
        drive.resetEncoders();
        startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        double fowardSpeed = maxSpeed - ((drive.getLeftEncoderDistance() / distance) * maxSpeed);
        double turnSpeed = LimelightAim.getSteeringAdjust(limelight.getHorizontalTargetOffset());
        drive.arcadeDrive(-fowardSpeed, turnSpeed);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - startTime > DRIVE_TIME;
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        drive.arcadeDrive(0.0, 0.0);
    }
}
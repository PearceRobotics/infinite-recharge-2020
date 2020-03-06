package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Limelight;

/**
 * Add your docs here.
 */
public class AimCommand extends CommandBase {
    private Drive drive;
    private Limelight limelight;

    private final double DEADBAND_DEGREES = 2;
    private final double MAX_SPEED = .4;
    private final double MIN_SPEED = 0.2;
    private final double KpAIM = 0.09;

    public AimCommand(Drive drive, Limelight limelight) {
        this.drive = drive;
        this.limelight = limelight;
        addRequirements(drive);
    }

    // Called just before this Command runs the first time  
    @Override
    public void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        if (limelight.hasValidTarget()) {
                // record offset early because it gets used repeatedly
                double offset = limelight.getHorizontalTargetOffset();
                // Keep steering adjust between MIN and MAX. set to abs to determine magnitude,
                // but reuse the sign
                double steeringAdjust = Math
                        .copySign(Math.max(MIN_SPEED, Math.min(MAX_SPEED, KpAIM * Math.abs(offset))), offset);
                //if(Math.abs(limelight.getHorizontalTargetOffset()) > DEADBAND_DEGREES) {
                    //double steeringAdjust = -KpAIM * limelight.getHorizontalTargetOffset();
                    drive.arcadeDrive(0.0, steeringAdjust);
                //}

        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        drive.arcadeDrive(0.0, 0.0);
    }
}

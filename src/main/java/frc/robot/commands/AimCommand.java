package frc.robot.commands.powerCellScoringCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Limelight;

/**
 * Add your docs here.
 */
public class AimCommand extends CommandBase {
    private Drive drive;
    private Limelight limelight;

    private final double MAX_SPEED = 1.0;
    private final double MIN_SPEED = 0.05;
    final double KpAIM = 0.025;

    public AimCommand(Drive drive, Limelight limelight) {
        this.drive = drive;
        this.limelight = limelight;
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
                drive.arcadeDrive(0.0, steeringAdjust);

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

package frc.robot.commands.powerCellScoringCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Limelight;

/**
 * Add your docs here.
 */
public class TurnToTopTargetCommand extends CommandBase {
    private Drive drive;
    private Limelight limelight;
    public final double TOP_GOAL_DEADBAND = 0.5;

    // want to be on target for a COUNT_ON_TARGET count
    private int count;
    private final int COUNT_ON_TARGET = 10;

    private final double MAX_SPEED = 1.0;
    private final double MIN_SPEED = 0.05;
    final double KpAIM = 0.025;

    public TurnToTopTargetCommand(Drive drive, Limelight limelight) {
        this.drive = drive;
        this.limelight = limelight;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        count = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        if (limelight.hasValidTarget()) {
            // record offset early because it gets used repeatedly
            double offset = limelight.getHorizontalTargetOffset();
            if (Math.abs(offset) > TOP_GOAL_DEADBAND) {
                                // Keep steering adjust between MIN and MAX. set to abs to determine magnitude,
                // but reuse the sign
                double steeringAdjust = Math
                        .copySign(Math.max(MIN_SPEED, Math.min(MAX_SPEED, KpAIM * Math.abs(offset))), offset);
                drive.arcadeDrive(0.0, steeringAdjust);
            }
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if (limelight.hasValidTarget() && Math.abs(limelight.getHorizontalTargetOffset()) <= TOP_GOAL_DEADBAND) {
            ++count;
            if (count >= COUNT_ON_TARGET) {
                return true;
            }
        }
        else if(!limelight.hasValidTarget())
        {
            //This is a failed situation, we should cancel the whole command group
        }
        else{
            if(count > 0)
            {
                --count;
            }
            //do nothing
        }
        return false;
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        drive.arcadeDrive(0.0, 0.0);
    }
}

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.shooter.ShooterSpeedController;

public class IndexerOutakeCommand extends CommandBase {

    private IndexerController indexerController;
    private ShooterSpeedController shooterSpeedController;

    public IndexerOutakeCommand(IndexerController indexerController){
        this.indexerController = indexerController;

        addRequirements(indexerController);
    }

    @Override
    public void execute() {
        indexerController.outtake();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return false;
            //This is a failed situation, we should cancel the whole command group
 
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
        indexerController.stop();
    }
}
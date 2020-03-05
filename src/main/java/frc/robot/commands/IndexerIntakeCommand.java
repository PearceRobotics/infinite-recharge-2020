package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.IndexerController;

public class IndexerIntakeCommand extends CommandBase {

    private IndexerController indexerController;

    public IndexerIntakeCommand(IndexerController indexerController){
        this.indexerController = indexerController;

        addRequirements(indexerController);
    }

    @Override
    public void execute() {
        indexerController.intake();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        return true;
            //This is a failed situation, we should cancel the whole command group
 
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {

    }
}

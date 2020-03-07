package frc.robot.commands.climbingCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

/**
 * Add your docs here.
 */
public class ElevatorUpCommand extends CommandBase {

    private Climber climber;
    private double startTime = 0.0;
    private final double ALLOWED_RUN_TIME = 0.5;

    public ElevatorUpCommand(Climber climber) {
        this.climber = climber;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        this.climber.moveElevatorUp();
        startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        // do nothing
    }

    @Override
    public boolean isFinished() {
        return (this.climber.isElevatorAtSetPoint() || ((Timer.getFPGATimestamp() - startTime) > ALLOWED_RUN_TIME));
    }
}

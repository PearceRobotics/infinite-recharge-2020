package frc.robot.commands.drivingCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;

public class SpeedModeSwitchCommand extends CommandBase {

    private Drive drive;

    public SpeedModeSwitchCommand(Drive drive) {
        this.drive = drive;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        drive.setDriveSlow(!drive.getDriveSlow());
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        // do nothing
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
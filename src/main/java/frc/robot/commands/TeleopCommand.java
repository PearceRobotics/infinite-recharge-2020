package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.io.Controls;
import frc.robot.subsystems.drive.Drive;

public class TeleopCommand extends CommandBase {

    private Drive drive;
    private Controls controls;
    private double DEADZONE = 0.2;
  
    public TeleopCommand(Controls controls, Drive drive) {
        this.controls = controls;
        this.drive = drive;

        addRequirements(controls);
        addRequirements(drive);
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        drive.arcadeDrive(controls.getLeftY(DEADZONE), controls.getRightX(DEADZONE));
    }
}
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.io.Controls;
import frc.robot.subsystems.drive.Drive;

public class TeleopCommand extends CommandBase {

    private Drive drive;
    private double pValue;
    private Controls controls;
    private double DEADZONE = 0.11;
    private boolean resetEncoders;
  
    public TeleopCommand(Controls controls, Drive drive, double  pValue) {
        this.controls = controls;
        this.drive = drive;
        this.pValue = pValue;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        if (controls.getRightX(DEADZONE) == 0.0) {
            if (resetEncoders == false) {
                drive.resetEncoders();
                resetEncoders = true;
            }
            drive.arcadeDrive(controls.getLeftY(DEADZONE), -drive.straightTurnPower(pValue));
        } else {
            drive.arcadeDrive(-controls.getLeftY(DEADZONE), -controls.getRightX(DEADZONE));
            resetEncoders = false;
        }
    }
}

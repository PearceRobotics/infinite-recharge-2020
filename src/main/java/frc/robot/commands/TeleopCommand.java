package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.io.Controls;
import frc.robot.subsystems.drive.Drive;

public class TeleopCommand extends CommandBase {

    private Drive drive;
    private Controls controls;
    private double DEADZONE = 0.11;
    private boolean resetEncoders;
  
    public TeleopCommand(Controls controls, Drive drive) {
        this.controls = controls;
        this.drive = drive;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        System.out.println("Initialized");

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        if (controls.getRightX(DEADZONE) == 0.0) {
            if (resetEncoders = false) {
                drive.resetEncoders();
                resetEncoders = true;
            }
            System.out.println("drive straight: " + controls.getLeftY(DEADZONE));
            System.out.println("turn code: " + (drive.straightTurnPower()));
            drive.arcadeDrive(controls.getLeftY(DEADZONE), drive.straightTurnPower());
        } else {
            drive.arcadeDrive(controls.getLeftY(DEADZONE), controls.getRightX(DEADZONE));
            resetEncoders = false;
        }
    }
}

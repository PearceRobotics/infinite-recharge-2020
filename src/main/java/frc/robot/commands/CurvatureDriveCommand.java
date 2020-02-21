package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.io.Controls;
import frc.robot.subsystems.drive.Drive;

public class CurvatureDriveCommand extends CommandBase {

    private Drive drive;
    private Controls controls;
    private double DEADZONE = 0.2;
  
    public CurvatureDriveCommand(Controls controls, Drive drive) {
        this.controls = controls;
        this.drive = drive;

        addRequirements(drive);
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
    double throttle = controls.getLeftY(DEADZONE);
    double turn = controls.getRightX(DEADZONE);
        if(Math.abs(turn) < DEADZONE || Math.abs(throttle) < DEADZONE){
            drive.arcadeDrive(throttle, turn);
        }
        else{
        drive.curvatureDrive(-throttle, turn, false);
        }
    }
}
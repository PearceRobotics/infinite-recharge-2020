package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.operatorInputs.Controls;
import frc.robot.subsystems.drive.Drive;

public class CurvatureDriveCommand extends CommandBase {

    //classes
    private Drive drive;
    private Controls controls;

    //constants
    private final double DEADZONE = 0.2;
    private final double TURN_CONSTANT = .5;
  
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
                drive.arcadeDrive(throttle*.8, turn *.75);
            }
        else{ 
            drive.curvatureDrive(-(throttle*.8), turn * TURN_CONSTANT, false);
            }
    }
}
package frc.robot.commands.drivingCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.operatorInputs.Controls;
import frc.robot.subsystems.drive.Drive;

public class CurvatureDriveCommand extends CommandBase {

    //classes
    private Drive drive;
    private Controls controls;

    //constants
    private final double DEADZONE = 0.2;
    private final double TURN_CONSTANT = 0.5;
    private final double THROTTLE_PERCENT = 0.8;
    private final double TURN_PERCENT = 0.75;
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
                drive.arcadeDrive(throttle*THROTTLE_PERCENT, turn *TURN_PERCENT);
            }
        else{ 
            drive.curvatureDrive(-(throttle*THROTTLE_PERCENT), turn * TURN_CONSTANT, false);
            }
    }
}
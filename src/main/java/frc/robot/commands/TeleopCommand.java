package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.io.Controls;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Gyroscope;

public class TeleopCommand extends CommandBase {

    private Drive drive;
    private Gyroscope gyroscope;
    private Controls controls;
    private double DEADZONE = 0.11;
    private boolean resetEncoders;
  
    public TeleopCommand(Controls controls, Drive drive, Gyroscope gyroscope) {
        this.controls = controls;
        this.drive = drive;
        this.gyroscope = gyroscope;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        System.out.println("Initialized");
        gyroscope.resetGyroAngle();

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        if (controls.getRightX(DEADZONE) == 0.0) {
            if (resetEncoders = false) {
                drive.resetEncoders();
                resetEncoders = true;
            }
            drive.arcadeDrive(controls.getLeftY(DEADZONE), drive.straightTurnPower());
        } else {
            drive.arcadeDrive(controls.getLeftY(DEADZONE), controls.getRightX(DEADZONE));
            resetEncoders = false;
        }
        if(controls.getDpadUpButton()){
            gyroscope.rotate(180, .3); //rotate right 180 degrees
        }
        else if(controls.getDpadDownButton()){
            gyroscope.rotate(-180, .3); //rotate left 180 degrees
        }
        else if(controls.getDpadLeftButton()){
            gyroscope.rotate(-90, .3); //rotate left 90 degrees
        }
        else if(controls.getDpadRightButton()){
            gyroscope.rotate(90, .3); //rotate right 90 degrees
        }
    }
}

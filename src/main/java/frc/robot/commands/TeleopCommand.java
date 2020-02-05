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
    double desiredAngle;
    boolean drivingStraight = false;
  
    public TeleopCommand(Controls controls, Drive drive, Gyroscope gyroscope) {
        this.controls = controls;
        this.drive = drive;
        this.gyroscope = gyroscope;
    }

    // Called just before this Command runs the first time
    @Override
    public void initialize() {
        System.out.println("Initialized");
        gyroscope.resetGyro();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        System.out.println(" Y axis" + gyroscope.getGyroAngle());
        if (controls.getRightX(DEADZONE) == 0.0) {
            drive.setCoastMode();
            System.out.println("driveStraight");
            if(drivingStraight == false){
                desiredAngle = gyroscope.getGyroAngle(); 
                drivingStraight = true;
            }
            gyroscope.driveStraightGyro(controls.getLeftY(DEADZONE), desiredAngle);
            System.out.println("desired Angle" + desiredAngle);
            System.out.println("GyroAngle" + gyroscope.getGyroAngle());
        } else {
            drive.setCoastMode();
            drive.arcadeDrive(controls.getLeftY(DEADZONE), controls.getRightX(DEADZONE));
            drivingStraight = false;
        }
        if(controls.getRightTrigger()){
            drive.setBrakeMode();
            gyroscope.rotate(180, .3); //rotate right 180 degrees
        }
        else if(controls.getLeftTrigger()){
            drive.setBrakeMode();
            gyroscope.rotate(-180, .3); //rotate left 180 degrees
        }
        else if(controls.getLeftBumper()){
            drive.setBrakeMode();
            gyroscope.rotate(-90, .3); //rotate left 90 degrees
        }
        else if(controls.getRightBumper()){
            drive.setBrakeMode();
            gyroscope.rotate(90, .3); //rotate right 90 degrees
        }
    }

}

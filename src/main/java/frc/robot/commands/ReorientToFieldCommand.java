package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Gyroscope;

public class ReorientToFieldCommand extends CommandBase {
    //Classes
    private Drive drive;
    private Gyroscope gyro;

    //Constants
    private final double P_VALUE = 0.0027;//ratio to multiply error by to get a number between -1 and 1 for the speed
    private final double MIN_SPEED = 0.1;// minimum speed the robot will drive
    private final double DEADBAND = 1.0;

    public ReorientToFieldCommand(Drive drive, Gyroscope gyro){
        this.drive = drive;
        this.gyro = gyro;
    }

    @Override
    public void initialize() {
        drive.setBrakeMode();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        double error = gyro.getGyroAngle() % 360.0;
        double speed = error * P_VALUE;

        if(Math.abs(speed) < MIN_SPEED){
            if (speed < 0.0){
                speed = -MIN_SPEED;
            }
            else if(speed > 0.0) {
                speed = MIN_SPEED;
            }
        }

        drive.arcadeDrive(0.0, speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if((Math.abs(gyro.getGyroAngle()) % 360.0) > DEADBAND){
            return false;
        }
        else {
            drive.arcadeDrive(0.0, 0.0);
            return true;
        }
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {

    }    
}
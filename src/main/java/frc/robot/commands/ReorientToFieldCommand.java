package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Gyroscope;

public class ReorientToFieldCommand extends CommandBase{

    private Drive drive;
    private Gyroscope gyro;
    public ReorientToFieldCommand(Drive drive, Gyroscope gyro){
        this.drive = drive;
        this.gyro = gyro;
    }

    @Override
    public void initialize() {
        double currentAngle = gyro.getGyroAngle();
        drive.setBrakeMode();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
                double error = gyro.getGyroAngle()%360;
                double speed = -error*(0.0027) *.65;
                if(Math.abs(speed) < .1){
                    if (speed/ Math.abs(speed) == -1){
                        speed = -.1;
                    }
                    else if(speed/ Math.abs(speed) == 1) {
                        speed = .1;
                    }
                    else{
                        // do nothing
                    }
                }
                drive.arcadeDrive(0, speed);
                System.out.println("gyro angle" + gyro.getGyroAngle());
                System.out.println("gyro speed" + speed);
        }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if((Math.abs(gyro.getGyroAngle())% 360) > 1)
        {
            return false;
        }
        else{
            System.out.println("gyro angle" + gyro.getGyroAngle());
            System.out.println("complete");
            drive.arcadeDrive(0,0);
            return true;
        }
      
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
     
    }

    
}



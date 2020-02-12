/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.controller.PIDController;
import frc.robot.subsystems.drive.Gyroscope;
import frc.robot.subsystems.drive.Drive;

public class GyroTurnCommand extends CommandBase{

    private Gyroscope gyro;
    private double turnAngle;
    private Drive drive;
    private double newAngle;
   // private PIDController rotateController;

    public GyroTurnCommand(Gyroscope gyro, double turnAngle, Drive drive, double kP, double kI, double kD){

        this.gyro = gyro;
        this.turnAngle = turnAngle;
        this.drive = drive;
       // rotateController = new PIDController(kP, kI, kD);

        addRequirements(gyro);
        addRequirements(drive);
    }
    @Override
    public void initialize() {
        double currentAngle = gyro.getGyroAngle();
        newAngle = currentAngle + turnAngle;
        drive.setBrakeMode();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
                double error = newAngle - gyro.getGyroAngle();
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
        if(Math.abs(gyro.getGyroAngle() - newAngle) > 1)
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




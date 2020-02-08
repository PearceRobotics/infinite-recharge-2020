/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Gyroscope;
import frc.robot.subsystems.drive.Drive;

public class GyroTurnCommand extends CommandBase{

    private Gyroscope gyro;
    private double turnAngle;
    private double speed;
    private Drive drive;
    private double newAngle;

    GyroTurnCommand(Gyroscope gyro, double turnAngle, Drive drive){
        this.gyro = gyro;
        this.turnAngle = turnAngle;
        this.drive = drive;
    }
    @Override
    public void initialize() {
        double currentAngle = gyro.getGyroAngle();
        newAngle = currentAngle + turnAngle;
        if( newAngle/ Math.abs(newAngle) == 1){
        speed = .75; //speed stays positive
        }
        else{
        speed = -.75; //speed is negative
        }
        drive.setBrakeMode();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
                drive.arcadeDrive(-0, -speed); //b/c joysticks come out negative, we can either change the arcade method, or just make all things plugged in negative
        }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if(gyro.getGyroAngle()< newAngle)
        {
            return false;
        }
        else{
            return true;
        }
      
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
     
    }

}




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
    private PIDController rotateController;

    public GyroTurnCommand(Gyroscope gyro, double turnAngle, Drive drive, double kP, double kI, double kD, double  kF){

        this.gyro = gyro;
        this.turnAngle = turnAngle;
        this.drive = drive;
        rotateController = new PIDController(kP, kI, kD, kF);

        addRequirements(gyro);
        addRequirements(drive);
    }
    @Override
    public void initialize() {
        System.out.println("initialized");
        double currentAngle = gyro.getGyroAngle();
        newAngle = currentAngle + turnAngle;
        drive.setBrakeMode();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
                double speed = (newAngle - gyro.getGyroAngle())*(0.0027) *.75;
                if (speed /Math.abs(speed) == -1 ){
                    speed = speed;
                }
                else if( speed < .05){
                    speed = .05;
                }
                drive.arcadeDrive(0, speed);
        }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if(Math.abs(gyro.getGyroAngle() - newAngle) > 5)
        {
            return false;
        }
        else{
            System.out.println("complete");
            return true;
        }
      
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
     
    }

}




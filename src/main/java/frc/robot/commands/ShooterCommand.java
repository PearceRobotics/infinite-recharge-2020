/*----------------------------------------------------------------------------*/
/* Copyright (c) 2020 1745 JJ Pearce Robotics. All Rights Reserved.           */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.ShooterSpeedController;

public class ShooterCommand extends CommandBase
{
    
   ShooterSpeedController speedController;

    //Default Constructor.  Set initial launch speed to 0 inches/second.
    public ShooterCommand(ShooterSpeedController speedController) 
    {
        this.speedController = speedController;
    }

        // Called just before this Command runs the first time
        @Override
        public void initialize() {
            System.out.println("Initialized");
    
        }
    
        // Called repeatedly when this Command is scheduled to run
        @Override
        public void execute() {
            System.out.println("executing");
            if(speedController.getCurrentSpeed() < speedController.getLaunchSpeed())
        {
            //If speed is too low, set controllers to maximum speed
            this.speedController.setFullForward();
        }
        else
        {            
            //If speed is too high, set controllers to 0
            this.speedController.setFullReverse();
        }

        }
}
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climbingCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

/**
 * Add your docs here.
 */
public class WinchCommand extends CommandBase {

    private Climber climber;
    private double startTime; 
    private double currentTime;

    //constants
    private final double WINCH_SPEED = 0.5;

    public WinchCommand(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        this.startTime = Timer.getFPGATimestamp();  
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        currentTime = Timer.getFPGATimestamp() - startTime;
        climber.setWinchSpeed(WINCH_SPEED);
    }

    @Override
    public boolean isFinished() {
        if(currentTime >= 2){
            return true;
        }
        else{
            return false;
        }
    }
}

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
public class ElevatorUpCommand extends CommandBase {

    private Climber climber;
    private double startTime = 0.0;
    private final double delayTime = 1.0;

    public ElevatorUpCommand(Climber climber) {
        this.climber = climber;

        addRequirements(climber);
    }

    @Override
    public void initialize() {        
        this.climber.setElevatorSpeed(-0.4);
        startTime = Timer.getFPGATimestamp();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
        //do nothing
    }

    @Override
    public boolean isFinished() {
        return(this.climber.isElevatorAtSetPoint() && (Timer.getFPGATimestamp() - startTime) > delayTime);
    }

    @Override
    public void end(final boolean interrupted) {
    this.climber.setElevatorSpeed(0.0);

    }
}

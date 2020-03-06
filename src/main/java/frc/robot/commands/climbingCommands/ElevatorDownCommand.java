/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.climbingCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Climber;

/**
 * Add your docs here.
 */
public class ElevatorDownCommand extends CommandBase {

    private Climber climber;

    public ElevatorDownCommand(Climber climber) {
        this.climber = climber;

        addRequirements(climber);
    }

    @Override
    public void initialize() {
        
        this.climber.setElevatorSpeed(0.4);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return this.climber.isElevatorAtSetPoint();
    }
    

    @Override
    public void end(final boolean interrupted) {
    this.climber.setElevatorSpeed(0.0);

    }
}

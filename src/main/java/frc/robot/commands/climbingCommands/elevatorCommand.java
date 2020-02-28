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
public class elevatorCommand extends CommandBase{

    private Climber climber;
    private double position;

    public elevatorCommand(Climber climber, Double position){
        this.climber = climber;
        this.position = position;

        addRequirements(climber);
    }
    @Override
    public void initialize() {
        climber.setClimberPIDSetpoint(position);
        climber.setClimberPIDTolerance();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
    climber.setElevatorSpeed(position);
    }
}



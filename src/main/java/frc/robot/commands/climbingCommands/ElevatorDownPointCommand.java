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
public class ElevatorDownPointCommand extends CommandBase {

    private Climber climber;
    private double startTime;
    private final double ALLOWED_RUN_TIME = 3.0;

    public ElevatorDownPointCommand(Climber climber) {
        this.climber = climber;

        addRequirements(climber);
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void initialize() {
        this.climber.gotoElevatorDownpoint();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return this.climber.isElevatorAtSetPoint() || (Timer.getFPGATimestamp() - startTime) > ALLOWED_RUN_TIME;
    }
}

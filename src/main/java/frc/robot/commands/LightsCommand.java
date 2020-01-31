package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Lights;

public class LightsCommand extends CommandBase {
    private Lights lights;

    public LightsCommand(Lights lights) {
        this.lights = lights;
    }

    @Override
    public void execute() {
        lights.idleAnimation(2);
    }
}
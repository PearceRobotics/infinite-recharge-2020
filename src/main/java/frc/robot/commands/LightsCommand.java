package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Lights;

public class LightsCommand extends CommandBase {
    private Lights lights;
    private boolean setWithinDistance;

    public LightsCommand(Lights lights) {
        this.lights = lights;
    }

    @Override
    public void execute() {
        if (this.setWithinDistance) {
            lights.allLimeGreen();
        } else {
            lights.idleAnimation(3);
        }
    }

    public void setWithinDistance(boolean setWthinDistance) {
        this.setWithinDistance = setWthinDistance;
    }
}
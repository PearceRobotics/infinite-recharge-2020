package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Lights;
import frc.robot.subsystems.vision.Limelight;

public class LightsCommand extends CommandBase {
    private Lights lights;
    private Limelight limelight;
    boolean isIdle = false;

    public LightsCommand(Lights lights, Limelight limelight) {
        this.lights = lights;
        this.limelight = limelight;
    }

    @Override
    public void execute() {
        if (limelight.hasValidTarget() && !isIdle) {
            lights.allLimeGreen();
            isIdle = true;
        } else if(!limelight.hasValidTarget() && isIdle){
            lights.idleAnimation(3);
            isIdle = false;
        }
    }
}
package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Lights;
import frc.robot.subsystems.vision.Limelight;

public class LightsCommand extends CommandBase {
    private Lights lights;
    private Limelight limelight;
    private final double DEADZONE = 27;

    public LightsCommand(Lights lights, Limelight limelight) {
        this.lights = lights;
        this.limelight = limelight;
    }

    @Override
    public void execute() {
        if (limelight.hasValidTarget()) {
            if(Math.abs(limelight.getHorizontalTargetOffset()) < DEADZONE){
                lights.allLimeGreen();
            }else{
                lights.idleAnimation(3);
            }
        } else {
            lights.idleAnimation(3);
        }
    }
}
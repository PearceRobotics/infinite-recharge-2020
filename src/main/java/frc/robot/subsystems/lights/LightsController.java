package frc.robot.subsystems.lights;

import frc.robot.subsystems.vision.Limelight;

public class LightsController {
    private Lights lights;
    private Limelight limelight;
    boolean isIdle = false;

    public LightsController(Lights lights, Limelight limelight) {
        this.lights = lights;
        this.limelight = limelight;
    }

    public void checkTargetLock() {
        if (limelight.hasValidTarget() && !isIdle) {
            lights.allLimeGreen();
            isIdle = true;
        } else if(!limelight.hasValidTarget() && isIdle){
            lights.idleAnimation(3);
            isIdle = false;
        }
    }
}
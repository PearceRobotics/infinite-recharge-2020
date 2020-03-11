package frc.robot.subsystems.lights;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.vision.Limelight;

public class LightsController extends SubsystemBase {
    private Lights lights;
    private Limelight limelight;
    boolean isIdle = true;

    public LightsController(Lights lights, Limelight limelight) {
        this.lights = lights;
        this.limelight = limelight;
    }

    public void checkTargetLock() {
        if (limelight.hasValidTarget() && !isIdle && !(isAutonOn()) ) {
            lights.allLimeGreen();
            isIdle = true;
        } else if(!limelight.hasValidTarget() && isIdle && !(isAutonOn())){
            lights.idleAnimation(3, lights.getBlue(), lights.getRed());
            isIdle = false;
        }
        else if(isAutonOn()){
            lights.idleAnimation(3, lights.getGreen(), lights.getYellow());
        }
    }

    public boolean isAutonOn(){
    return lights.isAutonOn();
    }
}
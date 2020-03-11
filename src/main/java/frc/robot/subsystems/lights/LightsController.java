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
        this.lights.idleAnimation(3);
    }

    public void checkTargetLock() {
        if (limelight.hasValidTarget() && !isIdle && !(isAutonOn()) ) {
            lights.allLimeGreen();
            isIdle = true;
        } else if(!limelight.hasValidTarget() && isIdle && !(isAutonOn())){
            lights.idleAnimation(3);
            isIdle = false;
        }
        else if(isAutonOn()){
            lights.idleAutonDriveAnimation(3);
        }
    }

    public boolean isAutonOn(){
    return lights.isAutonOn();
    }
}
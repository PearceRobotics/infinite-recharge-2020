package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax.IdleMode;

public class Gearbox {
    private final CANSparkMax frontController;
    private final CANSparkMax backController;

    public Gearbox(final CANSparkMax frontController, final CANSparkMax backController) {
        this.frontController = frontController;
        this.backController = backController;
        this.setLeaderToFront();
    }

    private void setLeaderToFront() {
        final CANError backSlave = this.backController.follow(this.frontController);

        if (backSlave != CANError.kOk) {
            throw new IllegalStateException(
                    "Unsuccessful in setting leader, BackSlave error status: " + backSlave.name());
        }
    }

    public void setSpeed(double rate) {
        if (rate < -1.0) {
            rate = -1.0;
        } else if (rate > 1.0) {
            rate = 1.0;
        }
        this.frontController.set(rate);
    }

    public void setRampRate(final double rate) {
        this.frontController.setOpenLoopRampRate(rate);
    }

    public CANSparkMax getBackController() {
        return this.backController;
    }

    public CANSparkMax getFrontController() {
        return this.frontController;
    }

    public void setBrakeMode() {
        this.frontController.setIdleMode(IdleMode.kBrake);
    }

    public void setCoastMode() {
        this.frontController.setIdleMode(IdleMode.kCoast);
    }
}


/*What this needs to do 
 Drive Straight
 Turn 90 Left on button push
 Turn 90 Right on button push
 Turn 180 Left on button push
 Turn 180 Right on button push
 Re-orient to field*/
package frc.robot.subsystems.drive;

import frc.robot.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.annotations.Config;
import com.kauailabs.navx.frc.AHRS;

/**
 * Add your docs here.
 */
public class Gyroscope extends SubsystemBase {


    private Drive drive; 
    AHRS imu = new AHRS(SPI.Port.kMXP);

    boolean drivingStraight = false;
    double pValue;

    public Gyroscope(Drive drive){
        this.drive = drive;
    }

    //Angle does not reset to 0 after full rotation - goes to 361...2... etc
    public void resetGyro(){
        imu.reset();
    }
    public double getGyroAngle(){
        return imu.getYaw();
    }

    public void rotate(double degrees, double speed){ //might want to add speed controller for turning
        double currentAngle = getGyroAngle();
        double newAngle = currentAngle + degrees;

        if( degrees/ Math.abs(degrees) == 1){
            speed = speed; //speed stays positive
        }
        else{
        speed = -speed; //speed is negative
        }
        drive.setBrakeMode();
        while(getGyroAngle() < newAngle){
            drive.arcadeDrive(-0, -speed); //b/c joysticks come out negative, we can either change the arcade method, or just make all things plugged in negative
        }
    }

    public void driveStraightGyro(double forwardSpeed,double desiredAngle){ 
        double error = (desiredAngle-getGyroAngle()) * .0014; 
        drive.arcadeDrive(forwardSpeed, error);
    }

    public void backToStartingAngle(double speed){
        double currentAngle = getGyroAngle();
        double turnAngle = currentAngle % 360;
        if( turnAngle/ Math.abs(turnAngle) == 1){
            speed = speed; //speed stays positive
        }
        else{
        speed = -speed; //speed is negative
        }
        drive.setBrakeMode();
        while(Math.abs(getGyroAngle()) < Math.abs(currentAngle)){
            drive.arcadeDrive(-0, -speed); //b/c joysticks come out negative, we can either change the arcade method, or just make all things plugged in negative
        }
    }
@Config
    public void setPValue(double pValue){
        this.pValue = pValue;
    }

}

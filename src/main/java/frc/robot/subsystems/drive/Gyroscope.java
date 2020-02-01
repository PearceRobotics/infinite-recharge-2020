
/*What this needs to do 
 Drive Straight
 Turn 90 Left on button push
 Turn 90 Right on button push
 Turn 180 Left on button push
 Turn 180 Right on button push
 Re-orient to field*/
package frc.robot.subsystems.drive;

import frc.robot.subsystems.drive.Drive;
import com.analog.adis16470.frc.ADIS16470_IMU;
//import com.analog.adis16470.frc.ADIS16470_IMU.Axis;

/**
 * Add your docs here.
 */
public class Gyroscope {


    private Drive drive;
    public static final ADIS16470_IMU imu = new ADIS16470_IMU();

    public Gyroscope(Drive drive){
        this.drive = drive;
    }

    public void gyroCalibrate(){
        imu.calibrate();
    }

    public void resetGyroAngle(){
        imu.reset();
    }

    //Angle does not reset to 0 after full rotation - goes to 361...2... etc
    public double getGyroAngle(){
        return imu.getAngle();
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
        while(getGyroAngle() < newAngle){
            drive.arcadeDrive(-0, -speed); //b/c joysticks come out negative, we can either change the arcade method, or just make all things plugged in negative
        }
    }

    public void driveStraightGyro(){

    }
    
 
}

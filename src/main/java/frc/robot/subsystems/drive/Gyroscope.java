package frc.robot.subsystems.drive;

import frc.robot.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.lang.Math;
import com.analog.adis16470.frc.ADIS16470_IMU;
import com.analog.adis16470.frc.ADIS16470_IMU.IMUAxis;

public class Gyroscope extends SubsystemBase {

    private Drive drive; 
    private ADIS16470_IMU imu;

    boolean drivingStraight = false;
    double pValue;
    double startTime;
    double driftPerSecond;
    
    public Gyroscope(Drive drive){
        this.imu = new ADIS16470_IMU();
        this.drive = drive;
        imu.setYawAxis(IMUAxis.kY);
        gyroCalibrate();
    }

    public double getGyroAngle(){
        double runTime = Timer.getFPGATimestamp() - startTime;
        double drift = runTime * driftPerSecond;
        return imu.getAngle() - drift;
    }

    public void resetGyro(){
        imu.reset();
        startTime = Timer.getFPGATimestamp();
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

    public void gyroCalibrate(){
        startTime = Timer.getFPGATimestamp();        
        double startAngle = imu.getAngle();
        try{
            Thread.sleep(16000);
        }catch(Exception e)
        {
            //dont care about exceptions rn
        }
        driftPerSecond = (imu.getAngle() - startAngle)/(Timer.getFPGATimestamp() - startTime);
    }
}

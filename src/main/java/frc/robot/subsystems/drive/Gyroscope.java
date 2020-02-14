package frc.robot.subsystems.drive;

import frc.robot.subsystems.drive.Drive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.lang.Math;
import com.analog.adis16470.frc.ADIS16470_IMU;
import com.analog.adis16470.frc.ADIS16470_IMU.IMUAxis;

public class Gyroscope extends SubsystemBase {
    //Classes
    private Drive drive; 
    private ADIS16470_IMU imu;
    //variables
    private double startTime;
    private double driftPerSecond;
    //constants
    private double pValue = .0014;
    
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
        double error = (desiredAngle-getGyroAngle()) * pValue; 
        drive.arcadeDrive(forwardSpeed, error);
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
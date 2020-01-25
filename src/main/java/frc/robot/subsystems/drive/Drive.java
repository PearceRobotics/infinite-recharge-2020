/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Encoder;

/**
 * Add your docs here.
 */
public class Drive {

    public Encoder leftEncoder;
    public Encoder rightEncoder;

    private Gearbox leftGearbox;
    private Gearbox rightGearbox;
    //left gear box CAN ids
    private final int LEFT_BACK_CAN_ID = 6;
    private final int LEFT_MIDDLE_CAN_ID = 13;
    private final int LEFT_FRONT_CAN_ID = 5;
    //right gear box CAN ids
    private final int RIGHT_BACK_CAN_ID = 7;
    private final int RIGHT_MIDDLE_CAN_ID = 8;
    private final int RIGHT_FRONT_CAN_ID = 10;

    private MotorType DRIVE_MOTOR_TYPE = MotorType.kBrushless;
    public Drive() {
        this.leftGearbox = new Gearbox(new CANSparkMax(LEFT_BACK_CAN_ID, DRIVE_MOTOR_TYPE), 
                                        new CANSparkMax(LEFT_MIDDLE_CAN_ID, DRIVE_MOTOR_TYPE), 
                                        new CANSparkMax(LEFT_FRONT_CAN_ID, DRIVE_MOTOR_TYPE));
    
        this.rightGearbox = new Gearbox(new CANSparkMax(RIGHT_BACK_CAN_ID, DRIVE_MOTOR_TYPE), 
                                        new CANSparkMax(RIGHT_MIDDLE_CAN_ID, DRIVE_MOTOR_TYPE), 
                                        new CANSparkMax(RIGHT_FRONT_CAN_ID, DRIVE_MOTOR_TYPE));
        this.leftGearbox.setRampRate(1);
        this.rightGearbox.setRampRate(1);
        

    this.leftEncoder = new Encoder(0, 1);
    this.rightEncoder = new Encoder(4, 5);

    leftEncoder.setDistancePerPulse((6.00*Math.PI)/2048.0);
    leftEncoder.setReverseDirection(true);
    rightEncoder.setDistancePerPulse((6.00*Math.PI)/2048.0);
      }
    
      public void setLeftSpeed(double speed) {
        this.leftGearbox.setSpeed(speed);
      }
    
      public void setRightSpeed(double speed) {
        this.rightGearbox.setSpeed(speed);
      }
    
      public void arcadeDrive(double staightSpeed, double turnModifer) {
        this.setLeftSpeed(-(staightSpeed - turnModifer));
        this.setRightSpeed(staightSpeed + turnModifer);
      }
    
      public void arcadeDrive(DrivingDeltas drivingDeltas) {
        arcadeDrive(drivingDeltas.getForwardPower(), drivingDeltas.getSteeringPower());
      }

      public void resetEncoders(){
        leftEncoder.reset();
        rightEncoder.reset();
      }

      public void driveStraight(double distance, double maxSpeed, double constant){
        leftGearbox.setBrakeMode();
        rightGearbox.setBrakeMode();
        resetEncoders();
        while(leftEncoder.getDistance() < distance && rightEncoder.getDistance() < distance) {
          System.out.println("left distance " + leftEncoder.getDistance());
          System.out.println("right distance " + rightEncoder.getDistance());
        double error = leftEncoder.getDistance() - rightEncoder.getDistance();
        double turnPower =  error * .15;
        double fowardSpeed = maxSpeed - ((leftEncoder.getDistance()/distance)*maxSpeed);
        
        System.out.println("speed " + fowardSpeed);
         arcadeDrive(-fowardSpeed, -turnPower);
         } 
        setRightSpeed(0);
        setLeftSpeed(0);

       // leftGearbox.setCoastMode();
       // rightGearbox.setCoastMode();

      }


  
}

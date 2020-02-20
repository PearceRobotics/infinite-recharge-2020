package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {

  private Encoder leftEncoder;
  private Encoder rightEncoder;
  private Gearbox leftGearbox;
  private Gearbox rightGearbox;

  private Gyroscope gyroscope;

  //variables
  private double desiredAngle;

  //constants
  private final double P_VALUE = .0014;
  // left gear box CAN ids
  private final int LEFT_BACK_CAN_ID = 11;
  // private final int LEFT_MIDDLE_CAN_ID = 13;
  private final int LEFT_FRONT_CAN_ID = 12;
  // right gear box CAN ids
  private final int RIGHT_BACK_CAN_ID = 4;
  // private final int RIGHT_MIDDLE_CAN_ID = 6;
  private final int RIGHT_FRONT_CAN_ID = 5;

  boolean brakeMode = false;

  private MotorType DRIVE_MOTOR_TYPE = MotorType.kBrushless;

  public Drive(Gyroscope gyroscope) {
    this.gyroscope = gyroscope;
    this.leftGearbox = new Gearbox(new CANSparkMax(LEFT_BACK_CAN_ID, DRIVE_MOTOR_TYPE),
        new CANSparkMax(LEFT_FRONT_CAN_ID, DRIVE_MOTOR_TYPE));

    this.rightGearbox = new Gearbox(new CANSparkMax(RIGHT_BACK_CAN_ID, DRIVE_MOTOR_TYPE),
        new CANSparkMax(RIGHT_FRONT_CAN_ID, DRIVE_MOTOR_TYPE));
    this.leftGearbox.setRampRate(1);
    this.rightGearbox.setRampRate(1);

    this.leftEncoder = new Encoder(0, 1);
    this.rightEncoder = new Encoder(4, 5);

    leftEncoder.setDistancePerPulse((6.0 * Math.PI) / 2048.0);
    leftEncoder.setReverseDirection(true);
    rightEncoder.setDistancePerPulse((6.0 * Math.PI) / 2048.0);
  }

  public void setLeftSpeed(double speed) {
    this.leftGearbox.setSpeed(speed);
  }

  public void setRightSpeed(double speed) {
    this.rightGearbox.setSpeed(speed);
  }

  public void arcadeDrive(double throttle, double turnModifer) {
    if(turnModifer == 0.0 && throttle != 0.0 ) {
      if(desiredAngle == Integer.MAX_VALUE) {
        desiredAngle = gyroscope.getGyroAngle();
      }
      turnModifer = driveStraightGyro(desiredAngle); 
    }
    else {
      desiredAngle = Integer.MAX_VALUE;
    }
    this.setLeftSpeed(-(throttle - turnModifer));
    this.setRightSpeed(throttle + turnModifer);
  }

  public void arcadeDrive(DrivingDeltas drivingDeltas) {
    arcadeDrive(drivingDeltas.getForwardPower(), drivingDeltas.getSteeringPower());
  }

  public void resetEncoders() {
    leftEncoder.reset();
    rightEncoder.reset();
  }

  public void setBrakeMode(){
    if(brakeMode == false){
    leftGearbox.setBrakeMode();
    rightGearbox.setBrakeMode();
    brakeMode = true;
    }
    else{
      //do nothing
    }
  }

  public void setCoastMode(){
    if(brakeMode == true){
    leftGearbox.setCoastMode();
    rightGearbox.setCoastMode();
    brakeMode = false;
    }
    else{
      //do nothing
    }
  }

  public double getLeftEncoderDistance(){
    double leftEncoderDistance =leftEncoder.getDistance();
    return leftEncoderDistance;
  }

  public double getRightEncoderDistance(){
    double rightEncoderDistance = leftEncoder.getDistance();
    return rightEncoderDistance;
  }

  public double straightTurnPower(){
    double error = getLeftEncoderDistance() - getRightEncoderDistance();
    double turnPower = error * .15;
    return turnPower;
  }

  public double driveStraightGyro(double desiredAngle){  
    return  (desiredAngle - gyroscope.getGyroAngle()) * P_VALUE;
  }
}
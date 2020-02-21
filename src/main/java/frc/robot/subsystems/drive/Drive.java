package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
 
public class Drive extends SubsystemBase{

  private Encoder leftEncoder;
  private Encoder rightEncoder;
  private Gearbox leftGearbox;
  private Gearbox rightGearbox;
  private SpeedControllerGroup leftGearboxController;
  private SpeedControllerGroup rightGearboxController;

  private Gyroscope gyroscope;
  private DifferentialDrive differentialDrive;

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
  
  private MotorType DRIVE_MOTOR_TYPE = MotorType.kBrushless;

  public Drive(Gyroscope gyroscope) {
    leftGearboxController = new SpeedControllerGroup(new CANSparkMax(LEFT_BACK_CAN_ID, DRIVE_MOTOR_TYPE),new CANSparkMax(LEFT_FRONT_CAN_ID, DRIVE_MOTOR_TYPE));
    rightGearboxController = new SpeedControllerGroup(new CANSparkMax(RIGHT_BACK_CAN_ID, DRIVE_MOTOR_TYPE),  new CANSparkMax(RIGHT_FRONT_CAN_ID, DRIVE_MOTOR_TYPE));
    
    this.gyroscope = gyroscope;
    this.leftGearbox = new Gearbox(new CANSparkMax(LEFT_BACK_CAN_ID, DRIVE_MOTOR_TYPE),
        new CANSparkMax(LEFT_FRONT_CAN_ID, DRIVE_MOTOR_TYPE),
        leftGearboxController);

    this.rightGearbox = new Gearbox(new CANSparkMax(RIGHT_BACK_CAN_ID, DRIVE_MOTOR_TYPE),
        new CANSparkMax(RIGHT_FRONT_CAN_ID, DRIVE_MOTOR_TYPE),
        rightGearboxController);

    differentialDrive = new DifferentialDrive(leftGearboxController, rightGearboxController);

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

  public void curvatureDrive(double throttle, double curvature, boolean isQuickTurn) {
    differentialDrive.curvatureDrive(throttle, curvature, isQuickTurn);
  }

  public void arcadeDrive(double throttle, double turnModifer) {
    if(turnModifer == 0.0 && throttle != 0.0) {
      if(this.desiredAngle == Integer.MAX_VALUE) {
        this.desiredAngle = gyroscope.getGyroAngle();
      }
      turnModifer = this.getAngularError(desiredAngle); 
    }
    else {
      this.desiredAngle = Integer.MAX_VALUE;
    }
    this.setLeftSpeed(-(throttle - turnModifer));
    this.setRightSpeed(throttle + turnModifer);
  }

  public double getAngularError(double desiredAngle) {  
    return  (desiredAngle - gyroscope.getGyroAngle()) * P_VALUE;
  }

  public void arcadeDrive(DrivingDeltas drivingDeltas) {
    arcadeDrive(drivingDeltas.getForwardPower(), drivingDeltas.getSteeringPower());
  }

  public void resetEncoders() {
    leftEncoder.reset();
    rightEncoder.reset();
  }

  public void setBrakeMode(){
    leftGearbox.setBrakeMode();
    rightGearbox.setBrakeMode();
  }

  public void setCoastMode() {
    leftGearbox.setCoastMode();
    rightGearbox.setCoastMode();
  }

  public double getLeftEncoderDistance() {
    double leftEncoderDistance =leftEncoder.getDistance();
    return leftEncoderDistance;
  }

  public double getRightEncoderDistance() {
    double rightEncoderDistance = leftEncoder.getDistance();
    return rightEncoderDistance;
  }

  public double straightTurnPower(double pValue) {
    double error = getLeftEncoderDistance() - getRightEncoderDistance();
    double turnPower = error * pValue;
    return turnPower;
  }
}
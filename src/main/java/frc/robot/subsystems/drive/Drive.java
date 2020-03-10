package frc.robot.subsystems.drive;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {

  private Encoder leftEncoder;
  private Encoder rightEncoder;
  private Gearbox leftGearbox;
  private Gearbox rightGearbox;
  private Gyroscope gyroscope;
  private DifferentialDrive differentialDrive;

  //variables
  private double desiredAngle;

  //constants
  private final double P_VALUE = .0025;
  private final double RAMP_RATE = 1;
  private final double QUICK_TURN_THROTTLE_DEADZONE = 0.1;

  /*Kinematics start*/
  private final double ksVolts = 0.22;
  private final double kvVoltSecondsPerMeter = 1.98;
  private final double kaVoltSecondsSquaredPerMeter = 0.2;
  private final double kPDriveVel = 8.5;
  private final double kTrackwidthMeters = 0.667385;
  private final double kMaxSpeedMetersPerSecond = 3;
  private final double kMaxAccelerationMetersPerSecondSquared = 3;
  // Reasonable baseline values for a RAMSETE follower in units of meters and seconds
  private final double kRamseteB = 2;
  private final double kRamseteZeta = 0.7;

  private final DifferentialDriveKinematics kDriveKinematics;
  private final DifferentialDriveOdometry m_odometry;
  /* Kinematics End */

  // left gear box CAN ids
  private final int LEFT_BACK_CAN_ID = 11; 
  private final int LEFT_FRONT_CAN_ID = 12;
  private final int LEFT_MIDDLE_CAN_ID = 13;
  // right gear box CAN ids
  private final int RIGHT_BACK_CAN_ID = 4;
  private final int RIGHT_FRONT_CAN_ID = 5;
  private final int RIGHT_MIDDLE_CAN_ID = 6;

  private MotorType DRIVE_MOTOR_TYPE = MotorType.kBrushless;

  private boolean gyroDisabled = false;

  public Drive(Gyroscope gyroscope) {
    this.leftGearbox = new Gearbox(new CANSparkMax(LEFT_BACK_CAN_ID, DRIVE_MOTOR_TYPE),
        new CANSparkMax(LEFT_FRONT_CAN_ID, DRIVE_MOTOR_TYPE), new CANSparkMax(LEFT_MIDDLE_CAN_ID, DRIVE_MOTOR_TYPE));

    this.rightGearbox = new Gearbox(new CANSparkMax(RIGHT_BACK_CAN_ID, DRIVE_MOTOR_TYPE),
        new CANSparkMax(RIGHT_FRONT_CAN_ID, DRIVE_MOTOR_TYPE), new CANSparkMax(RIGHT_MIDDLE_CAN_ID, DRIVE_MOTOR_TYPE));

    this.leftGearbox.setRampRate(RAMP_RATE);
    this.rightGearbox.setRampRate(RAMP_RATE);
    
    this.differentialDrive = new DifferentialDrive(this.leftGearbox.getSpeedControllerGroup(), 
                                              this.rightGearbox.getSpeedControllerGroup());

    this.gyroscope = gyroscope;

    this.leftEncoder = new Encoder(0, 1);
    this.rightEncoder = new Encoder(2, 3);

    this.leftEncoder.setDistancePerPulse((6.0 * Math.PI) / 2048.0);
    this.leftEncoder.setReverseDirection(true);
    this.rightEncoder.setDistancePerPulse((6.0 * Math.PI) / 2048.0);

    this.kDriveKinematics = new DifferentialDriveKinematics(kTrackwidthMeters);
    this.m_odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
  }

  @Override
  public void periodic() {
    // Update the odometry in the periodic block
    m_odometry.update(Rotation2d.fromDegrees(getHeading()), leftEncoder.getDistance(),
                      rightEncoder.getDistance());
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getHeading() {
    return Math.IEEEremainder(gyroscope.getGyroAngle(), 360);
  }


  private void setLeftSpeed(double speed) {
    this.leftGearbox.setSpeed(speed);
  }

  private void setRightSpeed(double speed) {
    this.rightGearbox.setSpeed(speed);
  }

  public void arcadeDrive(double throttle, double turnModifier){
    differentialDrive.arcadeDrive(throttle, turnModifier);
  }

  public void curvatureDrive(double throttle, double curvature) {
    if(throttle != 0.0 && curvature == 0.0 && !gyroDisabled) { //driving straight and no turn
      if(this.desiredAngle == Integer.MAX_VALUE) { //means robot just started driving straight
        this.desiredAngle = gyroscope.getGyroAngle(); 
      }
      curvature = this.getAngularError(desiredAngle); 
      this.setLeftSpeed(-(throttle - curvature));
      this.setRightSpeed(throttle + curvature);
    }
    else { // when robot isn't driving straight
      this.desiredAngle = Integer.MAX_VALUE; //if turn is greater than 0 or if robot is still
      differentialDrive.curvatureDrive(-throttle, curvature, (Math.abs(throttle) < QUICK_TURN_THROTTLE_DEADZONE));
    }
  }

  public double getAngularError(double desiredAngle) {  
    return  (gyroscope.getGyroAngle() - desiredAngle) * P_VALUE;
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

  public void gyroDisabled(boolean gyroDisabled) {
    this.gyroDisabled = gyroDisabled;
  }
}
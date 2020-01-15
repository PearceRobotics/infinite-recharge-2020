/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private final double pi = 3.141592;
  private CANSparkMax left;
  private CANSparkMax right;
  private Encoder shaftEncoder;
  private double wheelVelocity = 0.0;
  private Controls controls;
  private final int JOYSTICK_PORT = 1;
  private boolean isRecording = false;
  private double aButtonStart = 0;
  private double aButtonEnd = 0;
  private double now;
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    left = new CANSparkMax(4, MotorType.kBrushless);
    right = new CANSparkMax(5, MotorType.kBrushless);
    shaftEncoder = new Encoder(0, 1);
    controls = new Controls(new Joystick(JOYSTICK_PORT));

    shaftEncoder.setDistancePerPulse((4 * pi) / 2048);
    shaftEncoder.setReverseDirection(true);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
    case kCustomAuto:
      // Put custom auto code here
      break;
    case kDefaultAuto:
    default:
      // Put default auto code here
      break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    double motorSpeed = 0.1;
    left.set(-motorSpeed);
    right.set(motorSpeed);

    wheelVelocity = shaftEncoder.getRate();

    double rps = wheelVelocity / (4 * pi);
    double rpm = rps * 60;
   /* System.out.println("Wheel Velocity (in/s): " + wheelVelocity);
    System.out.println("rpm: " + rpm);
    System.out.println("left motor rpm: " + left.getEncoder().getVelocity());
    System.out.println("right motor rpm: " + right.getEncoder().getVelocity());
    */
    if(controls.getAButton() == true && !isRecording && ((now - aButtonEnd) > 0.5))
    {
      isRecording = true;  
      aButtonStart = now;
      System.out.println("Recording now " + aButtonStart);
    }
    now = Timer.getFPGATimestamp();
    if(controls.getAButton() && isRecording && ((now - aButtonStart) > 0.5))
    {
      aButtonEnd = now;
      System.out.println("Done Recording " + now) ;
      isRecording = false;

    }
    
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}

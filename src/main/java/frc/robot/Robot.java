/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.drive.Drive;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.TeleopCommand;
import frc.robot.io.Controls;

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

  private Drive drive;
  private Controls controls;
  private AutonomousCommand autonomousCommand;
  private TeleopCommand teleopCommand;

  // Constants
  private final int JOYSTICK_PORT = 1;

  private double maxSpeed;
  private double distance;
  private double constant;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    Logger.configureLoggingAndConfig(this, false);

    this.drive = new Drive();
    this.controls = new Controls(new Joystick(JOYSTICK_PORT));
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
    Logger.updateEntries();
    CommandScheduler.getInstance().run();
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

    setDistance(distance);
    setMaxSpeed(maxSpeed);
    setConstant(constant);
    this.autonomousCommand = new AutonomousCommand(distance, maxSpeed, drive);
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
    if (autonomousCommand != null)
      autonomousCommand.schedule();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();
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
  @Override
 public void teleopInit(){
  this.teleopCommand = new TeleopCommand(controls, drive);
  if (teleopCommand != null)
    teleopCommand.schedule();
}
  // does nothing 
 
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    CommandScheduler.getInstance().run();
   // manualControl();

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  @Config(defaultValueNumeric = 36)
  public void setDistance(double distance) {
    this.distance = distance;
  }

  @Config(defaultValueNumeric = .75)
  public void setMaxSpeed(double maxSpeed) {
    this.maxSpeed = maxSpeed;
  }

  @Config(defaultValueNumeric = 1.2)
  public void setConstant(double constant) {
    this.constant = constant;
  }
}

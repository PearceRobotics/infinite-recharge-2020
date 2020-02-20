package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Lights;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.Gyroscope;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.LightsCommand;
import frc.robot.commands.TeleopCommand;
import frc.robot.io.Controls;
import frc.robot.io.IO;

public class Robot extends TimedRobot {

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Drive drive;
  private Controls controls;
  private Lights lights;
  private Gyroscope gyro;
  private IO io;
  
  private AutonomousCommand autonomousCommand;
  private TeleopCommand teleopCommand;
  private LightsCommand lightsCommand;


  // Constants
  private final int JOYSTICK_PORT = 1;

  private double pValue = 0.2;

  private double maxSpeed;
  private double distance;
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

    this.gyro = new Gyroscope();
    this.drive = new Drive(this.gyro);
    this.controls = new Controls(new Joystick(JOYSTICK_PORT));
    this.lights = new Lights(9, 60, 50);
    this.io = new IO(controls, drive, gyro);

    this.lightsCommand = new LightsCommand(this.lights);
    this.autonomousCommand = new AutonomousCommand(distance, maxSpeed, drive, pValue);
    this.teleopCommand = new TeleopCommand(this.controls, this.drive);
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
    lightsCommand.schedule();
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
    switch(m_autoSelected) {
      case kCustomAuto:
        break;
      case kDefaultAuto:
        default:
          if (autonomousCommand != null) {
            autonomousCommand.schedule();
          }
        break;
    }
  }

  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    teleopCommand.schedule();
  }
 
  @Override
  public void teleopPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void testPeriodic() {
  }

  @Config(tabName = "Autonomous", name = "Distance", defaultValueNumeric = 36)
  public void setAutonStraightDistance(double distance){
    this.distance = distance;
  }

  @Config(tabName = "Autonomous", name = "Maximum Speed", defaultValueNumeric = .75)
  public void setAutonMaxSpeedForDriveStraight(double maxSpeed){
    this.maxSpeed = maxSpeed;
  }

  @Config(name = "Constant", defaultValueNumeric = .1)
  public void setDriveStraightPValue(double pValue){
    this.pValue = pValue;
  } 
}

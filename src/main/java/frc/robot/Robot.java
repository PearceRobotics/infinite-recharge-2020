package frc.robot;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.subsystems.Lights;
import frc.robot.subsystems.drive.Drive;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.LightsCommand;
import frc.robot.commands.TeleopCommand;
import frc.robot.io.Controls;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;



public class Robot extends TimedRobot {
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Drive drive;
  private Controls controls;
  private Lights lights;
  
  private AutonomousCommand autonomousCommand;
  private TeleopCommand teleopCommand;
  private LightsCommand lightsCommand;

  // Constants
  private final int JOYSTICK_PORT = 1;

  private double maxSpeed;
  private double distance;
  private double pValue;
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
    this.lights = new Lights(9, 60, 50);

    this.lightsCommand = new LightsCommand(lights);
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
   
    Color detectedColor = m_colorSensor.getColor();
    double IR = m_colorSensor.getIR();
    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putNumber("IR", IR);
    int proximity = m_colorSensor.getProximity();
    SmartDashboard.putNumber("Proximity", proximity);
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
    this.autonomousCommand = new AutonomousCommand(distance, maxSpeed, drive, pValue);
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

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    this.teleopCommand = new TeleopCommand(controls, drive, pValue);
    if (teleopCommand != null) {
      teleopCommand.schedule();
    }
  }
 
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    CommandScheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
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

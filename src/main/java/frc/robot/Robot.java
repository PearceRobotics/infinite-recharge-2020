package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.ShooterSpeedController;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.TeleopCommand;
import frc.robot.io.Controls;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Drive drive;
  private Controls controls;
  private ShooterCommand shooterCommand;
  private ShooterSpeedController shooterSpeedController;

  private boolean shotRequested = false;

  // Constants
  private final int JOYSTICK_PORT = 1;

  private final double DEADZONE = 0.05;

  private AutonomousCommand autonomousCommand;
  private TeleopCommand teleopCommand;

  @Log
  private double maxSpeed;
  @Log
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
    this.shooterSpeedController = new ShooterSpeedController();

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
    this.autonomousCommand = new AutonomousCommand(distance, maxSpeed, drive);
    m_autoSelected = m_chooser.getSelected();
    System.out.println("Auto selected: " + m_autoSelected);
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
    this.shooterCommand = new ShooterCommand(shooterSpeedController);
    if (shooterCommand != null)
    shooterCommand.schedule();
  

    this.teleopCommand = new TeleopCommand(controls, drive);
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

    if (controls.getRightTrigger() || true) {
      // call shooter.determineLaunchSpeed
      // use it to set the shooterSpeedController
      shooterSpeedController.setLaunchSpeed(1150.0); //using a number that should be replaced

      //Set the bool to know that a shot is requested
      shotRequested = true;
    }

    
    System.out.println("current launch speed " + shooterSpeedController.getLaunchSpeed());
    System.out.println("current shooter speed " + shooterSpeedController.getCurrentSpeed());

    // when firing the shooter, make sure it's at speed
    if (shotRequested && shooterSpeedController.isAtSpeed()) {
      // fire the shooter
      //code to fire shooter
      System.out.println("at speed and firing!");

      //Set the bool to know that the shot was fired
      shotRequested = false;
    }

    // manualControl();

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}

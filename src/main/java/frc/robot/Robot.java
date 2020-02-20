package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.Lights;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterSpeedController;
import frc.robot.commands.ShooterCommand;
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
  private Shooter shooter;
  private Lights lights;
  private ShooterCommand shooterCommand;
  private Gyroscope gyro;
  private IO io;
  
  private AutonomousCommand autonomousCommand;
  private TeleopCommand teleopCommand;
  private LightsCommand lightsCommand;
  private ShooterSpeedController shooterSpeedController;
  private HopperController hopperController;
  private IndexerController indexerController;

  private boolean shotRequested = false;
  private boolean shotInProgress = false;
  private final double BALL_SHOOT_TIME = 1.0;
  private final double INNER_DISTANCE_FROM_TARGET = 29.0;
  private double ballShootStartTime;

  // Constants
  private final int JOYSTICK_PORT = 1;

  private double distanceToGoal = 0.0;
  private double overrideSpeed = 1330.0;
  private double indexerSpeed = 0.3;

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
    this.shooterSpeedController = new ShooterSpeedController();

    Logger.configureLoggingAndConfig(this, false);

    this.gyro = new Gyroscope();
    this.drive = new Drive(this.gyro);
    this.controls = new Controls(new Joystick(JOYSTICK_PORT));
    this.shooter = new Shooter();
    this.lights = new Lights(9, 60, 50);
    this.hopperController = new HopperController();
    this.indexerController = new IndexerController();
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
    switch (m_autoSelected) {
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
    this.shooterCommand = new ShooterCommand(shooterSpeedController);
    if (shooterCommand != null)
      shooterCommand.schedule();
  }

  // Use this for the speed finding algorithm
  @Config
  public void setDistanceToGoal(double distanceToGoal) {
    this.distanceToGoal = distanceToGoal;
  }

  // use this to override the algorithm and just use a speed
  @Config
  public void setOverrideSpeed(double overrideSpeed) {
    this.overrideSpeed = overrideSpeed;
    shooterSpeedController.setLaunchSpeed(this.overrideSpeed);
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    CommandScheduler.getInstance().run();

    if (controls.getLeftTrigger()) {
      indexerController.outtake();
    }
    if (controls.getAButton()) {
      // call shooter.determineLaunchSpeed
      // use it to set the shooterSpeedController
      // TODO Limelight might take inner distance to account, revisit this
      double speed = shooter.determineLaunchSpeed(distanceToGoal + INNER_DISTANCE_FROM_TARGET);
      shooterSpeedController.setLaunchSpeed(speed);

      // Set the bool to know that a shot is requested
      shotRequested = true;
    }

    // Make sure the shooter is at speed before loading a power cell
    if (shotRequested && shooterSpeedController.isAtSpeed() && !shotInProgress) {
      // record the start time so we can turn off the indexer after a period of time
      ballShootStartTime = Timer.getFPGATimestamp();

      // Set the bool to know that the indexer is spinning
      shotInProgress = true;

      // turn on the indexer and hopper
      indexerController.intake();
      hopperController.start();
    }

    // turn off the indexer after a set amount of time
    if (shotInProgress && ((Timer.getFPGATimestamp() - ballShootStartTime) > BALL_SHOOT_TIME)) {
      indexerController.stop();
      hopperController.stop();

      shotInProgress = false;
      shotRequested = false;
    }
  }

  @Override
  public void testPeriodic() {
  }

  @Config(name = "Indexer Speed", defaultValueNumeric = 0.3)
  public void setIndexerSpeed(double indexerSpeed) {
    this.indexerSpeed = indexerSpeed;
    this.indexerController.setSpeed(this.indexerSpeed);
  }

  @Config(tabName = "Autonomous", name = "Distance", defaultValueNumeric = 36)
  public void setAutonStraightDistance(double distance) {
    this.distance = distance;
    this.autonomousCommand.setDistance(this.distance);
  }

  @Config(tabName = "Autonomous", name = "Maximum Speed", defaultValueNumeric = .75)
  public void setAutonMaxSpeedForDriveStraight(double maxSpeed) {
    this.maxSpeed = maxSpeed;
    this.autonomousCommand.setMaxSpeed(this.maxSpeed);
  }

  @Config(name = "Constant", defaultValueNumeric = .1)
  public void setDriveStraightPValue(double pValue) {
    this.pValue = pValue;
    this.autonomousCommand.setPValue(this.pValue);
  }
}

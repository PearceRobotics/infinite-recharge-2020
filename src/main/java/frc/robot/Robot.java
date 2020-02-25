package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.Lights;
import frc.robot.subsystems.vision.DistanceCalculator;
import frc.robot.subsystems.vision.Limelight;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.ShooterSpeedController;
import frc.robot.subsystems.drive.Gyroscope;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.LightsCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.TeleopCommand;
import frc.robot.operatorInputs.Controls;
import frc.robot.operatorInputs.OperatorInputs;

public class Robot extends TimedRobot {

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private static final String kShooterCamera = "Default";
  private static final String kShooterDistance = "Distance";
  private String m_autoSelected;
  private String m_shooterSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final SendableChooser<String> shooter_chooser = new SendableChooser<>();

  private Drive drive;
  private Controls controls;
  private Lights lights;
  private Limelight limelight;
  private Gyroscope gyro;
  private OperatorInputs operatorInputs;
  private AutonomousCommand autonomousCommand;
  private ShooterCommand shooterCommand;
  private TeleopCommand teleopCommand;
  private LightsCommand lightsCommand;
  private ShooterSpeedController shooterSpeedController;
  private HopperController hopperController;
  private IndexerController indexerController;

  // Constants
  private final int JOYSTICK_PORT = 1;

  private double overrideSpeed = 1330.0;
  private double indexerSpeed = 0.3;

  private double pValue = 0.2;
  private double maxSpeed;
  private double distance = 36.0;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    shooter_chooser.setDefaultOption("Shooter Camera", kShooterCamera);
    shooter_chooser.addOption("Shooter Distance", kShooterDistance);
    SmartDashboard.putData("Shooter choices", shooter_chooser);

    Logger.configureLoggingAndConfig(this, false);

    this.gyro = new Gyroscope();
    this.drive = new Drive(this.gyro);
    this.controls = new Controls(new Joystick(JOYSTICK_PORT));
    this.lights = new Lights(9, 60, 50);
    this.limelight = new Limelight();
    this.lightsCommand = new LightsCommand(lights);
    this.shooterSpeedController = new ShooterSpeedController();
    this.hopperController = new HopperController();
    this.indexerController = new IndexerController();
    this.operatorInputs = new OperatorInputs(controls, drive, gyro, shooterSpeedController, hopperController,
        indexerController, limelight);
    this.lightsCommand = new LightsCommand(this.lights);
    this.autonomousCommand = new AutonomousCommand(distance, maxSpeed, drive, pValue);
    this.teleopCommand = new TeleopCommand(this.controls, this.drive);
    this.shooterCommand = new ShooterCommand(shooterSpeedController, hopperController, indexerController, limelight);
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
    System.out.println("dsitance to target " + DistanceCalculator.getDistanceFromTarget(Math.toRadians(limelight.getVerticalTargetOffset())));
    System.out.println("vertical angle " + limelight.getVerticalTargetOffset());
    System.out.println("target area " + limelight.getTargetArea());


    //131.5 == -17.3 degrees
    //235.5 == -6.34
    // 77.5 == 8.2
  m_shooterSelected = shooter_chooser.getSelected();
    switch (m_shooterSelected) {
      case kShooterDistance:
        break;
      case kShooterCamera:
      default:
        if (shooterCommand != null) {
          shooterCommand.schedule();
        }
        break;
    }
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
    if (teleopCommand != null) {
      teleopCommand.schedule();
    }
  }

  // use this to override the algorithm and just use a speed
  @Config
  public void setOverrideSpeed(double overrideSpeed) {
    this.overrideSpeed = overrideSpeed;
    shooterSpeedController.setLaunchSpeed(this.overrideSpeed);
  }

  @Override
  public void teleopPeriodic() {
    CommandScheduler.getInstance().run();
    if (controls.getLeftTrigger()) {
      indexerController.outtake();
    }
  }

  /**
   * This function is called periodically during test mode.
   */
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

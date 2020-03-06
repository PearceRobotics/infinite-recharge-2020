package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.vision.Limelight;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.ShooterSpeedController;
import frc.robot.subsystems.drive.Gyroscope;
import frc.robot.subsystems.lights.Lights;
import frc.robot.subsystems.lights.LightsController;
import frc.robot.commands.AutonomousCommand;
import frc.robot.operatorInputs.Controls;
import frc.robot.operatorInputs.OperatorInputs;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.DistanceSensorDetector;

public class Robot extends TimedRobot {

  //Pick Autonomous
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_autonChooser = new SendableChooser<>();
  //Pick Limelight Pipeline
  private static final String kHighGoal = "High Goal";
  private static final String kLowGoal = "Low Goal";
  private String m_pipelineSelected;
  private final SendableChooser<String> m_pipelineChooser = new SendableChooser<>();

  private Drive drive;
  private Controls controls;
  private Lights lights;
  private Limelight limelight;
  private Gyroscope gyro;
  private Climber climber;
  private OperatorInputs operatorInputs;
  private AutonomousCommand autonomousCommand;
  private LightsController lightsController;
  private ShooterSpeedController shooterSpeedController;
  private HopperController hopperController;
  private IndexerController indexerController;
  private DistanceSensorDetector distanceSensorDetector;

  // Constants
  private final int JOYSTICK_PORT = 1;

  private double overrideSpeed = 1330.0;
  private double indexerSpeed = 0.3;

  private double pValue = 0.2;

  @Log
  private boolean isPowerCellLoaded;

  @Log
  private boolean isLimelightLockedOn;

  private double maxSpeed = 0.75;
  private double distance = 36.0;

  //
  private double elevatorHeight = 19.0; // height for elevator to move to, in inches

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_autonChooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_autonChooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_autonChooser);


    m_autonChooser.setDefaultOption("High Goal", kHighGoal);
    m_autonChooser.addOption("Low Goal", kLowGoal);
    SmartDashboard.putData("LimelightPipeline", m_pipelineChooser);

    Logger.configureLoggingAndConfig(this, false);

    this.gyro = new Gyroscope();
    this.climber = new Climber();
    this.drive = new Drive(this.gyro);
    this.controls = new Controls(new Joystick(JOYSTICK_PORT));
    this.lights = new Lights(9, 82, 50);
    this.limelight = new Limelight();
    this.shooterSpeedController = new ShooterSpeedController();
    this.distanceSensorDetector = new DistanceSensorDetector();
    this.hopperController = new HopperController();
    this.indexerController = new IndexerController();
    this.lightsController = new LightsController(this.lights, this.limelight);
    this.operatorInputs = new OperatorInputs(controls, drive, gyro, shooterSpeedController, hopperController,
        indexerController, limelight, climber, distanceSensorDetector, lightsController);
    this.autonomousCommand = new AutonomousCommand(distance, maxSpeed, this.drive, pValue);
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
    m_autoSelected = m_autonChooser.getSelected();
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
  }

  @Override
  public void teleopInit() {
  }

  // use this to override the algorithm and just use a speed
  @Config(name = "Override Speed", defaultValueNumeric = 1330.0)
  public void setOverrideSpeed(final double overrideSpeed) {
    this.overrideSpeed = overrideSpeed;
    shooterSpeedController.setLaunchSpeed(this.overrideSpeed);
  }
  @Override
  public void teleopPeriodic() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  public boolean isPowerCellLoaded(){
    isPowerCellLoaded = distanceSensorDetector.isPowerCellLoaded();
    return isPowerCellLoaded;
  }

  public boolean isLimelightLockedOn(){
    isLimelightLockedOn = limelight.hasValidTarget();
    return isLimelightLockedOn;
  }

  @Config(name = "Elevator Height", defaultValueNumeric = 19.0)
  public void setElevatorHeightInches(double elevatorHeight) {
    this.elevatorHeight = elevatorHeight;
  }

  @Config(name = "Indexer Speed", defaultValueNumeric = 0.3)
  public void setIndexerSpeed(final double indexerSpeed) {
    this.indexerSpeed = indexerSpeed;
    this.indexerController.setSpeed(this.indexerSpeed);
  }

  @Config(tabName = "Autonomous", name = "Distance", defaultValueNumeric = 36)
  public void setAutonStraightDistance(final double distance) {
    this.distance = distance;
    this.autonomousCommand.setDistance(this.distance);
  }

  @Config(tabName = "Autonomous", name = "Maximum Speed", defaultValueNumeric = .75)
  public void setAutonMaxSpeedForDriveStraight(final double maxSpeed) {
    this.maxSpeed = maxSpeed;
    this.autonomousCommand.setMaxSpeed(this.maxSpeed);
  }

  @Config(name = "Constant", defaultValueNumeric = .1)
  public void setDriveStraightPValue(final double pValue) {
    this.pValue = pValue;
    this.autonomousCommand.setPValue(this.pValue);
  }

  @Config(name = "DISABLE GYRO", defaultValueBoolean = false) 
  private void disableEnableGyro(boolean gyroDisabled) {
    this.drive.gyroDisabled(gyroDisabled);
  }
}
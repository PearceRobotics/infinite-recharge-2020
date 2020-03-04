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
import frc.robot.commands.CurvatureDriveCommand;
import frc.robot.commands.LightsCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.NotStraightArcadeDriveCommand;
import frc.robot.commands.ArcadeDriveCommand;
import frc.robot.operatorInputs.Controls;
import frc.robot.operatorInputs.OperatorInputs;
import frc.robot.subsystems.Climber;

public class Robot extends TimedRobot {

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private static final String kShooterCamera = "Default";
  private static final String kShooterDistance = "Distance";
  private String m_autoSelected;
  private String m_shooterSelected;
  private final SendableChooser<String> shooter_chooser = new SendableChooser<>();
  private final SendableChooser<String> m_autonChooser = new SendableChooser<>();

  private static final String kCurvatureDrive = "Cheesy Boi";
  private static final String kArcadeDrive = "Arcade Boi";
  private static final String kNotStraightArcadeDrive = "UrNotStraight";
  private String m_teleopSelected;
  private final SendableChooser<String> m_teleopChooser = new SendableChooser<>();

  private Drive drive;
  private Controls controls;
  private Lights lights;
  private Limelight limelight;
  private Gyroscope gyro;
  private Climber climber;
  private OperatorInputs operatorInputs;
  private AutonomousCommand autonomousCommand;
  private ShooterCommand shooterCommand;
  private CurvatureDriveCommand curvatureDriveCommand;
  private ArcadeDriveCommand arcadeDriveCommand;
  private NotStraightArcadeDriveCommand notStraightArcadeDriveCommand;
  private LightsCommand lightsCommand;
  private ShooterSpeedController shooterSpeedController;
  private HopperController hopperController;
  private IndexerController indexerController;

  // Constants
  private final int JOYSTICK_PORT = 1;

  private double overrideSpeed = 1330.0;
  private double indexerSpeed = 0.3;

  private double pValue = 0.2;
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
    shooter_chooser.setDefaultOption("Shooter Camera", kShooterCamera);
    shooter_chooser.addOption("Shooter Distance", kShooterDistance);
    SmartDashboard.putData("Shooter choices", shooter_chooser);
    m_autonChooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_autonChooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_autonChooser);

    m_teleopChooser.setDefaultOption("Curvature Drive", kCurvatureDrive);
    m_teleopChooser.addOption("Arcade Drive", kArcadeDrive);
    m_teleopChooser.addOption("UrNotStraight", kNotStraightArcadeDrive);
    SmartDashboard.putData("Teleop Drive", m_teleopChooser);

    Logger.configureLoggingAndConfig(this, false);

    this.gyro = new Gyroscope();
    this.climber = new Climber();
    this.drive = new Drive(this.gyro);
    this.controls = new Controls(new Joystick(JOYSTICK_PORT));
    this.lights = new Lights(9, 60, 50);
    this.limelight = new Limelight();
    this.lightsCommand = new LightsCommand(lights);
    this.shooterSpeedController = new ShooterSpeedController();
    this.hopperController = new HopperController();
    this.indexerController = new IndexerController();
    this.operatorInputs = new OperatorInputs(controls, drive, gyro, shooterSpeedController, hopperController,
        indexerController, limelight, climber);
    this.lightsCommand = new LightsCommand(this.lights);
    this.autonomousCommand = new AutonomousCommand(distance, maxSpeed, drive, pValue);
    this.shooterCommand = new ShooterCommand(shooterSpeedController, hopperController, indexerController, limelight);
    this.autonomousCommand = new AutonomousCommand(distance, maxSpeed, this.drive, pValue);
    this.curvatureDriveCommand = new CurvatureDriveCommand(this.controls, this.drive);
    this.notStraightArcadeDriveCommand = new NotStraightArcadeDriveCommand(controls, drive);
    this.arcadeDriveCommand = new ArcadeDriveCommand(this.controls, this.drive);
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
    CommandScheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    setDriveMode();
  }

  // use this to override the algorithm and just use a speed
  @Config(name = "Override Speed", defaultValueNumeric = 1330.0)
  public void setOverrideSpeed(final double overrideSpeed) {
    this.overrideSpeed = overrideSpeed;
    shooterSpeedController.setLaunchSpeed(this.overrideSpeed);
  }

  @Override
  public void teleopPeriodic() {
    setDriveMode();
    CommandScheduler.getInstance().run();
    // if (controls.getYButton()) {
    //   System.out.println("Y button pressed");
    //   climber.gotoElevatorPosition(elevatorHeight);
    // }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    CommandScheduler.getInstance().run();
    climber.getFlexSensorPosition();
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

  public void setDriveMode() {
    m_teleopSelected = m_teleopChooser.getSelected();
    switch (m_teleopSelected) {
    case kArcadeDrive:
    if(!(arcadeDriveCommand.isScheduled())){
      curvatureDriveCommand.cancel();
      notStraightArcadeDriveCommand.cancel();
      arcadeDriveCommand.schedule();
    }
      break;
    case kCurvatureDrive:
    if(!(curvatureDriveCommand.isScheduled())){
      arcadeDriveCommand.cancel();
      notStraightArcadeDriveCommand.cancel();
      curvatureDriveCommand.schedule();
    }
      break;
    case kNotStraightArcadeDrive:
    if(!(notStraightArcadeDriveCommand.isScheduled())){
      curvatureDriveCommand.cancel();
      arcadeDriveCommand.cancel();
      notStraightArcadeDriveCommand.schedule();
    }
      break;
    default:
      curvatureDriveCommand.schedule();
      break;
    }
  }
}
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
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.shooter.ShooterSpeedController;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.AutonomousCommand;
import frc.robot.commands.LightsCommand;
import frc.robot.commands.TeleopCommand;
import frc.robot.io.Controls;

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private Drive drive;
  private Controls controls;
  private Shooter shooter;
  private ShooterCommand shooterCommand;
  private ShooterSpeedController shooterSpeedController;
  private HopperController hopperController;
  private IndexerController indexerController;

  private boolean shotRequested = false;

  private double indexerSpeed;
  private Lights lights;
  
  private AutonomousCommand autonomousCommand;
  private TeleopCommand teleopCommand;
  private LightsCommand lightsCommand;

  // Constants
  private final int JOYSTICK_PORT = 1;

  private double distanceToGoal = 0.0;

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
    this.shooterSpeedController = new ShooterSpeedController();

    Logger.configureLoggingAndConfig(this, false);

    this.drive = new Drive();
    this.controls = new Controls(new Joystick(JOYSTICK_PORT));
    this.shooter = new Shooter();
    this.lights = new Lights(9, 60, 50);
    this.hopperController = new HopperController();
    this.indexerController = new IndexerController();

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
    this.shooterCommand = new ShooterCommand(shooterSpeedController);
    if (shooterCommand != null)
    shooterCommand.schedule();
  
    this.teleopCommand = new TeleopCommand(controls, drive, pValue);
    if (teleopCommand != null) {
      teleopCommand.schedule();
    }
  }

  @Config
  public void setDistanceToGoal(double distanceToGoal)
  {
    this.distanceToGoal = distanceToGoal;
  }
 
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    //remove
    this.distanceToGoal = 234.0;
    CommandScheduler.getInstance().run();
    if(controls.getRightTrigger()) {
      System.out.println("indexer triggered");
      indexerController.intake();
      hopperController.start();
    }
    else {
      indexerController.stop();
      hopperController.stop();
    }
    if(controls.getLeftTrigger()) {
      indexerController.outtake();
    }
    if(controls.getRightTrigger() || true) {
      // call shooter.determineLaunchSpeed
      // use it to set the shooterSpeedController

      double speed = shooter.determineLaunchSpeed(distanceToGoal + 29.0);
      shooterSpeedController.setLaunchSpeed(speed); //using a number that should be replaced

      //Set the bool to know that a shot is requested
      shotRequested = true;
    }

    
    // System.out.println("current set launch speed " + shooterSpeedController.getLaunchSpeed());
    // System.out.println("current shooter speed " + shooterSpeedController.getCurrentSpeed());

    // when firing the shooter, make sure it's at speed
    if (shotRequested && shooterSpeedController.isAtSpeed()) {
      // fire the shooter
      //code to fire shooter
      System.out.println("*******FIREFIREFIRE******!");

      //Set the bool to know that the shot was fired
      shotRequested = false;
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  @Config(name = "Indexer Speed", defaultValueNumeric = 0.3)
  public void setIndexerSpeed(double indexerSpeed){
    this.indexerSpeed = indexerSpeed;
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

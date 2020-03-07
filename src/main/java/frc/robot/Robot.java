package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.vision.Limelight;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.shooter.ShooterSpeedController;
import frc.robot.subsystems.drive.Gyroscope;
import frc.robot.subsystems.lights.Lights;
import frc.robot.subsystems.lights.LightsController;
import frc.robot.commands.autonomousCommands.AutonomousCommandGroup;
import frc.robot.operatorInputs.Controls;
import frc.robot.operatorInputs.OperatorInputs;
import frc.robot.subsystems.Climber;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot {
  private Drive drive;
  private Controls controls;
  private Lights lights;
  private Limelight limelight;
  private Gyroscope gyro;
  private Climber climber;
  private OperatorInputs operatorInputs;
  private LightsController lightsController;
  private ShooterSpeedController shooterSpeedController;
  private HopperController hopperController;
  private IndexerController indexerController;
  private UsbCamera usbCamera;

  private AutonomousCommandGroup autonomousCommandGroup;

  // Constants
  private final int JOYSTICK_PORT = 1;

  private double maxSpeed = 0.75;
  private double distance = 36.0;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    this.usbCamera = CameraServer.getInstance().startAutomaticCapture();
    this.gyro = new Gyroscope();
    this.climber = new Climber();
    this.drive = new Drive(this.gyro);
    this.controls = new Controls(new Joystick(JOYSTICK_PORT));
    this.lights = new Lights(9, 82, 50);
    this.limelight = new Limelight();
    this.shooterSpeedController = new ShooterSpeedController();
    this.hopperController = new HopperController();
    this.indexerController = new IndexerController();
    this.lightsController = new LightsController(this.lights, this.limelight);
    this.operatorInputs = new OperatorInputs(controls, drive, gyro, shooterSpeedController, hopperController,
        indexerController, limelight, climber, lightsController);
    this.autonomousCommandGroup = new AutonomousCommandGroup(drive, shooterSpeedController, hopperController,
        indexerController, limelight, distance, maxSpeed);
    
    Logger.configureLoggingAndConfig(this, false);
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
    autonomousCommandGroup.schedule();
  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
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

  @Log.CameraStream(name = "CAMERA", width = 20, height = 20, showCrosshairs = false, showControls = false)
  private UsbCamera getCamera() {
    return usbCamera;
  }

  @Log.BooleanBox(name = "Limelight LOCK", width = 16, height = 16)
  private boolean isLimelightLockedOn() {
    return limelight.hasValidTarget();
  }

  @Log(name = "Current Limelight Pipeline", width = 32, height = 32)
  private double currentLimelightPipeline() {
    return limelight.getPipeline();
  }  

  @Config(name = "DISABLE GYRO", defaultValueBoolean = false)
  private void disableEnableGyro(boolean gyroDisabled) {
    this.drive.gyroDisabled(gyroDisabled);
  }

  @Config(tabName = "Autonomous", name = "Distance", defaultValueNumeric = 36)
  public void setAutonStraightDistance(final double distance) {
    this.distance = distance;
  }
}
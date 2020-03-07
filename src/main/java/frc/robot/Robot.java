package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystems.HopperController;
import frc.robot.subsystems.IndexerController;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.subsystems.vision.LimelightAim;
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
import edu.wpi.cscore.VideoSource;
import edu.wpi.first.cameraserver.CameraServer;

public class Robot extends TimedRobot {
  private Drive drive;
  private Controls driverControls;
  private Controls operatorControls;
  private Lights lights;
  private Limelight limelight;
  private Gyroscope gyro;
  private Climber climber;
  private OperatorInputs operatorInputs;
  private LightsController lightsController;
  private ShooterSpeedController shooterSpeedController;
  private HopperController hopperController;
  private IndexerController indexerController;
  private LimelightAim limelightAim;

  private UsbCamera usbCamera;
  private VideoSource limelightCamera;

  private AutonomousCommandGroup autonomousCommandGroup;

  // Constants
  private final int JOYSTICK_PORT_DRIVER = 1;
  private final int JOYSTICK_PORT_OPERATOR = 0;

  private double maxSpeed = 0.75;
  private double distance = 36.0;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    this.usbCamera = CameraServer.getInstance().startAutomaticCapture();
    this.limelightCamera = CameraServer.getInstance().addServer("http://limelight.local:5800/stream.mjpg").getSource();
    this.gyro = new Gyroscope();
    this.climber = new Climber();
    this.drive = new Drive(this.gyro);
    this.driverControls = new Controls(new Joystick(JOYSTICK_PORT_DRIVER));
    this.operatorControls = new Controls(new Joystick(JOYSTICK_PORT_OPERATOR));
    this.lights = new Lights(9, 82, 50);
    this.limelight = new Limelight();
    this.shooterSpeedController = new ShooterSpeedController();
    this.hopperController = new HopperController();
    this.indexerController = new IndexerController();
    this.lightsController = new LightsController(this.lights, this.limelight);
    this.limelightAim = new LimelightAim(limelight);
    this.operatorInputs = new OperatorInputs(driverControls, operatorControls, drive, gyro, shooterSpeedController,
        hopperController, indexerController, limelight, climber, lightsController);
    this.autonomousCommandGroup = new AutonomousCommandGroup(drive, shooterSpeedController, hopperController,
        indexerController, limelight, limelightAim, distance, maxSpeed);

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

  @Log.CameraStream(name = "CAMERA", width = 500, height = 500, showCrosshairs = false, showControls = false)
  private UsbCamera getCamera() {
    return usbCamera;
  }

  // @Log.CameraStream(name = "Limelight CAMERA", width = 100, height = 100, showCrosshairs = false, showControls = false)
  // private VideoSource getLimelightCamera() {
  //     return limelightCamera;
  // }


  @Log.BooleanBox(name = "Limelight LOCK", width = 100, height = 100)
  private boolean isLimelightLockedOn() {
    return limelight.hasValidTarget();
  }

  @Log(name = "Current Limelight Pipeline", width = 100, height = 100)
  private String currentLimelightPipeline() {
    return limelight.getPipeline() == 1 ? "Loading Zone" : "High Goal";
  }  
  
  @Config(name = "DISABLE GYRO", defaultValueBoolean = false)
  private void disableEnableGyro(boolean gyroDisabled) {
    this.drive.gyroDisabled(gyroDisabled);
  }

  @Config(name = "High Goal Pipeline", defaultValueBoolean = false) 
  private void enableHighGoalPipeline(boolean enable) {
    if(enable) {
      limelight.setHighGoalPipeline();
    }
  }

  @Config(name = "Loading Zone Pipeline", defaultValueBoolean = false) 
  private void enableLoadingZonePipeline(boolean enable) {
    if(enable) {
      limelight.setLowGoalPipeline();
    }
  }
}
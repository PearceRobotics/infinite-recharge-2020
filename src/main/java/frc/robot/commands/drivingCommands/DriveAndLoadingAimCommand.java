package frc.robot.commands.drivingCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.operatorInputs.Controls;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.subsystems.vision.LimelightAim;

public class DriveAndLoadingAimCommand extends CommandBase {

    //classes
    private Drive drive;
    private Limelight limelight;
    private Controls driverControls;

    //constants
    private final double SlOW_SPEED_DIVISOR = 100.0;
    private final double DEADZONE = 0.2;
    private final double MIN_SPEED = 0.5;

    // private final double CONTROLS_

    public DriveAndLoadingAimCommand(Drive drive, Limelight limelight, Controls driverControls) {
        this.drive = drive;
        this.limelight = limelight;
        this.driverControls = driverControls;

        private double slowSpeed = 0;

        addRequirements(drive);
    }

    @Override
    public void initialize() {
        limelight.setLowGoalPipeline();
    }

    @Override
    public void execute() {
        if (limelight.hasValidTarget()) {
            slowSpeed = limelight.getTargetArea()/SlOW_SPEED_DIVISOR;
            if(slowSpeed < MIN_SPEED){
                slowSpeed = MIN_SPEED;
            }
            else{
            double steeringAssist = LimelightAim.getSteeringAdjust(limelight.getHorizontalTargetOffset());
            drive.arcadeDrive(-(driverControls.getLeftY(DEADZONE)-slowSpeed), steeringAssist);
            }
        } else {
            drive.curvatureDrive(driverControls.getLeftY(DEADZONE)-slowSpeed, driverControls.getRightX(DEADZONE));
        }
    }

    @Override
    public void end(boolean interupted) {
        limelight.setHighGoalPipeline();
    }

}
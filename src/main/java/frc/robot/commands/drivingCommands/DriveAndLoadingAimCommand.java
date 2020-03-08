package frc.robot.commands.drivingCommands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.operatorInputs.Controls;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.vision.Limelight;
import frc.robot.subsystems.vision.LimelightAim;

public class DriveAndLoadingAimCommand extends CommandBase {

    private Drive drive;
    private Limelight limelight;
    private Controls driverControls;

    // private final double CONTROLS_

    public DriveAndLoadingAimCommand(Drive drive, Limelight limelight, Controls driverControls) {
        this.drive = drive;
        this.limelight = limelight;
        this.driverControls = driverControls;
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        limelight.setLowGoalPipeline();
    }

    @Override
    public void execute() {
        if (limelight.hasValidTarget()) {
            double steeringAssist = LimelightAim.getSteeringAdjust(limelight.getHorizontalTargetOffset());
            drive.arcadeDrive(-driverControls.getLeftY(0.2), steeringAssist);
        } else {
            drive.curvatureDrive(driverControls.getLeftY(0.2), driverControls.getRightX(0.2));
        }
    }

    @Override
    public void end(boolean interupted) {
        limelight.setHighGoalPipeline();
    }

}
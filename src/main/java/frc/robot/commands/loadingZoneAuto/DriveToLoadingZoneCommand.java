package frc.robot.commands.loadingZoneAuto;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Drive;

public class DriveToLoadingZoneCommand extends CommandBase {
    private Drive drive;

    private final String TRAJECTORY_JSON = "paths/YourPath.wpilib.json";
    public DriveToLoadingZoneCommand(Drive drive) {
        this.drive = drive;
    }

    @Override
    public void execute() {
        try {
            Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(TRAJECTORY_JSON);
            Trajectory trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + TRAJECTORY_JSON, ex.getStackTrace());
        }
    }
}
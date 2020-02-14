package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.Gyroscope;
import frc.robot.subsystems.drive.Drive;
public class GyroTurnCommand extends CommandBase{
    //Classes
    private Gyroscope gyro;
    private Drive drive;
    //Variables
    private double turnAngle;//Angle we want the robot to rotate in degrees
    private double newAngle;//Angle gyroscope will see new angle as
    private double error; //continuous error the robot is off from its desired position
    private double speed;//speed the robot turns
    //Constants
    private double ratio = 0.0027;//ratio to multiply error by to get a number between -1 and 1 for the speed
    private double minSpeed = 0.1;// minimum speed the robot will drive
    private double p = 1.0;//p loop constant

    public GyroTurnCommand(Gyroscope gyro, double turnAngle, Drive drive){

        this.gyro = gyro;
        this.turnAngle = turnAngle;
        this.drive = drive;
       // rotateController = new PIDController(kP, kI, kD);

        addRequirements(gyro);
        addRequirements(drive);
    }
    @Override
    public void initialize() {

        double currentAngle = gyro.getGyroAngle();
        newAngle = currentAngle + turnAngle;
        drive.setBrakeMode();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    public void execute() {

        error = newAngle - gyro.getGyroAngle();
        speed = -error*(ratio) *p;

        if(Math.abs(speed) < minSpeed){
            if (speed < 0.0){
                speed = -minSpeed;
            }
            else if(speed >= 0.0) {
                speed = minSpeed;
            }
            else{
                // do nothing
            }
        }
        drive.arcadeDrive(0.0, speed);
        System.out.println("gyro angle" + gyro.getGyroAngle());
        System.out.println("gyro speed" + speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    public boolean isFinished() {
        if(Math.abs(gyro.getGyroAngle() - newAngle) > 1.0)
        {
            return false;
        }
        else{
            drive.arcadeDrive(0.0, 0.0);
            return true;
        }
    }

    // Called once after isFinished returns true
    @Override
    public void end(boolean interrupted) {
     
    }
}




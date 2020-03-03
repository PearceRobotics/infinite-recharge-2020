package frc.robot.operatorInputs;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Button;

public class Controls {

    private Joystick driveJoystick;
    private Joystick operatorJoystick;
    private final int LEFT_X = 0;
    private final int LEFT_Y = 1;
    private final int RIGHT_X = 4;
    private final int RIGHT_Y = 5;

    private final int RIGHT_BUMPER_ID = 6;
    private final int RIGHT_TRIGGER_ID = 3;

    private final int LEFT_BUMPER_ID = 5;
    private final int LEFT_TRIGGER_ID = 2;

    private final int B_BUTTON_ID = 2;
    private final int Y_BUTTON_ID = 4;
    private final int A_BUTTON_ID = 1;
    private final int X_BUTTON_ID = 3;

    private final int LEFT_STICK_BUTTON_ID = 9;
    private final int RIGHT_STICK_BUTTON_ID = 10;

    private final JoystickButton aDriveJoystickButton;
    private final JoystickButton bDriveJoystickButton;
    private final JoystickButton xDriveJoystickButton;
    private final JoystickButton yDriveJoystickButton;

    private final JoystickButton aOperatorJoystickButton;
    private final JoystickButton bOperatorJoystickButton;
    private final JoystickButton xOperatorJoystickButton;
    private final JoystickButton yOperatorJoystickButton;

    private final JoystickButton leftDriveBumperJoystickButton;
    private final JoystickButton rightDriveBumperJoystickButton;

    private final JoystickButton leftOperatorBumperJoystickButton;
    private final JoystickButton rightOperatorBumperJoystickButton;

    private final JoystickButton leftDriveStickJoystickButton;
    private final JoystickButton rightDriveStickJoystickButton;

    private final JoystickButton leftOperatorStickJoystickButton;
    private final JoystickButton rightOperatorStickJoystickButton;

    public Controls(Joystick driveJoystick, Joystick operatorJoystick) {
        this.driveJoystick = driveJoystick;
        this.operatorJoystick = operatorJoystick;

        aDriveJoystickButton = new JoystickButton(driveJoystick, A_BUTTON_ID);
        bDriveJoystickButton = new JoystickButton(driveJoystick, B_BUTTON_ID);
        xDriveJoystickButton =  new JoystickButton(driveJoystick, X_BUTTON_ID);
        yDriveJoystickButton =  new JoystickButton(driveJoystick, Y_BUTTON_ID);

        aOperatorJoystickButton = new JoystickButton(operatorJoystick, A_BUTTON_ID);
        bOperatorJoystickButton = new JoystickButton(operatorJoystick, B_BUTTON_ID);
        xOperatorJoystickButton =  new JoystickButton(operatorJoystick, X_BUTTON_ID);
        yOperatorJoystickButton =  new JoystickButton(operatorJoystick, Y_BUTTON_ID);
    
        leftDriveBumperJoystickButton = new JoystickButton(driveJoystick, LEFT_BUMPER_ID);
        rightDriveBumperJoystickButton = new JoystickButton(driveJoystick, RIGHT_BUMPER_ID);

        leftOperatorBumperJoystickButton = new JoystickButton(operatorJoystick, LEFT_BUMPER_ID);
        rightOperatorBumperJoystickButton = new JoystickButton(operatorJoystick, RIGHT_BUMPER_ID);
        
        leftOperatorStickJoystickButton = new JoystickButton(operatorJoystick, LEFT_STICK_BUTTON_ID);
        rightOperatorStickJoystickButton = new JoystickButton(operatorJoystick, RIGHT_STICK_BUTTON_ID);

        leftDriveStickJoystickButton = new JoystickButton(driveJoystick, LEFT_STICK_BUTTON_ID);
        rightDriveStickJoystickButton = new JoystickButton(driveJoystick, RIGHT_STICK_BUTTON_ID);

    }

    public boolean getDriveBButton() {
        return driveJoystick.getRawButton(B_BUTTON_ID);
    }

    public boolean getOperatorBButton() {
        return operatorJoystick.getRawButton(B_BUTTON_ID);
    }

    public JoystickButton getDriveJoystickBButton() {
        return bDriveJoystickButton;
    }

    public JoystickButton getOperatorJoystickBButton() {
        return bOperatorJoystickButton;
    }

    public boolean getDriveAButton() {
        return driveJoystick.getRawButton(A_BUTTON_ID);
    }

    public boolean getOperatorAButton() {
        return operatorJoystick.getRawButton(A_BUTTON_ID);
    }

    public JoystickButton getDriveJoystickAButton() {
        return aDriveJoystickButton;
    }

    public JoystickButton getOperatorJoystickAButton() {
        return aOperatorJoystickButton;
    }

    public boolean getDriveXButton() {
        return driveJoystick.getRawButton(X_BUTTON_ID);
    }

    public boolean getOperatorXButton() {
        return operatorJoystick.getRawButton(X_BUTTON_ID);
    }

    public JoystickButton getDriveJoystickXButton() {
        return xDriveJoystickButton;
    }
    public JoystickButton getOperatorJoystickXButton() {
        return xOperatorJoystickButton;
    }

    public boolean getDriveYButton() {
        return driveJoystick.getRawButton(Y_BUTTON_ID);
    }

    public boolean getOperatorYButton() {
        return operatorJoystick.getRawButton(Y_BUTTON_ID);
    }

    public JoystickButton getDriveJoystickYButton() {
        return yDriveJoystickButton;
    }

    public JoystickButton getOperatorJoystickYButton() {
        return yOperatorJoystickButton;
    }

    public double getDriveRightX(double deadzone) {
        double x = driveJoystick.getRawAxis(RIGHT_X);
        if (Math.abs(x) < deadzone) {
            return 0.0;
        }
        return x;
    }

    public double getOperatorRightX(double deadzone) {
        double x = operatorJoystick.getRawAxis(RIGHT_X);
        if (Math.abs(x) < deadzone) {
            return 0.0;
        }
        return x;
    }

    public double getDriveLeftY(double deadzone) {
        double y = driveJoystick.getRawAxis(LEFT_Y);
        if (Math.abs(y) < deadzone) {
            return 0.0;
        }
        return y;
    }

    public double getOperatorLeftY(double deadzone) {
        double y = operatorJoystick.getRawAxis(LEFT_Y);
        if (Math.abs(y) < deadzone) {
            return 0.0;
        }
        return y;
    }

    public double getDriveRightY(double deadzone) {
        double y = driveJoystick.getRawAxis(RIGHT_Y);
        if (Math.abs(y) < deadzone) {
            return 0.0;
        }
        return y;
    }

    public double getOperatorRightY(double deadzone) {
        double y = operatorJoystick.getRawAxis(RIGHT_Y);
        if (Math.abs(y) < deadzone) {
            return 0.0;
        }
        return y;
    }

    public boolean getDriveRightBumper() {
        return driveJoystick.getRawButton(RIGHT_BUMPER_ID);
    }

    public boolean getLeftRightBumper() {
        return operatorJoystick.getRawButton(RIGHT_BUMPER_ID);
    }

    public JoystickButton getDriveRightJoystickBumper() {
        return rightDriveBumperJoystickButton;
    }

    public JoystickButton getOperatorRightJoystickBumper() {
        return rightOperatorBumperJoystickButton;
    }

    public boolean getDriveRightTrigger() {
        return driveJoystick.getRawAxis(RIGHT_TRIGGER_ID) > 0;
    }

    public boolean getOperatorRightTrigger() {
        return operatorJoystick.getRawAxis(RIGHT_TRIGGER_ID) > 0;
    }

    public boolean getDriveLeftBumper() {
        return driveJoystick.getRawButton(LEFT_BUMPER_ID);
    }

    public boolean getOperatorLeftBumper() {
        return operatorJoystick.getRawButton(LEFT_BUMPER_ID);
    }

    public JoystickButton getDriveLeftJoystickBumper() {
        return leftDriveBumperJoystickButton;
    }

    public JoystickButton getOperatorstickBumper() {
        return leftOperatorBumperJoystickButton;
    }

    public boolean getDriveLeftTrigger() {
        return driveJoystick.getRawAxis(LEFT_TRIGGER_ID) > 0;
    }

    public boolean getOperatorLeftTrigger() {
        return operatorJoystick.getRawAxis(LEFT_TRIGGER_ID) > 0;
    }

    public JoystickButton getDriveLeftStick(){
        return leftDriveStickJoystickButton;
    }

    public JoystickButton getOperatorLeftStick(){
        return leftOperatorStickJoystickButton;
    }

    public JoystickButton getDriveRightStick(){
        return rightDriveStickJoystickButton;
    }

    public JoystickButton getOperatorRightStick(){
        return rightOperatorStickJoystickButton;
    }
}
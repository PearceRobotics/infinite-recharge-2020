/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class ColorSensorCommand extends CommandBase {
    private double ballGreen;
    private double ballBlue;
    private double ballRed;
    private I2C.Port i2cPort;
    private ColorSensorV3 m_colorSensor;
    private final double COLOR_RANGE = 0.05;

    public ColorSensorCommand() {
        i2cPort = I2C.Port.kOnboard;
        m_colorSensor = new ColorSensorV3(i2cPort);
}

    @Override
    public void initialize() {
        // Runs before this command runs for the first time

    
    }

    @Override
    public void execute() {
        Color detectedColor = m_colorSensor.getColor();
        double IR = m_colorSensor.getIR();
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("IR", IR);
        int proximity = m_colorSensor.getProximity();
        SmartDashboard.putNumber("Proximity", proximity);


        if (Math.abs(detectedColor.red - ballRed) <= COLOR_RANGE
                && Math.abs(detectedColor.blue - ballBlue) <= COLOR_RANGE
                && Math.abs(detectedColor.green - ballGreen) <= COLOR_RANGE) {
            System.out.println("Is a ball");
        }
    }
}

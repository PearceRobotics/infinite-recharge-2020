package frc.robot.subsystems.shooter;

import io.github.oblarg.oblog.annotations.Config;

/**
 * Add your docs here.
 */
public class Shooter {
    private final double wheelDiameter = 4.0; // in
    private final double Gravity = 386.0886; // in/s^2
    private final double maxRpm = 7640.0; // rpms
    private final double launcherHeight = 23.0; // in
    private final double wheelCircumference = wheelDiameter * Math.PI; // inches
    private final double maxTangentialSpeed = (wheelCircumference * (maxRpm / 60.0)); // in/s
    private final double initialUpperBound = 1.0; // 100% speed
    private final double initialLowerBound = 0.0;// 0% speed
    private final double launcherDegrees = 32.0; // launcherDegrees, used for ease of measurement and understanding
    private final double launcherRadians = Math.toRadians(launcherDegrees); // radians
    private final double targetHeight = 98.0; // inches above ground

    private final double ballDiameter = 7.0; // in
    private final double rho = 0.00004428; // lbm/in^3, STP air density
    private final double ballArea = (ballDiameter / 2.0) * (ballDiameter / 2.0) * Math.PI; // in^2
    private final double cd = 0.15; // unitless, taken from Chief Delphi 2012 game piece calculations
    private final double ballMass = 5.0 / 16.0; // lbm, AndyMark says balls are ~5 ounces

    private final int iterations = 20; //number of iterations when searching for the solution

    //not final so we can set with shuffleboard
    private double energyLostBase = 0.5; // Percent energy lost from full tangential speed to actual ball speed
    private double maxLaunchSpeed = maxTangentialSpeed * (1.0 - energyLostBase); // in/s

    public Shooter() {
        // empty contructor
    }

    @Config
    public void setEnergyLost(double energyLostBase) {
        this.energyLostBase = energyLostBase;
        this.maxLaunchSpeed = maxTangentialSpeed * (1.0 - energyLostBase);
    }

    public double determineLaunchSpeed(double distanceToTarget) {
        // declaration of % bounds
        double range = initialUpperBound - initialLowerBound;
        double launchPower = initialUpperBound;
        double heightAtTargetDistance;

        // Determine height at target distance
        heightAtTargetDistance = getHeightAtTargetDistance(distanceToTarget, launchPower);

        if (heightAtTargetDistance < targetHeight) {
            return -1.0;
            // shot not possible at current distance
        } else if (heightAtTargetDistance == targetHeight) {
            return maxTangentialSpeed * launchPower; // percent of max speed we should be going times the max speed
        }
        // iteration
        else {
            for (int i = 0; i < iterations; i++) {// binary search...gets close enough to what we want
                if (heightAtTargetDistance > targetHeight) {// if our current height is greater than our target height
                                                            // raise lowerbound
                    launchPower = launchPower - (range / (2 * Math.pow(2, i))); // decreases launchPower
                } else if (heightAtTargetDistance < targetHeight) {
                    launchPower = launchPower + (range / (2 * Math.pow(2, i)));// increases launchPower
                } else {
                    break;
                }
                heightAtTargetDistance = getHeightAtTargetDistance(distanceToTarget, launchPower);
            }
        }
        return launchPower * maxTangentialSpeed;

    }

    private double getHeightAtTargetDistance(double distanceToTarget, double launchPower) {
        // if you have a triangle, with the hypnotonuse being the angled velocity
        // vector, the adjacent being the horizontal velocity vector, and the opposite
        // side being the height
        // horizontal speed = adjacent side -> cos(angle) = a/h -> hcos(angle) = a ->
        // (maxLaunchSpeed * launchPower)cos(angle) = a
        double horizontalSpeed = Math.cos(launcherRadians) * maxLaunchSpeed * launchPower;

        // air resistance
        // 1/2 * rho * v^2 * A * cd, or lbm*in^2*in^2/in^3*s^2 === lbm*in/s^2
        double dragForceHorizontal = 0.5 * rho * horizontalSpeed * horizontalSpeed * ballArea * cd;

        //divide by mass to get in/s^2
        double horizontalAirResistanceAcceleration = dragForceHorizontal / ballMass;

        // Use quadratic equation to find the positive solution
        double travelTime = (-horizontalSpeed + Math.sqrt((horizontalSpeed * horizontalSpeed)
                - (4.0 * -0.5 * horizontalAirResistanceAcceleration * -distanceToTarget)))
                / (2.0 * -0.5 * horizontalAirResistanceAcceleration);

        double verticalSpeed = (Math.sin(launcherRadians) * maxLaunchSpeed * launchPower);

        // air resistance
        // 1/2 * rho * v^2 * A * cd, or lbm*in^2*in^2/in^3*s^2 === lbm*in/s^2
        double dragForceVertical = 0.5 * rho * verticalSpeed * verticalSpeed * ballArea * cd;
        
        //divide by mass to get in/s^2
        double verticalAirResistanceAcceleration = dragForceVertical / ballMass;

        // current distance = original distance +vt + .5at^2
        double heightAtTargetDistance = launcherHeight
                + (Math.sin(launcherRadians) * maxLaunchSpeed * launchPower * travelTime)
                - (0.5 * (Gravity + verticalAirResistanceAcceleration) * Math.pow(travelTime, 2));
        return heightAtTargetDistance;
    }

}

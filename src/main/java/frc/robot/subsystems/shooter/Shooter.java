package frc.robot.subsystems.shooter;

import io.github.oblarg.oblog.annotations.Config;

/**
 * Add your docs here.
 */
public class Shooter {
    private final double wheelDiameter = 4; // inches
    private final double Gravity = 386.0886; //inches per second squared
    private final double maxRpm = 7640.0; //rpms
    private final double launcherHeight = 24.0; // inches
    private double energyLostBase = 0.5; // Percent energy lost from full tangential speed to actual ball speed
    private final double wheelCircumference = wheelDiameter * Math.PI; //inches
    private final double maxTangentialSpeed = (wheelCircumference * (maxRpm / 60.0)); //inches per second
    private final double initialUpperBound = 1.0; // 100% speed
    private final double initialLowerBound = 0.0;// 0% speed
    private final double launcherDegrees = 32.0; //launcherDegrees
    private final double launcherRadians = Math.toRadians(launcherDegrees);
    private double targetHeight = 98.0;

    private final double INCHES_TO_METERS = 2.54 / 100.0;

    private double maxLaunchSpeed = maxTangentialSpeed * (1.0 - energyLostBase);

    public Shooter() {
        // empty contructor
    }

    @Config
    public void setEnergyLost(double energyLostBase) {
        this.energyLostBase = energyLostBase;
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
            for (int i = 0; i < 15; i++) {// binary search...gets close enough to what we want
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
        double horizontalSpeed;
        double travelTime;

        double ballDiameterInches = 7.0;
        double ballDiameterMeters = ballDiameterInches * INCHES_TO_METERS;

        double beta = 0.00016;
        double gamma = 0.25;

        // 155 inches is 9977017 //speed 1122

        // 184 inches is 9,244,452 //speed 1080

        // if you have a triangle, with the hypnotonuse being the angled velocity
        // vector, the adjacent being the horizontal velocity vector, and the opposite
        // side being the height
        // horizontal speed = adjacent side -> cos(angle) = a/h -> hcos(angle) = a ->
        // (maxLaunchSpeed * launchPower)cos(angle) = a
        horizontalSpeed = Math.cos(launcherRadians) * maxLaunchSpeed * launchPower;

        double horizontalSpeedMs = horizontalSpeed * INCHES_TO_METERS;

        double horizontalAirResistanceMetersPerSecondSquared = (ballDiameterMeters * beta * horizontalSpeedMs)
                + (gamma * ballDiameterMeters * ballDiameterMeters * horizontalSpeedMs * horizontalSpeedMs);

        double horizontalAirResistanceInchesPerSecondSquared = horizontalAirResistanceMetersPerSecondSquared
                / INCHES_TO_METERS;

        double qPlus = (-horizontalSpeed + Math.sqrt((horizontalSpeed * horizontalSpeed)
                - (4.0 * -0.5 * horizontalAirResistanceInchesPerSecondSquared * -distanceToTarget)))
                / (2.0 * -0.5 * horizontalAirResistanceInchesPerSecondSquared);

        // v = d/t -> vt=d -> t = d/v
        travelTime = qPlus;// distanceToTarget / horizontalSpeed;

        // System.out.println("travel time " + travelTime);

        double verticalSpeedMetersPerSecond = (Math.sin(launcherRadians) * maxLaunchSpeed * launchPower) * INCHES_TO_METERS;

        // System.out.println("vertical m/s " + verticalSpeedMetersPerSecond);

        double verticalAirResistanceMetersPerSecondSquared = (ballDiameterMeters * beta * verticalSpeedMetersPerSecond)
                + (gamma * ballDiameterMeters * ballDiameterMeters * verticalSpeedMetersPerSecond
                        * verticalSpeedMetersPerSecond);

        double verticalAirResistanceInchesPerSecondSquared = verticalAirResistanceMetersPerSecondSquared
                / INCHES_TO_METERS;

        // System.out.println("vertical air resistance time " +
        // verticalAirResistanceInchesPerSecondSquared);

        // current distance = original distance +vt + .5at^2
        double heightAtTargetDistance = launcherHeight + (Math.sin(launcherRadians) * maxLaunchSpeed * launchPower * travelTime)
                - (0.5 * (Gravity + verticalAirResistanceInchesPerSecondSquared) * Math.pow(travelTime, 2));
        return heightAtTargetDistance;
    }

}

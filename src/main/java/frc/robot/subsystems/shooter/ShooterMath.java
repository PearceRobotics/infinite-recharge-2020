package frc.robot.subsystems.shooter;

import io.github.oblarg.oblog.annotations.Config;

public class ShooterMath {
    private static final double WHEEL_DIAMETER = 4.0; // in
    private static final double GRAVITY = 386.0886; // in/s^2
    private static final double MAX_RPM = 7640.0; // rpms
    private static final double LAUNCHER_HEIGHT = 23.0; // in
    private static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI; // inches
    private static final double MAX_TANGENTIAL_SPEED = (WHEEL_CIRCUMFERENCE * (MAX_RPM / 60.0)); // in/s
    private static final double INITIAL_UPPER_BOUND = 1.0; // 100% speed
    private static final double INITIAL_LOWER_BOUND = 0.0;// 0% speed
    private static final double LAUNCHER_DEGREES = 32.0; // launcherDegrees, used for ease of measurement and
                                                         // understanding
    private static final double LAUNCHER_RADIANS = Math.toRadians(LAUNCHER_DEGREES); // radians
    private static final double GOAL_HEIGHT = 98.0; // inches above ground
    private static final double BALL_DIAMETER = 7.0; // in
    private static final double RHO = 0.00004428; // lbm/in^3, STP air density
    private static final double BALL_SURFACE_AREA = (BALL_DIAMETER / 2.0) * (BALL_DIAMETER / 2.0) * Math.PI; // in^2
    private static final double DRAG_COEFFICIENT = 0.35; // unitless, roughly matches a tennis ball
    private static final double BALL_MASS = 5.0 / 16.0; // lbm, AndyMark says balls are ~5 ounces
    private static final int ITERATIONS = 20; // number of iterations when searching for the solution

    // not final so we can set with shuffleboard
    private static double energyLostBase = 0.58; // Percent energy lost from full tangential speed to actual ball speed
    private static double maxLaunchSpeed = MAX_TANGENTIAL_SPEED * (1.0 - energyLostBase); // in/s

    public ShooterMath() {
        // empty contructor
    }

    @Config
    public static void setEnergyLost(double energyLostBaseIn) {
        energyLostBase = energyLostBaseIn;
        maxLaunchSpeed = MAX_TANGENTIAL_SPEED * (1.0 - energyLostBase);
    }

    public static double determineLaunchSpeed(double distanceToTarget) {
        // declaration of % bounds
        double range = INITIAL_UPPER_BOUND - INITIAL_LOWER_BOUND;
        double launchPower = INITIAL_UPPER_BOUND;
        double heightAtTargetDistance;

        // Determine height at target distance
        heightAtTargetDistance = getHeightAtTargetDistance(distanceToTarget, launchPower);

        if (heightAtTargetDistance < GOAL_HEIGHT) {
            return -1.0;
            // shot not possible at current distance
        } else if (heightAtTargetDistance == GOAL_HEIGHT) {
            return MAX_TANGENTIAL_SPEED * launchPower; // percent of max speed we should be going times the max speed
        }
        // iteration
        else {
            for (int i = 0; i < ITERATIONS; ++i) {// binary search...gets close enough to what we want
                if (heightAtTargetDistance > GOAL_HEIGHT) {// if our current height is greater than our target height
                                                           // raise lowerbound
                    launchPower = launchPower - (range / (2.0 * Math.pow(2, i))); // decreases launchPower
                } else if (heightAtTargetDistance < GOAL_HEIGHT) {
                    launchPower = launchPower + (range / (2.0 * Math.pow(2, i)));// increases launchPower
                } else {
                    break;
                }
                heightAtTargetDistance = getHeightAtTargetDistance(distanceToTarget, launchPower);
            }
        }
        return launchPower * MAX_TANGENTIAL_SPEED;

    }

    private static double getHeightAtTargetDistance(double distanceToTarget, double launchPower) {
        // if you have a triangle, with the hypnotonuse being the angled velocity
        // vector, the adjacent being the horizontal velocity vector, and the opposite
        // side being the height
        // horizontal speed = adjacent side -> cos(angle) = a/h -> hcos(angle) = a ->
        // (maxLaunchSpeed * launchPower)cos(angle) = a
        double horizontalSpeed = Math.cos(LAUNCHER_RADIANS) * maxLaunchSpeed * launchPower;

        // air resistance
        // 1/2 * rho * v^2 * A * cd, or lbm*in^2*in^2/in^3*s^2 === lbm*in/s^2
        double dragForceHorizontal = 0.5 * RHO * horizontalSpeed * horizontalSpeed * BALL_SURFACE_AREA
                * DRAG_COEFFICIENT;

        // divide by mass to get in/s^2
        double horizontalAirResistanceAcceleration = dragForceHorizontal / BALL_MASS;

        // Use quadratic equation to find the positive solution
        double travelTime = (-horizontalSpeed + Math.sqrt((horizontalSpeed * horizontalSpeed)
                - (4.0 * -0.5 * horizontalAirResistanceAcceleration * -distanceToTarget)))
                / (2.0 * -0.5 * horizontalAirResistanceAcceleration);

        double verticalSpeed = (Math.sin(LAUNCHER_RADIANS) * maxLaunchSpeed * launchPower);

        // air resistance
        // 1/2 * rho * v^2 * A * cd, or lbm*in^2*in^2/in^3*s^2 === lbm*in/s^2
        double dragForceVertical = 0.5 * RHO * verticalSpeed * verticalSpeed * BALL_SURFACE_AREA * DRAG_COEFFICIENT;

        // divide by mass to get in/s^2
        double verticalAirResistanceAcceleration = dragForceVertical / BALL_MASS;

        // current distance = original distance +vt + .5at^2
        double heightAtTargetDistance = LAUNCHER_HEIGHT
                + (Math.sin(LAUNCHER_RADIANS) * maxLaunchSpeed * launchPower * travelTime)
                - (0.5 * (GRAVITY + verticalAirResistanceAcceleration) * Math.pow(travelTime, 2));
        return heightAtTargetDistance;
    }

}

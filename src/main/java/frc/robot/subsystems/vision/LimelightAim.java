package frc.robot.subsystems.vision;

public class LimelightAim {

    private static final double MAX_SPEED = .4;
    private static final double MIN_SPEED = 0.2;
    private static final double KpAIM = 0.09;

    public static  double getSteeringAdjust(double offset) {
        // Keep steering adjust between MIN and MAX. set to abs to determine magnitude,
        // but reuse the sign
        double steeringAdjust = Math
                .copySign(Math.max(MIN_SPEED, Math.min(MAX_SPEED, KpAIM * Math.abs(offset))), offset);

        return steeringAdjust;
    }
}
package frc.robot.subsystems.vision;

public class LimelightAim {
    private Limelight limelight;

    private final double MAX_SPEED = .4;
    private final double MIN_SPEED = 0.2;
    private final double KpAIM = 0.09;

    public LimelightAim(Limelight limelight) {
        this.limelight = limelight;
    }

    public double getSteeringAdjust() {
        double offset = limelight.getHorizontalTargetOffset();
        // Keep steering adjust between MIN and MAX. set to abs to determine magnitude,
        // but reuse the sign
        double steeringAdjust = Math
                .copySign(Math.max(MIN_SPEED, Math.min(MAX_SPEED, KpAIM * Math.abs(offset))), offset);

        return steeringAdjust;
    }
}
package frc.robot.subsystems.vision;

public class DistanceCalculator {

    private static double map[][] = { { 8.77, 8.0, 6.6, 5.4, 4.65, 4.0, 3.2, 2.8, 2.53, 2.3, 1.8, 1.1, 0.7 },
            { 120.0, 130.0, 140.0, 150.0, 160.0, 170.0, 180.0, 190.0, 200.0, 210.0, 220.0, 230.0, 240.0 } };

    public DistanceCalculator() {
        /*
        ** vertical angle       distance
        ** 8.77	                    120
        ** 8	                    130
        ** 6.6		                140
        ** 5.4		                150
        ** 4.65		                160
        ** 4		                170
        ** 3.2		                180
        ** 2.8		                190
        ** 2.53	    	            200
        ** 2.3		                210
        ** 1.8		                220
        ** 1.1		                230
        ** 0.7		                240
        */

    }

    public static double getDistanceFromTarget(double targetAngleDegrees) {
        double firstAngle = -1.0;
        double secondAngle = -1.0;
        double firstDistance = -1.0;
        double secondDistance = -1.0;

        double distance = 0.0;

        //iterate through the array to find where the value is between two known points
        for (int i = 0; i < map[0].length - 1; ++i) {
            if (map[0][i] <= targetAngleDegrees) {
                firstAngle = map[0][i];
                secondAngle = map[0][i + 1];
                firstDistance = map[1][i];
                secondDistance = map[1][i + 1];
            }
        }
        //If the value is above the max or lower than the mix, nothing happens.

        if (firstAngle > -1.0) {
            // needs to be 1- since angle and distance are inversely correlated
            distance = ((1.0 - (targetAngleDegrees - secondAngle) / (firstAngle - secondAngle))
                    * (secondDistance - firstDistance)) + firstDistance;
        }

        return (distance);
    }

}
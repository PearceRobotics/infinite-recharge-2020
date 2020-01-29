package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class Lights {
    // Docs -
    // https://docs.wpilib.org/en/latest/docs/software/actuators/addressable-leds.html
    private AddressableLED ledStrip;
    private AddressableLEDBuffer ledBuffer;

    private final RGBValues RED = new RGBValues(255, 0, 0);
    private final RGBValues BLUE = new RGBValues(0, 0, 139);
    private final RGBValues LIME_GREEN = new RGBValues(0, 255, 0);

    public Lights(int pwmPort, int numLeds) {
        ledStrip = new AddressableLED(pwmPort);
        ledBuffer = new AddressableLEDBuffer(numLeds);
        ledStrip.setLength(ledBuffer.getLength());
        ledStrip.setData(ledBuffer);
        ledStrip.start();
    }

    public void allBlue() {
        this.setColor(BLUE);
    }

    public void allLimeGreen() {
        this.setColor(LIME_GREEN);
    }

    private void setColor(RGBValues color) {
        for (int x = 0; x < ledBuffer.getLength(); x++) {
            ledBuffer.setRGB(x, color.getRed(), color.getGreen(), color.getBlue());
        }

        ledStrip.setData(ledBuffer);
    }

    public void idleAnimation() {
        for (int x = ledBuffer.getLength() - 1; x > 0; x--) {
            ledBuffer.setRGB(ledBuffer.getLength() - x, RED.getRed(), RED.getGreen(), RED.getBlue());
            ledBuffer.setRGB(x, RED.getRed(), RED.getGreen(), RED.getBlue());
            ledStrip.setData(ledBuffer);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ledBuffer.setRGB(ledBuffer.getLength() - x, BLUE.getRed(), BLUE.getGreen(), BLUE.getBlue());
            ledBuffer.setRGB(x, BLUE.getRed(), BLUE.getGreen(), BLUE.getBlue());
            ledStrip.setData(ledBuffer);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

class RGBValues {
    private int red;
    private int green;
    private int blue;

    public RGBValues(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }
}


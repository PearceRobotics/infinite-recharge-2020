package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color8Bit;

public class Lights {
    // Docs -
    // https://docs.wpilib.org/en/latest/docs/software/actuators/addressable-leds.html
    private AddressableLED ledStrip;
    private AddressableLEDBuffer ledBuffer;

    private final Color8Bit RED = new Color8Bit(255, 0, 0);
    private final Color8Bit BLUE = new Color8Bit(0, 0, 139);
    private final Color8Bit LIME_GREEN = new Color8Bit(0, 255, 0);

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

    private void setColor(Color8Bit color) {
        for (int x = 0; x < ledBuffer.getLength(); x++) {
            ledBuffer.setLED(x, color);
        }

        ledStrip.setData(ledBuffer);
    }

    public void idleAnimation() {
        for (int x = ledBuffer.getLength() - 2; x > 0; x--) {
            ledBuffer.setLED(ledBuffer.getLength() - x, RED);
            ledBuffer.setLED(x, RED);
            ledBuffer.setLED(ledBuffer.getLength() - x + 1, RED);
            ledBuffer.setLED(x + 1, RED);
            ledStrip.setData(ledBuffer);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ledBuffer.setLED(ledBuffer.getLength() - x, BLUE);
            ledBuffer.setLED(x, BLUE);
            ledBuffer.setLED(ledBuffer.getLength() - x + 1, BLUE);
            ledBuffer.setLED(x + 1, BLUE);
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


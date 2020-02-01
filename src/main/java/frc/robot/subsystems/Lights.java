package frc.robot.subsystems;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color8Bit;

public class Lights {
    // Docs -
    //
    private AddressableLED ledStrip;
    private AddressableLEDBuffer ledBuffer;
    private long delay;

    private final Color8Bit RED = new Color8Bit(255, 0, 0);
    private final Color8Bit BLUE = new Color8Bit(0, 0, 139);
    private final Color8Bit LIME_GREEN = new Color8Bit(0, 255, 0);

    private ThreadPoolExecutor executor;

    public Lights(int pwmPort, int numLeds, long delay) {
        ledStrip = new AddressableLED(pwmPort);
        ledBuffer = new AddressableLEDBuffer(numLeds);
        this.delay = delay;
        ledStrip.setLength(ledBuffer.getLength());
        ledStrip.setData(ledBuffer);
        ledStrip.start();
        executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
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

    public void idleAnimation(int length) {
        if(executor.getPoolSize() == 0) {
            executor.submit(() -> {
                for (int x = ledBuffer.getLength() - length; x > 1; x--) {
                    for(int y = 0; y < length; y++) {
                        ledBuffer.setLED((ledBuffer.getLength() - length) - x + y, RED);
                        ledBuffer.setLED(x + y, RED);
                    }
                    ledStrip.setData(ledBuffer);
                    for(int y = 0; y < length; y++) {
                        ledBuffer.setLED((ledBuffer.getLength()-length) - x + y, BLUE);
                        ledBuffer.setLED(x + y, BLUE);
                    }
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                executor.shutdown();
            });
        }
    }
}


package frc.robot.subsystems.lights;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color8Bit;

public class Lights {
    private AddressableLED ledStrip;
    private AddressableLEDBuffer ledBuffer;
    private long delay;

    private boolean isAutonOn = false;

    private final Color8Bit PURPLE = new Color8Bit(255, 0, 255);
    private final Color8Bit ORANGE = new Color8Bit(255, 165, 0);
    private final Color8Bit CYAN = new Color8Bit(13, 240, 203);
    private final Color8Bit YELLOW = new Color8Bit(255, 255, 0);
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
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
    }

    public Color8Bit getBlue(){
        return BLUE;
    }

    public Color8Bit getRed(){
        return RED;
    }

    public Color8Bit getGreen(){
        return LIME_GREEN;
    }

    public Color8Bit getYellow(){
        return YELLOW;
    }

    public void setIsAutonOn(boolean isAutonOn){
        this.isAutonOn = isAutonOn;
    }

    public boolean isAutonOn(){
        return isAutonOn;
    }

    public void allBlue() {
        this.setColor(BLUE);
    }

    public void allOrange(){
        this.setColor(ORANGE);
    }

    public void allPurple() {
        this.setColor(PURPLE);
    }

    public void allYellow() {
        this.setColor(YELLOW);
    }

    public void allCyan() {
        this.setColor(CYAN);
    }

    public void allLimeGreen() {
        shutdownAndRestartThreadPool();
        if(executor.getPoolSize() == 0) {
            executor.submit(() -> {
                try {
                    this.setColor(LIME_GREEN);
                } catch (Exception e) {

                }
            });
        }
    }

    private void setColor(Color8Bit color) {
        for (int x = 0; x < ledBuffer.getLength(); x++) {
            ledBuffer.setLED(x, color);
        }

        ledStrip.setData(ledBuffer);
    }

    private void shutdownAndRestartThreadPool() {
        if(executor.getPoolSize() > 0) {
            executor.shutdownNow();
            executor.purge();
            executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        } 
    }

    public void idleAnimation(int length, Color8Bit stripColor, Color8Bit animColor) {
        shutdownAndRestartThreadPool();
        if(executor.getPoolSize() == 0) {
            executor.submit(() -> {
                try {
                    while(true) {
                        for (int x = ledBuffer.getLength() - length; x > 1; x--) {
                            for(int y = 0; y < length; y++) {
                                ledBuffer.setLED((ledBuffer.getLength() - length) - x + y, animColor);
                                ledBuffer.setLED(x + y, animColor);
                            }
                            ledStrip.setData(ledBuffer);
                            for(int y = 0; y < length; y++) {
                                ledBuffer.setLED((ledBuffer.getLength()-length) - x + y, stripColor);
                                ledBuffer.setLED(x + y, stripColor);
                            }
                            Thread.sleep(delay);
                        }
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                }
            });
        }
    }
}


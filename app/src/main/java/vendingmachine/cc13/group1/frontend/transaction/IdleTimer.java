package vendingmachine.cc13.group1.frontend.transaction;

import org.beryx.textio.ReadHandlerData;
import org.beryx.textio.ReadInterruptionStrategy;
import org.beryx.textio.TextTerminal;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

/**
 * Generates a separate thread that tracks time. 
 * 
 * @author Darin Huang (dhua3771)
 */
public class IdleTimer implements Runnable {

    private long start;
    private long current;
    private Thread thread;
    private boolean stop;
    private long elapsed;
    private long localStart;

    private final long time;

    /**
     * @param terminal the textio terminal
     * @param ms the time in milliseconds for the timer
     */
    public IdleTimer(TextTerminal<?> terminal, long ms) {
        this.time = ms;
        this.stop = false;
        terminal.registerHandler("alt Z", a -> {
            return new ReadHandlerData(ReadInterruptionStrategy.Action.ABORT)
                    .withPayload("Idle timer expired");
        });
        terminal.registerHandler("ctrl T", a -> {
            long remaining = ms - getElapsed();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining);
            long minMs = TimeUnit.MINUTES.toMillis(minutes);
            long secRe = remaining - minMs;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(secRe);
            terminal.executeWithPropertiesPrefix("help",
                    tt -> tt.printf("\n\nTime remaining: %dm:%ds", minutes, seconds));
            return new ReadHandlerData(ReadInterruptionStrategy.Action.RESTART)
                    .withRedrawRequired(true);
        });
        
        this.thread = new Thread(this, "Idle Timer");
        this.thread.setDaemon(true);
        this.elapsed = 0;
    }

    @Override
    public void run() {
        this.start = System.currentTimeMillis();
        this.elapsed = current - start;
        localStart = start;
        long localElapsed = elapsed;
        while(!stop && elapsed < time) {
            try {
                Thread.sleep(200);
                this.current = System.currentTimeMillis();
                this.elapsed = current - start;
                localElapsed = current - localStart;
                if (localElapsed >= 30000) {
                    Robot r;
                    try {
                        r = new Robot();
                        r.keyPress(KeyEvent.VK_CONTROL);
                        r.keyPress(KeyEvent.VK_T);
                        r.delay(100);
                        r.keyRelease(KeyEvent.VK_T);
                        r.keyRelease(KeyEvent.VK_CONTROL);
                    } catch (AWTException e) {
                    }
                    localStart = current;
                }
            } catch (InterruptedException e) {
            }
        }
        if (!stop) {
            Robot r;
            try {
                r = new Robot();
                r.keyPress(KeyEvent.VK_ALT);
                r.keyPress(KeyEvent.VK_Z);
                r.delay(100);
                r.keyRelease(KeyEvent.VK_Z);
                r.keyRelease(KeyEvent.VK_ALT);
            } catch (AWTException e) {
            }
            this.stop = true;
            this.thread = new Thread(this, "Idle Timer");
            this.thread.setDaemon(true);
        }
    }

    public void start() {
        this.thread.start();
    }

    public void reset() {
        this.start = System.currentTimeMillis();
        this.localStart = start;
    }

    public void stop() {
        this.stop = true;
    }

    public boolean hasStopped() {
        return stop;
    }

    public long getElapsed() {
        return elapsed;
    }

}

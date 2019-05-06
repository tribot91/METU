import java.util.*;
import java.util.stream.*;

/**
 * Abstract agent class that shares all common 
 * functions and values of Smelter, Constructor,
 * and Transporter classes.
 */
public abstract class Agent implements Runnable {
	public int ID, Interval;

	public Agent(int agentID, int agentInterval) {
		ID = agentID;
		Interval = agentInterval;
	}

	public synchronized void randomInterval(int interval) {
		Random random = new Random(System.currentTimeMillis());
		DoubleStream stream;
		stream = random.doubles(1, interval - interval * 0.01, interval + interval * 0.02);
		try {
			Thread.sleep((long) stream.findFirst().getAsDouble());
		} catch (InterruptedException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
import java.util.*;
import java.util.concurrent.locks.*;

/**
 * Constructor class
 */
public class Constructor extends Agent implements Runnable {
	public int Capacity, IngotType;
	public int copperCount = 0, ironCount = 0, ironPlate = 0, copperWire = 0;
	public boolean active = true;
	public boolean timeout = false;
	public ArrayList<Transporter> tArr;
	private final Object lock;
	private ReentrantLock selflock = new ReentrantLock();

	public Constructor(int cID, int IC, int CC, int TC, ArrayList<Transporter> tArr, Object lock) {
		super(cID, IC);
		Capacity = CC;
		IngotType = TC;
		this.tArr = tArr;
		this.lock = lock;
	}

	@Override
	public void run() {
		HW2Logger.WriteOutput(0, 0, ID, Action.CONSTRUCTOR_CREATED);
		while (true) {
			WaitIngots();
			if (timeout == true) {
				// System.out.printf("3 sec timeout --------------------.\n");
				break;
			}
			HW2Logger.WriteOutput(0, 0, ID, Action.CONSTRUCTOR_STARTED);
			randomInterval(Interval);
			ContructorProduced();
			HW2Logger.WriteOutput(0, 0, ID, Action.CONSTRUCTOR_FINISHED);
		}
		ContructorStopped();
		HW2Logger.WriteOutput(0, 0, ID, Action.CONSTRUCTOR_STOPPED);
	}

	// Marks the constructor out of simulation so that transporters who are
	// delivering this constructor can quit.
	public void WaitIngots() {
		long time = System.nanoTime() / 1000000;
		synchronized (lock) {
			while (IngotType == 0 && ironCount < 2 && System.nanoTime() / 1000000 - time < 3000) {
				try {
					lock.wait(time + 3000 - System.nanoTime() / 1000000);
				} catch (InterruptedException ex) {
					System.out.println(ex.getMessage());
				}
			}
			while (IngotType == 1 && copperCount < 3 && System.nanoTime() / 1000000 - time < 3000) {
				try {
					lock.wait(time + 3000 - System.nanoTime() / 1000000);
				} catch (InterruptedException ex) {
					System.out.println(ex.getMessage());
				}
			}
			if (System.nanoTime() / 1000000 - time >= 3000) {
				timeout = true;
				// break;
			}
		}

		// reserve the storage spaces until the production is finished.
		selflock.lock();
	}

	// Signals available transporters that storage spaces have been opened in this
	// constructor.
	public void ContructorProduced() { // synchronized
		try {
			if (IngotType == 0) {
				ironCount--;
				ironCount--;
				ironPlate++;
			} else {
				copperCount--;
				copperCount--;
				copperCount--;
				copperWire++;
			}
		} finally {
			selflock.unlock();
		}
	}

	public void ContructorStopped() {
		// Marks the constructor out of simulation so that transporters who are
		// delivering this constructor can quit.
		
		synchronized (lock) {
			active = false;
			lock.notifyAll();
		}
	}
}
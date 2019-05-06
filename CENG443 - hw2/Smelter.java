import java.util.*;
import java.util.concurrent.locks.*;

/**
 * Smelter class
 */
public class Smelter extends Agent implements Runnable {
	public int IngotType, Capacity, TotalIngot;
	public int producedCount = 0;
	public int currentCount = 0;
	public boolean active = true;
	public ArrayList<Transporter> tArr;
	private final Object lock;
	private ReentrantLock selflock = new ReentrantLock();

	public Smelter(int sID, int IS, int CS, int TS, int RS, ArrayList<Transporter> tArr, Object lock) {
		super(sID, IS);
		Capacity = CS;
		IngotType = TS;
		TotalIngot = RS;
		this.tArr = tArr;
		this.lock = lock;
	}

	@Override
	public void run() {
		HW2Logger.WriteOutput(ID, 0, 0, Action.SMELTER_CREATED);
		while (producedCount != TotalIngot) {
			WaitCanProduce();
			HW2Logger.WriteOutput(ID, 0, 0, Action.SMELTER_STARTED);
			randomInterval(Interval);
			IngotProduced();
			HW2Logger.WriteOutput(ID, 0, 0, Action.SMELTER_FINISHED);
			randomInterval(Interval);
		}

		SmelterStopped();
		HW2Logger.WriteOutput(ID, 0, 0, Action.SMELTER_STOPPED);
	}

	public void WaitCanProduce() {
		synchronized (lock) {
			while (Capacity == currentCount) {
				try {
					lock.wait();
				} catch (InterruptedException ex) {
					System.out.println("WAIT =====> " + ex.getMessage());
				}
			}
		}
		// reserve a storage space for the next ingot before production.
		selflock.lock();
	}

	public void IngotProduced() {
		try {
			producedCount++;
			currentCount++;
		} finally {
			selflock.unlock();
		}

		// Informs available transporters that there is available ingots in the
		// smelter’s storage.
		synchronized (lock) {
			lock.notify();
		}
	}

	public void SmelterStopped() {
		// Signal the transporters waiting smelter that smelter has stopped producing
		synchronized (lock) {
			active = false;
			lock.notifyAll();
		}

		// If no available storage for all waiting transporters, extra transporters
		// quit. - This happens automatically by Executor and upper conditions
	}
}
import java.util.*;
import java.util.concurrent.locks.*;

/**
 * Transporter class
 */
public class Transporter extends Agent implements Runnable {
	public int Smelter, Constructor;
	public ArrayList<Smelter> sArr;
	public ArrayList<Constructor> cArr;
	private final Object stLock, ctLock;
	private Lock selflock1 = new ReentrantLock();
	private Lock selflock2 = new ReentrantLock();

	public Transporter(int tID, int IT, int ST, int CT, ArrayList<Smelter> sArr, ArrayList<Constructor> cArr,
			Object stLock, Object ctLock, Lock selflock1, Lock selflock2) {
		super(tID, IT);
		Smelter = ST;
		Constructor = CT;
		this.sArr = sArr;
		this.cArr = cArr;
		this.stLock = stLock;
		this.ctLock = ctLock;
		this.selflock1 = selflock1;
		this.selflock2 = selflock2;
	}

	@Override
	public void run() {
		HW2Logger.WriteOutput(0, ID, 0, Action.TRANSPORTER_CREATED);
		Smelter s = sArr.get(Smelter - 1);
		Constructor c = cArr.get(Constructor - 1);
		Constructor tempConstructor;
		Smelter tempSmelter;
		while ((s.active || s.currentCount > 0) && c.active) {
			WaitNextLoad();

			if (s.currentCount == 0) {
				selflock1.unlock(); // 1
				break;
			}
			
			// HW2Logger.WriteOutput(s.ID, TransporterInfo, 0, Action.TRANSPORTER_TRAVEL)
			// TransporterInfo ???????????????????????????
			HW2Logger.WriteOutput(s.ID, ID, 0, Action.TRANSPORTER_TRAVEL);
			randomInterval(Interval);
			HW2Logger.WriteOutput(s.ID, ID, 0, Action.TRANSPORTER_TAKE_INGOT);
			randomInterval(Interval);
			Loaded(s);
			WaitConstructor(c);
			HW2Logger.WriteOutput(0, ID, c.ID, Action.TRANSPORTER_TRAVEL);
			randomInterval(Interval);
			HW2Logger.WriteOutput(0, ID, c.ID, Action.TRANSPORTER_DROP_INGOT);
			randomInterval(Interval);
			Unloaded(c);
		}

		HW2Logger.WriteOutput(0, ID, 0, Action.TRANSPORTER_STOPPED);
	}

	public void WaitNextLoad() {
		synchronized (stLock) {
			while (sArr.get(Smelter - 1).currentCount == 0) {
				try {
					stLock.wait();
				} catch (InterruptedException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
		selflock1.lock(); // 1
	}

	public void WaitConstructor(Constructor c) {
		synchronized (ctLock) {
			while (c.copperCount + c.ironCount == c.Capacity) {
				try {
					ctLock.wait();
				} catch (InterruptedException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
		// Once unblocked, storage is reserved so that no other transporters can fill
		// that storage space.
		selflock2.lock(); // 2
	}

	public void Loaded(Smelter s) {
		try {
			s.currentCount--;
		} finally {
			selflock1.unlock(); // 1
		}

		synchronized (stLock) {
			stLock.notifyAll();
		}
	}

	public void Unloaded(Constructor c) {
		try {
			if (c.IngotType == 0)
				c.ironCount++;
			else
				c.copperCount++;
		} finally {
			selflock2.unlock(); // 2
		}

		synchronized (ctLock) {
			ctLock.notifyAll();
		}
	}
}
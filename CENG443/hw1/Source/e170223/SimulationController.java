import java.util.*;

/**
 * Controller method of simulation.
 * 
 */
public class SimulationController {
    private final double height;
    private final double width;

    public ArrayList<SimulationObject> bulletArr = new ArrayList<SimulationObject>();
    public ArrayList<SimulationObject> soldierArr = new ArrayList<SimulationObject>();
    public ArrayList<SimulationObject> zombieArr = new ArrayList<SimulationObject>();
    public int bulletCount = 0;

    public SimulationController(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    /**
     * Make all instances to take one step
     */
    public void stepAll() {
        for (var i = 0; i < bulletArr.size(); i++)
            bulletArr.get(i).step(this);

        if (isFinished())
            return;

        for (var i = 0; i < soldierArr.size(); i++)
            soldierArr.get(i).step(this);

        if (isFinished())
            return;

        for (var i = 0; i < zombieArr.size(); i++)
            zombieArr.get(i).step(this);

        if (isFinished())
            return;
    }

    /**
     * Add new instances to simulation.
     * 
     * @param obj Obj that will be added to simulation
     */
    public void addSimulationObject(SimulationObject obj) {
        if (obj.getClass().equals(new RegularSoldier("regularSoldier", new Position(0, 0)).getClass()))
            soldierArr.add(obj);
        else if (obj.getClass().equals(new Commando("commando", new Position(0, 0)).getClass()))
            soldierArr.add(obj);
        else if (obj.getClass().equals(new Sniper("sniper", new Position(0, 0)).getClass()))
            soldierArr.add(obj);
        else if (obj.getClass().equals(new RegularZombie("regular", new Position(0, 0)).getClass()))
            zombieArr.add(obj);
        else if (obj.getClass().equals(new SlowZombie("slow", new Position(0, 0)).getClass()))
            zombieArr.add(obj);
        else if (obj.getClass().equals(new FastZombie("fast", new Position(0, 0)).getClass()))
            zombieArr.add(obj);
        else {
            bulletArr.add(obj);
            bulletCount++;
        }
    }

    /**
     * Remove existing instances from simulation.
     * 
     * @param obj Obj that will be removed from simulation
     */
    public void removeSimulationObject(SimulationObject obj) {
        if (obj.getClass().equals(new RegularSoldier("regularSoldier", new Position(0, 0)).getClass()))
            soldierArr.remove(obj);
        else if (obj.getClass().equals(new Commando("commando", new Position(0, 0)).getClass()))
            soldierArr.remove(obj);
        else if (obj.getClass().equals(new Sniper("sniper", new Position(0, 0)).getClass()))
            soldierArr.remove(obj);
        else if (obj.getClass().equals(new RegularZombie("regular", new Position(0, 0)).getClass()))
            zombieArr.remove(obj);
        else if (obj.getClass().equals(new SlowZombie("slow", new Position(0, 0)).getClass()))
            zombieArr.remove(obj);
        else if (obj.getClass().equals(new FastZombie("fast", new Position(0, 0)).getClass()))
            zombieArr.remove(obj);
        else
            bulletArr.remove(obj);
    }

    /**
     * Check if simulation is ended.
     */
    public boolean isFinished() {
        return (soldierArr.size() == 0 || zombieArr.size() == 0);
    }
}
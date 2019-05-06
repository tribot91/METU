import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main method that starts the simulation
 *
 */
public class SimulationRunner {
    public static void main(String[] args) {
        SimulationController simulation = new SimulationController(50, 50);

        // simulation.addSimulationObject(new RegularSoldier("Regular_Soldier", new Position(10, 10)));
        // simulation.addSimulationObject(new SlowZombie("SlowZombie", new Position(40, 40)));
        // simulation.addSimulationObject(new Commando("Commando", new Position(20, 20)));
        // simulation.addSimulationObject(new RegularZombie("Regular_Zombie", new Position(35, 35)));
        // simulation.addSimulationObject(new Sniper("Sniper", new Position(5, 5)));
        // simulation.addSimulationObject(new FastZombie("Fast_Zombie", new Position(45, 45)));

        while (!simulation.isFinished()) {
            simulation.stepAll();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(SimulationRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
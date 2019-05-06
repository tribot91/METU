/**
 * Sniper class that inherits Soldier abstract class
 * 
 */
public class Sniper extends Soldier {
    /**
     * Sniper constructor
     */
    public Sniper(String name, Position position) { // DO NOT CHANGE PARAMETERS
        super(name, position, 2.0);
        setType(SoldierType.SNIPER);
        setCollisionRange(5.0);
        setShootingRange(40.0);
    }

    @Override
    public void step(SimulationController controller) {
        double w = controller.getWidth();
        double h = controller.getHeight();
        double x = getPosition().getX() + getDirection().getX() * getSpeed();
        double y = getPosition().getY() + getDirection().getY() * getSpeed();

        if (getState() == SoldierState.SEARCHING) {
            if (x > w || x < 0 || y > h || y < 0) {
                Position random = getRandomDirection();
                setDirection(random);
                System.out.println(getName() + " changed direction to (" + round(random.getX()) + ", "
                        + round(random.getY()) + ").");
            } else {
                setPosition(new Position(x, y));
                System.out.println(getName() + " moved to (" + round(x) + ", " + round(y) + ").");
            }
            System.out.println(getName() + " changed state to AIMING.");
        } else if (getState() == SoldierState.AIMING) {
            int index = findClosest(controller.zombieArr, getShootingRange());
            if (index != -1) {
                setState(SoldierState.SHOOTING);
                System.out.println(getName() + " changed state to SHOOTING.");
                setDirection(findDirection(getPosition(), controller.zombieArr.get(index).getPosition()));
                System.out.println(getName() + " changed direction to (" + round(getDirection().getX()) + ", "
                        + round(getDirection().getY()) + ").");
            } else {
                setState(SoldierState.SEARCHING);
                System.out.println(getName() + " changed state to SEARCHING.");
            }
        } else { // SHOOTING
            System.out.println(getName() + " fired " + "Bullet" + controller.bulletCount + " to direction ("
                    + round(getDirection().getX()) + ", " + round(getDirection().getY()) + ").");
            controller.addSimulationObject(new Bullet(controller.bulletCount, getPosition(), 100.0, getDirection()));

            int index = findClosest(controller.zombieArr, getShootingRange());

            if (index != -1) {
                setState(SoldierState.AIMING);
                System.out.println(getName() + " changed state to AIMING.");
            } else {
                Position random = getRandomDirection();
                setDirection(random);
                System.out.println(getName() + " changed direction to (" + round(random.getX()) + ", "
                        + round(random.getY()) + ").");
                setState(SoldierState.SEARCHING);
                System.out.println(getName() + " changed state to SEARCHING.");
            }
        }
    }

}

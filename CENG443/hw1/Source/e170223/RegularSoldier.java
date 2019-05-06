/**
 * RegularSoldier class that inherits Soldier abstract class
 *
 */
public class RegularSoldier extends Soldier {
    /**
     * RegularSoldier constructor
     */
    public RegularSoldier(String name, Position position) { // DO NOT CHANGE PARAMETERS
        super(name, position, 5.0);
        setType(SoldierType.REGULAR);
        setCollisionRange(2.0);
        setShootingRange(20.0);
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

            // distance to zombie calculate, if close enough SoldierState.AIMING
            if (findClosest(controller.zombieArr, getShootingRange()) != -1) {
                setState(SoldierState.AIMING);
                System.out.println(getName() + " changed state to AIMING.");
            }
        } else if (getState() == SoldierState.AIMING) {
            int index = findClosest(controller.zombieArr, getShootingRange());
            // distance to zombie calculate, if distance is close, change direction to
            // zombie
            // change state to SoldierState.SHOOTING and normalize the direction
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
            // Create a bullet
            // Add the bullet to the simulation after all step functions are executed.
            System.out.println(getName() + " fired " + "Bullet" + controller.bulletCount + " to direction ("
                    + round(getDirection().getX()) + ", " + round(getDirection().getY()) + ").");
            controller.addSimulationObject(new Bullet(controller.bulletCount, getPosition(), 40.0, getDirection()));
            // turn all "not actives" to "active" at the end of turn ( Sanirim bunu
            // hallettim controllerda)

            // Find the closest zombie
            int index = findClosest(controller.zombieArr, getShootingRange());

            // if close, change SoldierState.AIMING
            if (index != -1) {
                setState(SoldierState.AIMING);
                System.out.println(getName() + " changed state to AIMING.");
            } else { // else change direction randomly and SoldierState.SEARCHING
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
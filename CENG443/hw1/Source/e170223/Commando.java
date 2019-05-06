/**
 * Commando class that inherits Soldier abstract class
 *
 */
public class Commando extends Soldier {
    /**
     * Commando constructor
     */
    public Commando(String name, Position position) { // DO NOT CHANGE PARAMETERS
        super(name, position, 10.0);
        setType(SoldierType.COMMANDO);
        setCollisionRange(2.0);
        setShootingRange(10.0);
    }

    @Override
    public void step(SimulationController controller) {
        double w = controller.getWidth();
        double h = controller.getHeight();
        double x = getPosition().getX() + getDirection().getX() * getSpeed();
        double y = getPosition().getY() + getDirection().getY() * getSpeed();

        if (getState() == SoldierState.SEARCHING) {
            int index = findClosest(controller.zombieArr, getShootingRange());

            if (index != -1) {
                setState(SoldierState.SHOOTING);
                System.out.println(getName() + " changed state to SHOOTING.");
                setDirection(findDirection(getPosition(), controller.zombieArr.get(index).getPosition()));
                System.out.println(getName() + " changed direction to (" + round(getDirection().getX()) + ", "
                        + round(getDirection().getY()) + ").");
            } else {
                if (x > w || x < 0 || y > h || y < 0) {
                    Position random = getRandomDirection();
                    setDirection(random);
                    System.out.println(getName() + " changed direction to (" + round(random.getX()) + ", "
                            + round(random.getY()) + ").");
                } else {
                    setPosition(new Position(x, y));
                    System.out.println(getName() + " moved to (" + round(x) + ", " + round(y) + ").");
                }

                int index2 = findClosest(controller.zombieArr, getShootingRange());

                if (index2 != -1) {
                    setState(SoldierState.SHOOTING);
                    System.out.println(getName() + " changed state to SHOOTING.");
                    setDirection(findDirection(getPosition(), controller.zombieArr.get(index2).getPosition()));
                    System.out.println(getName() + " changed direction to (" + round(getDirection().getX()) + ", "
                            + round(getDirection().getY()) + ").");
                }
            }

        } else if (getState() == SoldierState.SHOOTING) {
            System.out.println(getName() + " fired " + "Bullet" + controller.bulletCount + " to direction ("
                    + round(getDirection().getX()) + ", " + round(getDirection().getY()) + ").");
            controller.addSimulationObject(new Bullet(controller.bulletCount, getPosition(), 40.0, getDirection()));

            int index = findClosest(controller.zombieArr, getShootingRange());
            if (index != -1) {
                setDirection(findDirection(getPosition(), controller.zombieArr.get(index).getPosition()));
                System.out.println(getName() + " changed direction to (" + round(getDirection().getX()) + ", "
                        + round(getDirection().getY()) + ").");
            } else {
                setState(SoldierState.SEARCHING);
                System.out.println(getName() + " changed state to SEARCHING.");
            }
        }
    }

}

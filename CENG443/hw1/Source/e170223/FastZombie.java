/**
 * FastZombie class that inherits Zombie abstract class
 *
 */
public class FastZombie extends Zombie {
    /**
     * FastZombie constructor
     */
    public FastZombie(String name, Position position) { // DO NOT CHANGE PARAMETERS
        super(name, position, 20.0);
        setType(ZombieType.FAST);
        setCollisionRange(2.0);
        setDetectionRange(20.0);
    }

    @Override
    public void step(SimulationController controller) {
        boolean killed = commonZombieStep(controller);

        if (killed)
            return;

        if (controller.isFinished())
            return;

        double w = controller.getWidth();
        double h = controller.getHeight();
        double x = getPosition().getX() + getDirection().getX() * getSpeed();
        double y = getPosition().getY() + getDirection().getY() * getSpeed();

        if (getState() == ZombieState.WANDERING) {
            int index = findClosest(controller.soldierArr, getDetectionRange());

            if (index != -1) {
                setDirection(findDirection(getPosition(), controller.soldierArr.get(index).getPosition()));
                System.out.println(getName() + " changed direction to (" + round(getDirection().getX()) + ", "
                        + round(getDirection().getY()) + ").");

                setState(ZombieState.FOLLOWING);
                System.out.println(getName() + " changed state to FOLLOWING.");
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
            }
        } else { // FOLLOWING
            if (x > w || x < 0 || y > h || y < 0) {
                Position random = getRandomDirection();
                setDirection(random);
                System.out.println(getName() + " changed direction to (" + round(random.getX()) + ", "
                        + round(random.getY()) + ").");
            } else {
                setPosition(new Position(x, y));
                System.out.println(getName() + " moved to (" + round(x) + ", " + round(y) + ").");
            }
            setState(ZombieState.WANDERING);
            System.out.println(getName() + " changed state to WANDERING.");
        }
    }
}

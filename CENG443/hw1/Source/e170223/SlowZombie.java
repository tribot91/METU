/**
 * SlowZombie class that inherits Zombie abstract class
 *
 */
public class SlowZombie extends Zombie {
    /**
     * SlowZombie constructor
     */
    public SlowZombie(String name, Position position) { // DO NOT CHANGE PARAMETERS
        super(name, position, 2.0);
        setType(ZombieType.SLOW);
        setCollisionRange(1.0);
        setDetectionRange(40.0);
    }

    @Override
    public void step(SimulationController controller) {
        commonZombieStep(controller);

        if (controller.isFinished())
            return;

        double w = controller.getWidth();
        double h = controller.getHeight();
        double x = getPosition().getX() + getDirection().getX() * getSpeed();
        double y = getPosition().getY() + getDirection().getY() * getSpeed();

        if (getState() == ZombieState.WANDERING) {
            if (findClosest(controller.soldierArr, getDetectionRange()) != -1) {
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
            int index = findClosest(controller.soldierArr, getDetectionRange());

            if (index != -1) {
                setDirection(findDirection(getPosition(), controller.soldierArr.get(index).getPosition()));
                System.out.println(getName() + " changed direction to (" + round(getDirection().getX()) + ", "
                        + round(getDirection().getY()) + ").");
            }

            x = getPosition().getX() + getDirection().getX() * getSpeed();
            y = getPosition().getY() + getDirection().getY() * getSpeed();

            if (x > w || x < 0 || y > h || y < 0) {
                Position random = getRandomDirection();
                setDirection(random);
                System.out.println(getName() + " changed direction to (" + round(random.getX()) + ", "
                        + round(random.getY()) + ").");
            } else {
                setPosition(new Position(x, y));
                System.out.println(getName() + " moved to (" + round(x) + ", " + round(y) + ").");
            }
            if (index != -1) {
                setState(ZombieState.WANDERING);
                System.out.println(getName() + " changed state to WANDERING.");
            }

        }
    }
}

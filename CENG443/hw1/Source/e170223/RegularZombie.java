/**
 * RegularZombie class that inherits Zombie abstract class
 * 
 */
public class RegularZombie extends Zombie {
    private int stepCount = 0;

    /**
     * RegularZombie constructor
     */
    public RegularZombie(String name, Position position) { // DO NOT CHANGE PARAMETERS
        super(name, position, 5.0);
        setType(ZombieType.REGULAR);
        setCollisionRange(2.0);
        setDetectionRange(20.0);
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

        if (x > w || x < 0 || y > h || y < 0) {
            Position random = getRandomDirection();
            setDirection(random);
            System.out.println(
                    getName() + " changed direction to (" + round(random.getX()) + ", " + round(random.getY()) + ").");
        } else {
            setPosition(new Position(x, y));
            System.out.println(getName() + " moved to (" + round(x) + ", " + round(y) + ").");
        }

        if (getState() == ZombieState.WANDERING) {
            int index = findClosest(controller.soldierArr, getDetectionRange());
            // distance to soldier calculate, if close enough ZombieState.FOLLOWING
            if (index != -1) {
                setState(ZombieState.FOLLOWING);
                System.out.println(getName() + " changed state to FOLLOWING.");
                setDirection(findDirection(getPosition(), controller.soldierArr.get(index).getPosition()));
                System.out.println(getName() + " changed direction to (" + round(getDirection().getX()) + ", "
                        + round(getDirection().getY()) + ").");
            }
        } else { // FOLLOWING
            stepCount++;
            if (stepCount == 4) {
                stepCount = 0;
                setState(ZombieState.WANDERING);
                System.out.println(getName() + " changed state to WANDERING.");
            }
        }
    }
}
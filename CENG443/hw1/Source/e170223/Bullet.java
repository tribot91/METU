/**
 * Bullet class that inherits SimulationObject abstract class
 *
 */
public class Bullet extends SimulationObject {
    public Bullet(int number, Position position, double speed, Position direction) { // add speed
        super("Bullet" + number, position, speed);
        setDirection(direction);
    }

    /**
     * Step function of bullet that iterates the
     */
    @Override
    public void step(SimulationController controller) {
        Position oldPos = getPosition();
        Position dir = getDirection();

        for (int i = 0; i < getSpeed(); i++) {
            double newPosX = oldPos.getX() + dir.getX() * i;
            double newPosY = oldPos.getY() + dir.getY() * i;

            if (newPosX < 0 || newPosX > controller.getWidth() || newPosY < 0 || newPosY > controller.getHeight()) {
                System.out.println(getName() + " moved out of bounds.");
                controller.removeSimulationObject(this);
                return;
            }
            setPosition(updatedPosition(oldPos, dir, i));

            for (int j = 0; j < controller.zombieArr.size(); j++) {
                SimulationObject selectedZombie = controller.zombieArr.get(j);

                boolean check = checkHit(selectedZombie.getPosition(), selectedZombie.getCollisionRange());
                if (check) {
                    System.out.println(getName() + " hit " + selectedZombie.getName() + ".");
                    controller.removeSimulationObject(selectedZombie);
                    controller.removeSimulationObject(this);

                    return;
                }
            }
        }
        System.out.println(getName() + " dropped to the ground at (" + round(getPosition().getX()) + ", "
                + round(getPosition().getY()) + ").");
        controller.removeSimulationObject(this);

    }

    /**
     * @return Boolean depending on if bullet hits the given zombie
     */

    public boolean checkHit(Position zombiePos, double collisionRange) {
        return euclideanDistance(zombiePos, getPosition()) < getCollisionRange();
    }
}

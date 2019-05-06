import java.util.*;

/**
 * SimulationObject will be the main abstract class that we will inherit Soldier
 * and Zombie abstract classes and Bullet class from
 */
public abstract class SimulationObject {
    private final String name;
    private Position position;
    private Position direction;
    private final double speed;
    private boolean active;
    private double collisionRange;

    /**
     * Bullet constructor
     */
    public SimulationObject(String name, Position position, double speed) {
        this.name = name;
        this.position = position;
        this.speed = speed;
        this.direction = null;
        this.active = true;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getDirection() {
        return direction;
    }

    public void setDirection(Position direction) {
        this.direction = direction;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isActive() {
        return active;
    }

    public void setStatus(boolean status) {
        this.active = status;
    }

    public double getCollisionRange() {
        return this.collisionRange;
    }

    public void setCollisionRange(double collisionRange) {
        this.collisionRange = collisionRange;
    }

    /**
     * @return Random normalized direction
     */
    public Position getRandomDirection() {
        double x = Math.random() * 2 - 1;
        double y = Math.random() * 2 - 1;
        double newx = x / Math.sqrt(x * x + y * y);
        double newy = y / Math.sqrt(x * x + y * y);
        return new Position(newx, newy);
    }

    /**
     * @return Distance between two positions
     */
    public double euclideanDistance(Position p1, Position p2) {
        return Math.sqrt(Math.pow((p1.getX() - p2.getX()), 2) + Math.pow((p1.getY() - p2.getY()), 2));
    }

    /**
     * @return Normalized vector
     */
    public Position normalize(Position p) {
        double length = Math.sqrt(Math.pow(p.getX(), 2) + Math.pow(p.getY(), 2));
        return new Position(p.getX() / length, p.getY() / length);
    }

    /**
     * @return Normalized direction vector
     */
    public Position findDirection(Position p1, Position p2) {
        Position p = new Position(p2.getX() - p1.getX(), p2.getY() - p1.getY());
        return normalize(p);
    }

    /**
     * @param i Iteration count of bullet
     * @return New position for bullet since bullet goes 1.0 per iteration
     */
    public Position updatedPosition(Position p, Position direction, int i) {
        return new Position(p.getX() + direction.getX() * i, p.getY() + direction.getY() * i);
    }

    /**
     * @return Rounded number with 2 significants
     */
    public double round(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    /**
     * @param arr   Array of zombies or soldiers
     * @param range Range is either shootingRange of soldiers or detectionRange of
     *              zombies
     * @return Index of the enemy that is inside the range, if there is no such
     *         enemy, then -1
     */
    public int findClosest(ArrayList<SimulationObject> arr, double range) {
        double closestZombieDist = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < arr.size(); i++) {
            double temp = euclideanDistance(arr.get(i).getPosition(), getPosition());
            if (temp < closestZombieDist) {
                closestZombieDist = temp;
                index = i;
            }
        }
        if (closestZombieDist <= range)
            return index;
        return -1;
    }

    public abstract void step(SimulationController controller);
}

/**
 * Abstract class for RegularZombie, FastZombie and SlowZombie classes.
 */
public abstract class Zombie extends SimulationObject {
  private double detectionRange;
  private ZombieType type;
  private ZombieState state;

  /**
   * Zombie constructor
   */
  public Zombie(String name, Position position, double speed) {
    super(name, position, speed);
    setDirection(getRandomDirection());
    state = ZombieState.WANDERING;
  }

  public ZombieType getType() {
    return this.type;
  }

  public void setType(ZombieType type) {
    this.type = type;
  }

  public double getDetectionRange() {
    return this.detectionRange;
  }

  public void setDetectionRange(double detectionRange) {
    this.detectionRange = detectionRange;
  }

  public ZombieState getState() {
    return this.state;
  }

  public void setState(ZombieState state) {
    this.state = state;
  }

  /**
   * Common behaviour that performed by all zombie types at the start of the step
   * method
   */
  public boolean commonZombieStep(SimulationController controller) {
    int closestSoldierIndex = findClosest(controller.soldierArr, detectionRange);
    if (closestSoldierIndex != -1) {
      SimulationObject closestSoldier = controller.soldierArr.get(closestSoldierIndex);
      if (getCollisionRange() + closestSoldier.getCollisionRange() > euclideanDistance(getPosition(),
          closestSoldier.getPosition())) {
        System.out.println(this.getName() + " killed " + closestSoldier.getName() + ".");
        controller.removeSimulationObject(closestSoldier);
        return true;
      }
    }
    return false;
  }
}

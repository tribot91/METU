/**
 * Abstract class for RegularSoldier, Commando and Sniper classes.
 */
public abstract class Soldier extends SimulationObject {
  private double shootingRange;
  private SoldierType type;
  private SoldierState state;

  /**
   * Soldier constructor
   */
  public Soldier(String name, Position position, double speed) {
    super(name, position, speed);
    setDirection(getRandomDirection());
    state = SoldierState.SEARCHING;
  }

  public SoldierType getType() {
    return this.type;
  }

  public void setType(SoldierType type) {
    this.type = type;
  }

  public double getShootingRange() {
    return this.shootingRange;
  }

  public void setShootingRange(double shootingRange) {
    this.shootingRange = shootingRange;
  }

  public SoldierState getState() {
    return this.state;
  }

  public void setState(SoldierState state) {
    this.state = state;
  }
}

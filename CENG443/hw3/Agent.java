/**
 * Agent class is for special MazeObjects that
 * can move and has gold.
 */
public class Agent extends MazeObject {
    private static final long serialVersionUID = 4L;
    private final int id;
    private int collectedGold;
    
    public Agent(Position position, int id) {
        super(position, MazeObjectType.AGENT);
        this.id = id;
        this.collectedGold = 0;
    }

    public int getId() {
        return id;
    }

    public int getGold() {
        return collectedGold;
    }

    public void incrGold() {
        collectedGold++;
    }
}
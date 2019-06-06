import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Maze class creates 2D array and has 
 */
public class Maze implements IMaze, Serializable {
    private static final long serialVersionUID = 1L;
    public MazeObject maze[][];
    public ArrayList<MazeObject> temp = new ArrayList<MazeObject>();
    public ArrayList<Agent> AgentList = new ArrayList<Agent>();

    protected Maze() throws RemoteException {
    }

    /**
     * Creates maze
     */
    public void create(int height, int width) {
        maze = new MazeObject[height][width];
        for (int y = 0; y < height; y++) {
            temp.clear();
            for (int x = 0; x < width; x++) {
                MazeObject tempMazeObj = new MazeObject(new Position(x, y), null);
                maze[y][x] = tempMazeObj;
            }
        }
    }

    /**
     * Returns maze
     */
    public MazeObject getObject(Position position) {
        MazeObject tempMazeObj = maze[position.getY()][position.getX()];
        if (tempMazeObj.getType() != null)
            return tempMazeObj;
        return null;
    }

    /**
     * Creates object at 2D maze array
     */
    public boolean createObject(Position position, MazeObjectType type) {
        if (position.getY() < maze.length && position.getX() < maze[0].length) {
            MazeObject tempMazeObj = maze[position.getY()][position.getX()];
            if (tempMazeObj.getType() == null) {
                if (type == MazeObjectType.AGENT) {
                    Agent tempAgent = new Agent(position, AgentList.size() + 1);
                    maze[position.getY()][position.getX()] = tempAgent;
                    AgentList.add(tempAgent);
                } else {
                    maze[position.getY()][position.getX()] = new MazeObject(position, type);
                }
                // System.out.println("Operation Success.\n");
                return true;
            }
            // System.out.println("Operation Failed.\n");
            return false;
        }
        // System.out.println("Operation Failed.\n");
        return false;
    }

    /**
     * Removes object from 2D maze array
     */
    public boolean deleteObject(Position position) {
        MazeObject tempMazeObj = maze[position.getY()][position.getX()];
        if (tempMazeObj.getType() == null) {
            // System.out.println("Operation Failed.\n");
            return false;
        }
        maze[position.getY()][position.getX()] = new MazeObject(position, null);
        // System.out.println("Operation Success.\n");
        return true;
    }

    /**
     * Returns agents
     */
    public ArrayList<Agent> getAgents() {
        return AgentList;
    }

    /**
     * Moves agents on 2D maze array
     */
    public boolean moveAgent(int id, Position position) {
        int agentPos = 0;
        for (int i = 0; i < AgentList.size(); i++) {
            if (AgentList.get(i).getId() == id)
                agentPos = i;
        }

        Position oldpos = AgentList.get(agentPos).getPosition();

        if (maze[position.getY()][position.getX()].getType() == null) {
            if (oldpos.distance(position) == 1) {
                AgentList.get(agentPos).setPosition(position);

                maze[oldpos.getY()][oldpos.getX()] = new MazeObject(oldpos, null);
                maze[position.getY()][position.getX()] = AgentList.get(agentPos);

                // System.out.println("Operation Success.\n");
                return true;
            }
            // System.out.println("Operation Failed.\n");
            return false; // MANHATTAN NOT 1
        } else if (maze[position.getY()][position.getX()].getType() == MazeObjectType.HOLE) {
            AgentList.remove(agentPos);

            maze[oldpos.getY()][oldpos.getX()] = new MazeObject(oldpos, null);

            // System.out.println("Operation Success.\n");
            return true;
        } else if (maze[position.getY()][position.getX()].getType() == MazeObjectType.GOLD) {
            if (oldpos.distance(position) == 1) {
                AgentList.get(agentPos).incrGold();
                AgentList.get(agentPos).setPosition(position);

                maze[oldpos.getY()][oldpos.getX()] = new MazeObject(oldpos, null);
                maze[position.getY()][position.getX()] = AgentList.get(agentPos);

                // System.out.println("Operation Success.\n");
                return true;
            }
            // System.out.println("Operation Failed.\n");
            return false; // MANHATTAN NOT 1
        }
        // System.out.println("Operation Failed.\n");
        return false; // WALL OR AGENT
    }

    /**
     * Prints the 2D maze array with boundaries
     */
    public String print() {
        String result = "";

        for (int i = 0; i <= maze.length + 1; i++) {
            for (int j = 0; j <= maze[0].length + 1; j++) {
                if ((i == 0 && j == 0) || (i == 0 && j == maze[0].length + 1) || (i == maze.length + 1 && j == 0)
                        || (i == maze.length + 1 && j == maze[0].length + 1)) {
                    result = result + "+";
                    if (j == maze[0].length + 1)
                        result = result + '\n';
                } else if (i == 0 || i == maze.length + 1) {
                    result = result + "-";
                } else if (j == 0 || j == maze[0].length + 1) {
                    result = result + "|";
                    if (j == maze[0].length + 1)
                        result = result + '\n';
                } else {
                    if (maze[i - 1][j - 1].getType() == MazeObjectType.WALL) {
                        result = result + 'X';
                    } else if (maze[i - 1][j - 1].getType() == MazeObjectType.AGENT)
                        result = result + 'A';
                    else if (maze[i - 1][j - 1].getType() == MazeObjectType.HOLE)
                        result = result + 'O';
                    else if (maze[i - 1][j - 1].getType() == MazeObjectType.GOLD)
                        result = result + 'G';
                    else
                        result = result + " ";
                }
            }
        }
        System.out.println(result);
        return result;
    }
}
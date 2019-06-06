import java.rmi.*;
import java.util.ArrayList;

/**
 * MazeHub class includes multiples 2D mazes
 */
public class MazeHub implements IMazeHub {
    private static final long serialVersionUID = 200L;
    public static ArrayList<Maze> mazeArr = new ArrayList<Maze>();
    public static ArrayList<Integer> codes = new ArrayList<Integer>();

    public MazeHub() throws RemoteException {
    }

    /**
     * Creates 2D maze
     */
    public void createMaze(int width, int height) throws RemoteException {
        Maze tempMaze = new Maze();
        tempMaze.create(width, height);
        mazeArr.add(tempMaze);
        codes.add(codes.size());
    }

    /**
     * Returns 2D maze with specific index
     */
    public IMaze getMaze(int index) throws RemoteException {
        if (mazeArr.size() > index && index >= 0) {
            return mazeArr.get(index);
        }
        return null;
    }

    /**
     * Updates maze with specific index
     */
    public void updateMaze(Maze selectedMaze, int index) throws RemoteException {
        mazeArr.set(index, selectedMaze);
    }

    /**
     * Removes maze with specific index
     */
    public boolean removeMaze(int index) throws RemoteException {
        if (mazeArr.size() > index && index >= 0) {
            mazeArr.remove(index);
            return true;
        }
        return false;
    }
}
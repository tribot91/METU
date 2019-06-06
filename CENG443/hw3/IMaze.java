import java.util.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for Maze class
 */
public interface IMaze extends Remote {
    void create(int height, int width) throws RemoteException;
    MazeObject getObject(Position position) throws RemoteException;
    boolean createObject(Position position, MazeObjectType type) throws RemoteException;
    boolean deleteObject(Position position) throws RemoteException;
    ArrayList<Agent> getAgents() throws RemoteException;
    boolean moveAgent(int id, Position position) throws RemoteException;
    String print() throws RemoteException;
}

import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

/**
 * Server class
 */
public class RMIServer {
    public RMIServer() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws RemoteException {
        MazeHub impl = new MazeHub();
        // Maze impl2 = new Maze();

        IMazeHub stub = (IMazeHub) UnicastRemoteObject.exportObject((IMazeHub) impl, 0);
        // IMaze stub2 = (IMaze) UnicastRemoteObject.exportObject((IMaze) impl2, 0);

        Registry registry = LocateRegistry.getRegistry();

        registry.rebind("IMazeHub", stub);
        // registry.rebind("IMaze", stub2);
    }
}
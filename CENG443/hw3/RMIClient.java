import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Client class
 */
public class RMIClient {
    public static void main(String[] args) throws AccessException, RemoteException, NotBoundException {
        Scanner scanner = new Scanner(System.in);
        ParsedInput parsedInput = null;
        String input;

        Registry registry = LocateRegistry.getRegistry();
        IMazeHub stub = (IMazeHub) registry.lookup("IMazeHub");

        IMaze selectedMaze = null;
        int current = 0;

        while (true) {
            input = scanner.nextLine();

            try {
                parsedInput = ParsedInput.parse(input);
            } catch (Exception ex) {
                parsedInput = null;
            }

            if (parsedInput == null) {
                System.out.println("Wrong input format. Try again.");
                continue;
            }

            Object[] argss = parsedInput.getArgs();

            try {
                switch (parsedInput.getType()) {
                case CREATE_MAZE:
                    stub.createMaze((int) argss[1], (int) argss[0]);
                    break;
                case DELETE_MAZE:
                    boolean res = stub.removeMaze((int) argss[0]);
                    if (res == false) {
                        System.out.println("Operation Failed.\n");
                        break;
                    }
                    selectedMaze = null;
                    System.out.println("Operation Success.\n");
                    break;
                case SELECT_MAZE:
                    current = (int) argss[0];
                    selectedMaze = stub.getMaze(current);
                    if (selectedMaze == null) {
                        System.out.println("Operation Failed.\n");
                        break;
                    }
                    System.out.println("Operation Success.\n");
                    break;
                case PRINT_MAZE:
                    selectedMaze.print();
                    break;
                case CREATE_OBJECT:
                    if (selectedMaze != null) {
                        boolean res3 = selectedMaze.createObject(new Position((int) argss[0], (int) argss[1]),
                                MazeObjectType.valueOf(input.split(" ")[3].toUpperCase()));
                        stub.updateMaze((Maze) selectedMaze, current);
                        if (res3 == true) {
                            System.out.println("Operation Success.\n");
                            break;
                        } else {
                            System.out.println("Operation Failed.\n");
                            break;
                        }
                    } else {
                        System.out.println("Operation Failed.\n");
                        break;
                    }
                case DELETE_OBJECT:
                    boolean res2 = selectedMaze.deleteObject(new Position((int) argss[0], (int) argss[1]));
                    stub.updateMaze((Maze) selectedMaze, current); // ????
                    if (res2 == false) {
                        System.out.println("Operation Failed.\n");
                        break;
                    }
                    System.out.println("Operation Success.\n");
                    break;
                case LIST_AGENTS:
                    ArrayList<Agent> a = selectedMaze.getAgents();
                    for (int i = 0; i < a.size(); i++) {
                        System.out.println("Agent" + a.get(i).getId() + " at (" + a.get(i).getPosition().getX() + ", "
                                + a.get(i).getPosition().getY() + "). Gold collected: " + a.get(i).getGold() + ".\n");
                    }
                    break;
                case MOVE_AGENT:
                    selectedMaze.moveAgent((int) argss[0], new Position((int) argss[1], (int) argss[2]));
                    stub.updateMaze((Maze) selectedMaze, current);
                    break;
                case QUIT:
                    System.exit(0);
                    break;
                }
            } catch (Exception e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }
}
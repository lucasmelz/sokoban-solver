import java.util.List;
import java.util.Scanner;

public class Main {

    public static Example exampleOne() {

        // _ | S | _ | x | _ | _ | _
        // x | _ | x | _ | _ | _ | x
        // _ | _ | _ | _ | _ | _ | _
        // _ | o | _ | x | _ | _ | _
        // _ | _ | _ | x | _ | _ | _
        // _ | M | _ | x | _ | _ | _
        // _ | A | _ | _ | _ | _ | _

        int dimension = 7;
        Position exit = new Position(0, 1);

        // Initial state configuration
        Position agentPosition = new Position(6, 1);
        Position machinePosition = new Position(5, 1);
        Position[] objectPositions = new Position[] {
                new Position(3, 1),
        };
        Position[] obstaclePositions = new Position[] {
                new Position(0, 3),
                new Position(1, 0),
                new Position(1, 2),
                new Position(1, 6),
                new Position(3, 3),
                new Position(4, 3),
                new Position(5, 3),
        };

        State initialState = new State(agentPosition, machinePosition, obstaclePositions, objectPositions);

        Example example = new Example(dimension, exit, initialState);
        return example;
    }

    public static Example exampleTwo() {

        // _ | _ | x | _ | E | _ | _
        // x | _ | x | _ | _ | _ | x
        // _ | _ | _ | _ | o | _ | _
        // _ | _ | x | _ | _ | _ | _
        // _ | _ | _ | x | _ | _ | _
        // _ | M | _ | x | _ | _ | _
        // _ | A | _ | _ | _ | _ | _

        int dimension = 7;
        Position exit = new Position(0, 4); // Exit is at (0, 4)

        // Initial state configuration
        Position agentPosition = new Position(6, 1); // Agent is at (6, 1)
        Position machinePosition = new Position(5, 1); // Machine is at (5, 1)
        Position[] objectPositions = new Position[] {
                new Position(2, 4), // Object is at (2, 4)
        };
        Position[] obstaclePositions = new Position[] {
                new Position(0, 2), // Obstacle at (0, 2)
                new Position(1, 0), // Obstacle at (1, 0)
                new Position(1, 2), // Obstacle at (1, 2)
                new Position(1, 6), // Obstacle at (1, 6)
                new Position(3, 2), // Obstacle at (3, 2)
                new Position(4, 3), // Obstacle at (4, 3)
                new Position(5, 3), // Obstacle at (5, 3)
        };

        State initialState = new State(agentPosition, machinePosition, obstaclePositions, objectPositions);

        return new Example(dimension, exit, initialState);
    }

    public static Example exampleThree() {

        // _ | _ | x | _ | E | _ | _
        // x | _ | x | _ | _ | _ | x
        // _ | _ | _ | _ | _ | _ | _
        // _ | o | _ | x | _ | _ | _
        // _ | _ | _ | x | _ | _ | _
        // _ | M | _ | x | _ | o | _
        // _ | A | _ | _ | _ | _ | _

        int dimension = 7;
        Position exit = new Position(0, 4);

        // Initial state configuration
        Position agentPosition = new Position(6, 1);
        Position machinePosition = new Position(5, 1);
        Position[] objectPositions = new Position[] {
                new Position(3, 1),
                new Position(5, 5)
        };
        Position[] obstaclePositions = new Position[] {
                new Position(0, 2),
                new Position(1, 0),
                new Position(1, 2),
                new Position(1, 6),
                new Position(3, 3),
                new Position(4, 3),
                new Position(5, 3),
        };

        State initialState = new State(agentPosition, machinePosition, obstaclePositions, objectPositions);

        return new Example(dimension, exit, initialState);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Available examples:");
        System.out.println("Example 1:");
        System.out.println("  _ | S | _ | x | _ | _ | _");
        System.out.println("  x | _ | x | _ | _ | _ | x");
        System.out.println("  _ | _ | _ | _ | _ | _ | _");
        System.out.println("  _ | o | _ | x | _ | _ | _");
        System.out.println("  _ | _ | _ | x | _ | _ | _");
        System.out.println("  _ | M | _ | x | _ | _ | _");
        System.out.println("  _ | A | _ | _ | _ | _ | _");
        System.out.println();

        System.out.println("Example 2:");
        System.out.println("  _ | _ | x | _ | E | _ | _");
        System.out.println("  x | _ | x | _ | _ | _ | x");
        System.out.println("  _ | _ | _ | _ | o | _ | _");
        System.out.println("  _ | _ | x | _ | _ | _ | _");
        System.out.println("  _ | _ | _ | x | _ | _ | _");
        System.out.println("  _ | M | _ | x | _ | _ | _");
        System.out.println("  _ | A | _ | _ | _ | _ | _");
        System.out.println();

        System.out.println("Example 3:");
        System.out.println("  _ | _ | x | _ | E | _ | _");
        System.out.println("  x | _ | x | _ | _ | _ | x");
        System.out.println("  _ | _ | _ | _ | _ | _ | _");
        System.out.println("  _ | o | _ | x | _ | _ | _");
        System.out.println("  _ | _ | _ | x | _ | _ | _");
        System.out.println("  _ | M | _ | x | _ | o | _");
        System.out.println("  _ | A | _ | _ | _ | _ | _");
        System.out.println();

        System.out.println("Select an example (1, 2, or 3):");
        int exampleSelection = scanner.nextInt();

        System.out.println("Select a search algorithm (1 for BFS, 2 for DFS, 3 for AStar):");
        int algorithmSelection = scanner.nextInt();

        Example example = null;
        switch (exampleSelection) {
            case 1:
                example = exampleOne();
                break;
            case 2:
                example = exampleTwo();
                break;
            case 3:
                example = exampleThree();
                break;
            default:
                System.out.println("Invalid example selection.");
                return;
        }

        SokobanProblem problem = new SokobanProblem(example.dimension, example.exit);
        List<State> solutionPath = null;

        switch (algorithmSelection) {
            case 1:
                BreadthFirstSearch<State> bfs = new BreadthFirstSearch<>();
                solutionPath = bfs.search(problem, example.initialState);
                break;
            case 2:
                DepthFirstSearch<State> dfs = new DepthFirstSearch<>();
                solutionPath = dfs.search(problem, example.initialState);
                break;
            case 3:
                AStarSearch aStarSearch = new AStarSearch();
                solutionPath = aStarSearch.search(problem, example.initialState);
                break;
            default:
                System.out.println("Invalid search algorithm selection.");
                return;
        }

        // Print the solution path, if found
        if (solutionPath != null) {
            System.out.println("Solution path:");
            for (State state : solutionPath) {
                System.out.println("Agent: (" + state.agentPosition.x + ", " + state.agentPosition.y +
                        "), Machine: (" + state.machinePosition.x + ", " + state.machinePosition.y + ")");
            }
        } else {
            System.out.println("No solution found.");
        }
    }
}

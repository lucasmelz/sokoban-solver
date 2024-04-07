import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

class State {
    Position agentPosition;
    Position machinePosition;
    Position[] obstaclePositions;
    Position[] objectPositions;

    public State(Position agentPosition, Position machinePosition, Position[] obstaclePositions,
            Position[] objectPositions) {
        this.agentPosition = agentPosition;
        this.machinePosition = machinePosition;
        this.obstaclePositions = obstaclePositions;
        this.objectPositions = objectPositions;
    }

    // Copy constructor
    public State(State state) {
        this.agentPosition = state.agentPosition;
        this.machinePosition = state.machinePosition;
        this.objectPositions = state.objectPositions;
        this.obstaclePositions = state.obstaclePositions;
    }

    // The equals method needs to be overriden so that we can later check if a State
    // was already visited
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        State state = (State) o;
        return Objects.equals(agentPosition, state.agentPosition) &&
                Objects.equals(machinePosition, state.machinePosition) &&
                Arrays.equals(obstaclePositions, state.obstaclePositions) &&
                Arrays.equals(objectPositions, state.objectPositions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(agentPosition, machinePosition);
        result = 31 * result + Arrays.hashCode(obstaclePositions);
        result = 31 * result + Arrays.hashCode(objectPositions);
        return result;
    }
}

class Position {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

public class SokobanProblem implements ProblemSpace<State> {

    int dimension;
    Position exit;

    public SokobanProblem(int dimension, Position exit) {
        this.dimension = dimension;
        this.exit = exit;
    }

    @Override
    public List<Node<State>> generateSuccessors(Node<State> node) {
        List<Node<State>> successors = new ArrayList<>();
        State currentState = node.getState();
        int[][] moves = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } }; // Down, Up, Right, Left

        for (int[] move : moves) {
            int newX = currentState.agentPosition.x + move[0];
            int newY = currentState.agentPosition.y + move[1];
            Position newPosition = new Position(newX, newY);

            if (isFree(newPosition, currentState.obstaclePositions)) {
                State newState = new State(currentState); // Implement a copy constructor or cloning method in State
                newState.agentPosition = newPosition;

                // Check if the new agent position is adjacent to the machine
                if (newPosition.equals(currentState.machinePosition)
                        && canPush(currentState.agentPosition, currentState.machinePosition,
                                currentState.obstaclePositions)) {
                    // Calculate new machine position
                    int newMachineX = currentState.machinePosition.x + move[0];
                    int newMachineY = currentState.machinePosition.y + move[1];
                    Position newMachinePosition = new Position(newMachineX, newMachineY);

                    // Check if the new machine position is free
                    if (isFree(newMachinePosition, currentState.obstaclePositions)) {
                        newState.machinePosition = newMachinePosition;
                        newState.objectPositions = removeObjectIfCaught(newMachinePosition,
                                currentState.objectPositions);
                    }
                }

                successors.add(new Node<>(newState, node, node.getCost() + 1));
            }
        }

        return successors;
    }

    private boolean isFree(Position newPosition, Position[] obstaclePositions) {
        if (!isWithinLimits(newPosition)) {
            return false;
        }

        for (Position obstaclePosition : obstaclePositions) {
            if (newPosition.equals(obstaclePosition)) {
                return false;
            }
        }
        return true;
    }

    private boolean isWithinLimits(Position position) {
        return position.x >= 0 && position.y >= 0 && position.x < this.dimension && position.y < this.dimension;
    }

    private boolean canPush(Position agentPosition, Position machinePosition, Position[] obstaclePositions) {
        // Calculate the direction of the push
        int deltaX = machinePosition.x - agentPosition.x;
        int deltaY = machinePosition.y - agentPosition.y;

        // Ensure the agent is directly adjacent to the machine
        if (Math.abs(deltaX) > 1 || Math.abs(deltaY) > 1) {
            return false;
        }

        // Calculate the new position of the machine after being pushed
        Position newMachinePosition = new Position(machinePosition.x + deltaX, machinePosition.y + deltaY);

        // Check if the new position is within the grid and not blocked by obstacles or
        // objects
        if (!isWithinLimits(newMachinePosition)) {
            return false; // New position is out of bounds
        }

        for (Position obstaclePosition : obstaclePositions) {
            if (newMachinePosition.equals(obstaclePosition)) {
                return false; // New position is blocked by an obstacle
            }
        }

        // The space beyond the machine is free, and the push is valid
        return true;
    }

    // This method checks if the machine's new position catches an object and
    // updates the objectPositions array accordingly
    private Position[] removeObjectIfCaught(Position machinePosition, Position[] objectPositions) {
        List<Position> updatedObjectPositions = new ArrayList<>(Arrays.asList(objectPositions));
        updatedObjectPositions.removeIf(machinePosition::equals);
        return updatedObjectPositions.toArray(new Position[0]);
    }

    @Override
    public boolean isGoal(Node<State> node) {
        return node.getState().objectPositions.length == 0 && node.getState().machinePosition.equals(this.exit);
    }

}

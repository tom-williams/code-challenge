package dk.kelvin.williams;

import dk.kelvin.williams.model.Direction;
import dk.kelvin.williams.model.Maze;
import dk.kelvin.williams.model.MazeField;
import dk.kelvin.williams.model.MazeMap;
import dk.kelvin.williams.services.PonyChallengeService;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Getter
@Setter
public class MazeRunner {
  private static final Logger LOGGER = LogManager.getLogger(MazeRunner.class.getName());

  private MazeMap mazeMap;
  private Maze maze;
  private UUID mazeId;
  private PonyChallengeService service;

  public MazeRunner() {
    this.service = new PonyChallengeService();
  }

  public void startGame() throws IOException {
    this.setMazeId(service.createMaze());
    this.setMaze(service.getMaze(mazeId));
    this.setMazeMap(new MazeMap(maze));
  }

  public void navigateMaze() throws IOException {
    List<Direction> directions = mazeMap.solveMaze();

    LOGGER.debug("Solution found with " + directions.size() + " steps");

    while (gameActive(directions)) {
      Direction direction = directions.get(0);

      Map<String, String> moveDirection = getMoveDirection(direction);
      if (isDomokunAtNextStep(direction)) {
        moveDirection = stayMoveDirection();
      } else {
        directions.remove(0);
      }
      service.move(mazeId, moveDirection);

      LOGGER.debug("Move: " + moveDirection.get("direction"));

      resetMazeMap();
    }

    LOGGER.debug("Game State: " + maze.getGameState().getState());
  }

  boolean gameActive(List<Direction> directions) {
    return maze.getGameState().getState().equalsIgnoreCase("active") && directions.size() > 0;
  }

  void resetMazeMap() throws IOException {
    this.setMaze(service.getMaze(mazeId));
    mazeMap.setDomokunLocation(mazeMap.getMazeFields().get(maze.getDomokun().get(0)));
    mazeMap.setPonyLocation(mazeMap.getMazeFields().get(maze.getPony().get(0)));
  }

  boolean isDomokunAtNextStep(Direction direction) {
    MazeField nextField = mazeMap.getPonyLocation().getAccessibleNeighbor(direction);
    return mazeMap.getDomokunLocation().equals(nextField);
  }

  Map<String, String> stayMoveDirection() {
    return Collections.singletonMap("direction", "stay");
  }

  Map<String, String> getMoveDirection(Direction direction) {
    return  Collections.singletonMap("direction", direction.name().toLowerCase());
  }
}

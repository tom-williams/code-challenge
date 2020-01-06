package dk.kelvin.williams.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
public class MazeMap {
  private static final Logger LOGGER = LogManager.getLogger(MazeMap.class.getName());

  private Map<Integer, MazeField> mazeFields;

  private MazeField ponyLocation;
  private MazeField endPointLocation;

  private MazeField domokunLocation;

  private int mazeWidth;

  public MazeMap(Maze maze) {
    this.mazeWidth = maze.getSize().get(1);

    this.mazeFields = mapFields(maze);
    mapAccessibleNeigbors();

    this.ponyLocation = mazeFields.get(maze.getPony().get(0));
    this.endPointLocation = mazeFields.get(maze.getEndPoint().get(0));
    this.domokunLocation = mazeFields.get(maze.getDomokun().get(0));
  }


  /**
   * Find path from Pony to End point ignoring Domokun location
   *
   * @return A Directions list to get to end of maze
   */
  public List<Direction> solveMaze() {
    MazeField currentMazeField = traversePathToMazeEnd(ponyLocation.visit(ponyLocation));

    // Backtrack over found path.
    List<Direction> directions = new ArrayList<>();
    while (!currentMazeField.equals(ponyLocation)) {
      // retrieve direction of neighbor this node was visited from
      directions.add(Direction.reverseDirection(currentMazeField.getVisitorDirection()));
      currentMazeField = currentMazeField.getVisitedFromLocation();
    }

    Collections.reverse(directions);
    return directions;
  }

  /**
   * Recursively traverse the MazeFields, until we find the end point of the MazeMap
   *
   *  1. Go forward to any unvisited neighbor. Mark node as visited.
   *  2. Stop if you reach end point
   *  3. If no unvisited neighbor left, go back one step.
   *  4. Repeat from #1
   *
   * @param currentMazeField The current position in the MazeMap
   * @return the MazeField to go next, or if we have reached the end, then the end MazeField
   */
  public MazeField traversePathToMazeEnd(MazeField currentMazeField) {
    LOGGER.debug("current mazeField: " + currentMazeField.getLocation());
    if (currentMazeField.equals(endPointLocation)) {
      // We are done
      return currentMazeField;
    }

    MazeField nextField = currentMazeField.nextField();
    LOGGER.debug("Next field: " + nextField.getLocation() + ", isVisited: " + nextField.isVisited());
    if (!nextField.isVisited()) {
      LOGGER.debug("Visit field: " + nextField.getLocation());
      nextField.visit(currentMazeField);
    }

    return traversePathToMazeEnd(nextField);
  }

  private void mapAccessibleNeigbors() {
    mazeFields.values().forEach(mazeField -> mazeField.mapAccessibleNeighbors(mazeFields, mazeWidth));
  }

  private Map<Integer, MazeField> mapFields(Maze maze) {
    List<List<Direction>> mapData = maze.getData();
    List<Integer> mapSize = maze.getSize();

    // Create maze fields
    List<MazeField> mazeFields = IntStream.range(0, mapData.size())
        .mapToObj(MazeField::new)
        .peek(field -> field.mapWalls(mapData, mapSize))
        .collect(Collectors.toList());

    return mazeFields.stream()
        .collect(Collectors.toMap(MazeField::getLocation, Function.identity()));
  }
}

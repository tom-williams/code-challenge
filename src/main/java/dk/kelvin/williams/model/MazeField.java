package dk.kelvin.williams.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Getter
public class MazeField {
  private Set<Direction> wallLocations;
  private int location;
  private boolean visited;
  private MazeField visitedFromLocation;
  private List<NeighborField> accessibleNeighbors;

  public MazeField(int location) {
    this.location = location;
    this.wallLocations = new TreeSet<>();
    this.accessibleNeighbors = new ArrayList<>();
    this.visited = false;
    this.visitedFromLocation = null;
  }

  /**
   * For this MazeField node, map all the walls that surround it, to block off all non-accessible paths
   *
   * @param mapData The raw map data returned by the Pony API
   * @param mapSize The height and the width of the Map
   */
  public void mapWalls(List<List<Direction>> mapData, List<Integer> mapSize) {
    // Map the North and West walls provided by the MapData for this location
    mapData.get(location).forEach(this::addWallLocation);

    handleEasternWalls(mapData, mapSize.get(1));
    handleSouthernWalls(mapData, mapSize);
  }


  /**
   * Mark this MazeField as visited, and set the MazeField from which the visitation occurs,
   * as the visitedFromLocation.
   *
   * @param visitedFromLocation MazeField from which this MazeField node has been visited from
   * @return this MazeField object
   */
  public MazeField visit(MazeField visitedFromLocation) {
    this.visitedFromLocation = visitedFromLocation;
    this.visited = true;
    return this;
  }

  public void addWallLocation(Direction wallLocation) {
    this.wallLocations.add(wallLocation);
  }

  public void mapAccessibleNeighbors(Map<Integer, MazeField> mazeFields, int mazeWidth) {
    // For directions without a wall, map neighbor
    EnumSet.allOf(Direction.class).forEach(direction -> {
      if (!wallLocations.contains(direction)) {
        int neighborLocation = neighborLocation(direction, mazeWidth);
        accessibleNeighbors.add(NeighborField.builder()
            .neighbor(mazeFields.get(neighborLocation))
            .directionOfNeighbor(direction)
            .build()
        );
      }
    });
  }

  public MazeField getAccessibleNeighbor(Direction direction) {
    return accessibleNeighbors.stream()
        .filter(neighbor -> neighbor.directionOfNeighbor.equals(direction))
        .map(NeighborField::getNeighbor)
        .findFirst()
        .orElse(null);
  }

  /**
   * @return The Direction from this MazeField node to the MazeField node from which
   * this MazeField was visited.
   */
  public Direction getVisitorDirection() {
    return accessibleNeighbors.stream()
        .filter(neighbor -> neighbor.getNeighbor().equals(visitedFromLocation))
        .map(neighbor -> neighbor.directionOfNeighbor)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("No Neighbor as visitor"));
  }

  /**
   * Returns any unvisited accessible neighbor MazeField, or if none exist,
   * returns the MazeField from which this MazeField was visited from.
   *
   * @return The MazeField to move to next
   */
  public MazeField nextField() {
    return accessibleNeighbors.stream()
        .filter(neighbor -> !neighbor.getNeighbor().isVisited())
        .map(NeighborField::getNeighbor)
        .findFirst()
        .orElse(visitedFromLocation);
  }

  int neighborLocation(Direction direction, int mazeWidth) {
    switch(direction) {
      case EAST:
        return location + 1;
      case WEST:
        return location - 1;
      case NORTH:
        return location - mazeWidth;
      case SOUTH:
        return location + mazeWidth;
      default:
        return -1;
    }
  }

  void handleEasternWalls(List<List<Direction>> mapData, int mazeWidth) {
    if (isRightBoundaryOfMap(mazeWidth)) {
      addWallLocation(Direction.EAST);
    } else {
      mapData.get(location + 1).forEach(direction -> {
        if (direction.equals(Direction.WEST)) {
          addWallLocation(Direction.reverseDirection(direction));
        }
      });
    }
  }

  void handleSouthernWalls(List<List<Direction>> mapData, List<Integer> mapSize) {
    if (isBottomBoundaryOfMap(mapSize)) {
      addWallLocation(Direction.SOUTH);
    } else {
      mapData.get(location + mapSize.get(1)).forEach(direction -> {
        if (direction.equals(Direction.NORTH)) {
          addWallLocation(Direction.reverseDirection(direction));
        }
      });
    }
  }

  boolean isRightBoundaryOfMap(int width) {
    return (location + 1) % width == 0;
  }

  boolean isBottomBoundaryOfMap(List<Integer> mapSize) {
    return location >= mapSize.get(0) * mapSize.get(1) - mapSize.get(1);
  }
}

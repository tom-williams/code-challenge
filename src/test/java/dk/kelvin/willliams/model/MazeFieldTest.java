package dk.kelvin.willliams.model;

import dk.kelvin.williams.model.Direction;
import dk.kelvin.williams.model.MazeField;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MazeFieldTest {

  @Test
  public void mazeFieldOnBottomRowShouldMapSouthernMazeBoundary() {
    List<List<Direction>> mapData = threeByThreeMapData();
    List<Integer> mapSize = threeByThreeMapSize();

    MazeField bottomMiddleField = new MazeField(7);
    bottomMiddleField.mapWalls(mapData, mapSize);

    assertEquals(2, bottomMiddleField.getWallLocations().size());
    Set<Direction> wallLocations = bottomMiddleField.getWallLocations();
    assertTrue(wallLocations.contains(Direction.SOUTH));
  }

  @Test
  public void mazeFieldShouldMapEasternMazeBoundary() {
    List<List<Direction>> mapData = threeByThreeMapData();
    List<Integer> mapSize = threeByThreeMapSize();

    MazeField middleRightMostField = new MazeField(5);
    middleRightMostField.mapWalls(mapData, mapSize);

    assertEquals(2, middleRightMostField.getWallLocations().size());
    Set<Direction> wallLocations = middleRightMostField.getWallLocations();
    assertTrue(wallLocations.contains(Direction.EAST));
  }

  @Test
  public void mazeFieldShouldMapNeighborWallToSouth() {
    List<List<Direction>> mapData = threeByThreeMapData();
    List<Integer> mapSize = threeByThreeMapSize();

    MazeField middleMiddleField = new MazeField(4);
    middleMiddleField.mapWalls(mapData, mapSize);

    assertEquals(3, middleMiddleField.getWallLocations().size());
    Set<Direction> wallLocations = middleMiddleField.getWallLocations();
    assertTrue(wallLocations.contains(Direction.SOUTH));
  }

  @Test
  public void mazeFieldShouldMapNeighborWallToEast() {
    List<List<Direction>> mapData = threeByThreeMapData();
    List<Integer> mapSize = threeByThreeMapSize();

    MazeField middleMiddleField = new MazeField(4);
    middleMiddleField.mapWalls(mapData, mapSize);

    assertEquals(3, middleMiddleField.getWallLocations().size());
    Set<Direction> wallLocations = middleMiddleField.getWallLocations();
    assertTrue(wallLocations.contains(Direction.EAST));
  }

  /*
   * Should create the following:
   *    _________
   *   |  |     |
   *   |  |__|  |
   *   |________|
   */
  private List<List<Direction>> threeByThreeMapData() {
    return Stream.of(
        Stream.of(Direction.NORTH, Direction.WEST).collect(Collectors.toList()),
        Stream.of(Direction.NORTH, Direction.WEST).collect(Collectors.toList()),
        Stream.of(Direction.NORTH).collect(Collectors.toList()),
        Stream.of(Direction.WEST).collect(Collectors.toList()),
        Stream.of(Direction.WEST).collect(Collectors.toList()),
        Stream.of(Direction.WEST).collect(Collectors.toList()),
        Stream.of(Direction.WEST).collect(Collectors.toList()),
        Stream.of(Direction.NORTH).collect(Collectors.toList()),
        Collections.<Direction> emptyList()
    ).collect(Collectors.toList());
  }

  private List<Integer> threeByThreeMapSize() {
    return Stream.of(3, 3).collect(Collectors.toList());
  }
}

package dk.kelvin.williams.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NeighborField {
  Direction directionOfNeighbor;
  MazeField neighbor;
}

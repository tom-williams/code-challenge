package dk.kelvin.williams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum Direction {
  @JsonProperty("north")
  NORTH("north"),

  @JsonProperty("south")
  SOUTH("south"),

  @JsonProperty("east")
  EAST("east"),

  @JsonProperty("west")
  WEST("west");

  private String location;

  Direction(String location) {
    this.location = location;
  }

  public static Direction reverseDirection(Direction original) {
    switch(original) {
      case NORTH: return Direction.SOUTH;
      case SOUTH: return Direction.NORTH;
      case WEST: return Direction.EAST;
      case EAST: return Direction.WEST;
    }
    return null;
  }
}

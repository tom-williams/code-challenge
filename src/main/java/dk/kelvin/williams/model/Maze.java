package dk.kelvin.williams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Maze {

  private List<List<Direction>> data;

  private List<Integer> pony;
  private List<Integer> domokun;

  @JsonProperty("end-point")
  private List<Integer> endPoint;

  private List<Integer> size;
  private Integer difficulty;

  @JsonProperty("maze-id")
  private String mazeId;

  @JsonProperty("game-state")
  private GameState gameState;

  @Getter
  @Setter
  public static class GameState {
    private String state;

    @JsonProperty("state-result")
    private String stateResult;
  }

}

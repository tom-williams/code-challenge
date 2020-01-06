package dk.kelvin.williams.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MazeParameters {
  @JsonProperty("maze-width")
  private int mazeWidth;

  @JsonProperty("maze-height")
  private int mazeHeight;

  @JsonProperty("maze-player-name")
  private String mazePlayerName;

  @JsonProperty("difficulty")
  private int difficulty;
}

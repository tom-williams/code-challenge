package dk.kelvin.williams.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.kelvin.williams.model.Maze;
import dk.kelvin.williams.model.MazeParameters;
import dk.kelvin.williams.rest.PonyChallengeAPI;
import lombok.Getter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Service that wraps PonyChallengeAPI in a Retrofit2 implementation.
 */
@Getter
public class PonyChallengeService {
  private static final String REST_URI = "https://ponychallenge.trustpilot.com/pony-challenge/";

  private PonyChallengeAPI service;

  public PonyChallengeService() {
    ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    Retrofit retrofit = new Retrofit.Builder()
        .addConverterFactory(JacksonConverterFactory.create(mapper))
        .baseUrl(REST_URI)
        .build();

    service = retrofit.create(PonyChallengeAPI.class);
  }

  public UUID createMaze() throws IOException {
    return this.createMaze(defaultMazeParameters());
  }
  public UUID createMaze(MazeParameters mazeParameters) throws IOException {
    if (mazeParameters == null) {
      throw new IllegalArgumentException("Maze parameters cannot be null");
    }

    Response<JsonNode> response = service.createMaze(mazeParameters).execute();

    if (!response.isSuccessful()) {
      String errorMsg = response.errorBody() != null ? response.errorBody().string() : "Error calling createMaze Pony API";
      throw new RuntimeException(errorMsg);
    }

    JsonNode mazeIdNode = response.body().get("maze_id");
    if (mazeIdNode == null) {
      throw new RuntimeException("Pony API didn't return maze_id");
    }

    return UUID.fromString(mazeIdNode.asText());
  }

  public Maze getMaze(UUID mazeId) throws IOException {
      if (mazeId == null) {
        throw new IllegalArgumentException("Maze id cannot be null");
      }
      Response<Maze> response = service.getMaze(mazeId).execute();

    if (!response.isSuccessful()) {
      String errorMsg = response.errorBody() != null ? response.errorBody().string() : "Error calling getMaze on Pony API";
      throw new RuntimeException(errorMsg);
    }

    return response.body();
  }

  public void move(UUID mazeId, Map<String, String> direction) throws IOException {
    if (mazeId == null) {
      throw new IllegalArgumentException("Maze id cannot be null");
    }
    Response<Void> response = service.move(mazeId, direction).execute();

    if (!response.isSuccessful()) {
      String errorMsg = response.errorBody() != null ? response.errorBody().string() : "Error calling getMaze on Pony API";
      throw new RuntimeException(errorMsg);
    }

  }

  private MazeParameters defaultMazeParameters() {
    return MazeParameters.builder()
        .mazeHeight(15)
        .mazeWidth(15)
        .difficulty(0)
        .mazePlayerName("Applejack")
        .build();
  }
}

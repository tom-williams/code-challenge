package dk.kelvin.williams.rest;

import com.fasterxml.jackson.databind.JsonNode;
import dk.kelvin.williams.model.Maze;
import dk.kelvin.williams.model.MazeParameters;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;
import java.util.UUID;

public interface PonyChallengeAPI {
  @Headers({ "Accept: application/json", "Content-Type: application/json" })
  @POST("maze")
  Call<JsonNode> createMaze(@Body MazeParameters mazeParameters);

  @Headers("Accept: application/json")
  @GET("maze/{id}")
  Call<Maze> getMaze(@Path("id") UUID id);

  @Headers({ "Accept: application/json", "Content-Type: application/json" })
  @POST("maze/{id}")
  Call<Void> move(@Path("id") UUID id, @Body Map<String, String> direction);
}

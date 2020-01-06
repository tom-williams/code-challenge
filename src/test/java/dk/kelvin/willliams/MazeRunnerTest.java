package dk.kelvin.willliams;

import dk.kelvin.williams.MazeRunner;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MazeRunnerTest {

  @Test
  public void navigatingMazeShouldEndWithAWonGame() throws IOException {
    MazeRunner testMazeRunner = new MazeRunner();
    testMazeRunner.startGame();
    testMazeRunner.navigateMaze();

    assertEquals("won", testMazeRunner.getMaze().getGameState().getState().toLowerCase());
  }
}

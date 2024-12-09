package org.sber;

import org.junit.jupiter.api.Test;

import java.io.File;

class RockPaperScissorsEngineTest {

    @Test
    void play() {
        File dir = new File("./src/test/resources/");
        RockPaperScissorsEngine rockPaperScissorsEngine = new RockPaperScissorsEngine(dir);
        rockPaperScissorsEngine.run();
    }
}
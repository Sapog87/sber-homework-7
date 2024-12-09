package org.sber;

import java.util.Random;

public class Player implements PlayableRockPaperScissors {
    private final Random rand = new Random();

    @Override
    public RockPaperScissorsEnum play() {
        RockPaperScissorsEnum[] rps = RockPaperScissorsEnum.values();
        int index = rand.nextInt(rps.length);
        return rps[index];
    }
}

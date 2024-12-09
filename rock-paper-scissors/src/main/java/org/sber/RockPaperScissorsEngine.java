package org.sber;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import static org.sber.RockPaperScissorsEnum.*;

@Slf4j
@RequiredArgsConstructor
public class RockPaperScissorsEngine implements Runnable {
    private final File dir;

    @Override
    public void run() {
        File[] files = dir.listFiles(file -> file.isFile() && file.getName().endsWith(".jar"));
        if (files == null || files.length == 0) {
            throw new RuntimeException("Не удалось найти jar в %s".formatted(dir));
        }

        String currentWinnerName = files[0].getName();
        PlayableRockPaperScissors currentPlayer = getPlayerFromJar(files[0]);
        for (int i = 1; i < files.length; i++) {
            String nextPlayerName = files[i].getName();
            PlayableRockPaperScissors nextPlayer = getPlayerFromJar(files[i]);
            log.info("Игра {}: {} vs {}", i, currentWinnerName, nextPlayerName);

            boolean isFirstWinner = battle(currentPlayer, nextPlayer);
            if (!isFirstWinner) {
                currentWinnerName = nextPlayerName;
                currentPlayer = nextPlayer;
            }
            log.info("В игре {} победил {}", i, currentWinnerName);
            log.info("");
        }
        log.info("Итог: победил {}", currentWinnerName);
    }


    /**
     * @param jar где искать игрока
     * @return инстанс игрока
     */
    private PlayableRockPaperScissors getPlayerFromJar(File jar) {
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{jar.toURI().toURL()})) {
            Class<?> clazz = classLoader.loadClass("org.sber.Player");
            Object object = clazz.getDeclaredConstructor().newInstance();
            if (object instanceof PlayableRockPaperScissors plugin) {
                return plugin;
            }
            throw new RuntimeException("Игрок должен реализовывать PlayableRockPaperScissors");
        } catch (ReflectiveOperationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param firstPlayer  первый игрок
     * @param secondPlayer второй игрок
     * @return <code>true</code> – победил первый игрок
     */
    private boolean battle(PlayableRockPaperScissors firstPlayer, PlayableRockPaperScissors secondPlayer) {
        int result = 0;
        for (int i = 0; i < 3; i++) {
            boolean isFirstWinner = oneRound(firstPlayer, secondPlayer);

            log.info("В раунде {} победил {} игрок", i, isFirstWinner ? "первый" : "второй");

            result = isFirstWinner ? result + 1 : result - 1;
        }
        return result > 0;
    }

    /**
     * @param firstPlayer  первый игрок
     * @param secondPlayer второй игрок
     * @return <code>true</code> – победил первый игрок
     */
    private boolean oneRound(PlayableRockPaperScissors firstPlayer, PlayableRockPaperScissors secondPlayer) {
        while (true) {
            RockPaperScissorsEnum firstPlayerChoice = firstPlayer.play();
            RockPaperScissorsEnum secondPlayerChoice = secondPlayer.play();

            if (firstPlayerChoice == secondPlayerChoice) {
                log.info("Оба игрока выкинули {} - переигровка", firstPlayerChoice);
                continue;
            }

            log.info("Первый выкинул: {}, второй выкинул: {}", firstPlayerChoice, secondPlayerChoice);
            return isFirstWin(firstPlayerChoice, secondPlayerChoice);
        }
    }

    /**
     * @param firstPlayer  первый игрок
     * @param secondPlayer второй игрок
     * @return <code>true</code> – победил первый игрок
     */
    private boolean isFirstWin(RockPaperScissorsEnum firstPlayer, RockPaperScissorsEnum secondPlayer) {
        switch (firstPlayer) {
            case ROCK -> {
                return secondPlayer == SCISSORS;
            }
            case PAPER -> {
                return secondPlayer == ROCK;
            }
            case SCISSORS -> {
                return secondPlayer == PAPER;
            }
            default -> throw new RuntimeException("Невозможное состояние");
        }
    }
}
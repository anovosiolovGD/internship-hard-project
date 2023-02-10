package Blockchain;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        final var blockchain = Blockchain.getInstance();
        final var nThreads = Runtime.getRuntime().availableProcessors();
        final var executor = Executors.newFixedThreadPool(nThreads);
        IntStream.range(0, nThreads).mapToObj(i -> new Miner(blockchain, String.valueOf(i))).forEach(executor::submit);
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
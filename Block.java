package Blockchain;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;


import static Blockchain.util.StringUtils.applySha256;

public class Block implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String minerId;
    private final int id;
    private final long timestamp;
    private final String prevBlockHash;
    private long magicNumber;
    private String blockHash;
    private int timeToGenerate;

    private Block(String minerId, int id, String prevBlockHash) {
        this.minerId = minerId;
        this.id = id;
        this.prevBlockHash = prevBlockHash;
        this.timestamp = new Date().getTime();
    }

    public static Block getUnproved(String minerId, int id, String prevBlockHash) {
        return new Block(minerId, id, prevBlockHash);
    }

    public int getId() {
        return id;
    }

    public String getPrevBlockHash() {
        return prevBlockHash;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public int getTimeToGenerate() {
        return timeToGenerate;
    }

    @Override
    public String toString() {
        return String.format("""
                        Block:\s
                        Created by miner # %s\s
                        Id: %d\s
                        Timestamp: %d\s
                        Magic number: %d\s
                        Hash of the previous block:\s
                        %s\s
                        Hash of the block:\s
                        %s\s
                        Block was generating for %d seconds""",
                minerId,
                id,
                timestamp,
                magicNumber,
                prevBlockHash,
                blockHash,
                timeToGenerate);
    }

    public void prove(int zeroes) {
        final var startTime = Instant.now();
        findMagicNumber(zeroes);
        timeToGenerate = Math.toIntExact(Duration.between(startTime, Instant.now()).toSeconds());
    }

    private void findMagicNumber(int zeroes) {
        var hash = "";
        do {
            magicNumber = ThreadLocalRandom.current().nextLong();
            hash = applySha256(stringify());
        } while (!isProved(zeroes, hash));
        blockHash = hash;
    }

    public boolean isProved(int zeroes) {
        final var hash = applySha256(stringify());
        if (blockHash.equals(hash)) return isProved(zeroes, hash);
        return false;
    }

    private boolean isProved(int zeroes, String blockHash) {
        return IntStream.range(0, zeroes).allMatch(i -> blockHash.charAt(i) == '0');
    }

    private String stringify() {
        return "" +
                minerId +
                id +
                timestamp +
                magicNumber +
                prevBlockHash;
    }
}

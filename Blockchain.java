package Blockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: 30/12/2020 reimplement serialization features
public class Blockchain implements Serializable {
    //    private static final transient String FILE_PATH = "./Database.txt";
    private static final long serialVersionUID = 1L;
    private final List<Block> blocks;
    private final List<Integer> zeroesList;

    private Blockchain(List<Integer> zeroesList) {
        this.blocks = new ArrayList<>();
        this.zeroesList = zeroesList;
    }

    public static Blockchain getInstance() {
        final var zeroesList = new ArrayList<Integer>();
        zeroesList.add(0);
        return new Blockchain(zeroesList);
//        try {
//            final var blockchain = (Blockchain) deserialize(FILE_PATH);
//            return blockchain.isValid() ? blockchain : new Blockchain(zeroes);
//        } catch (ClassNotFoundException e) {
//            e.getMessage();
//            System.out.println("Nothing found on the database. Generating a new blockchain...");
//            return new Blockchain(zeroes);
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Something went wrong on restoring the object. Generating a new blockchain...");
//            return new Blockchain(zeroes);
//        }
    }

    @Override
    public String toString() {
        final var stringBuilder = new StringBuilder();
        for (var block : blocks) {
            stringBuilder.append(block).append("\n\n");
        }
        return String.valueOf(stringBuilder);
    }

    public int getNextZeroes() {
        if (zeroesList.get(zeroesList.size() - 1) > 3) return 3;
        else return zeroesList.get(zeroesList.size() - 1);
    }

    public int getNextBlockId() {
        return blocks.size() + 1;
    }

    public Block getLastBlock() {
        return blocks.get(blocks.size() - 1);
    }

    public String getLastBlockHash() {
        return blocks.isEmpty() ? "0" : getLastBlock().getBlockHash();
    }

    public void printUpdates() {
        System.out.println(getLastBlock());
        final var zeroesChange = getNextZeroes() - zeroesList.get(zeroesList.size() - 2);
        if (zeroesChange > 0) {
            System.out.println("N was increased by 1");
        } else if (zeroesChange < 0) {
            System.out.println("N was decreased by 1");
        } else {
            System.out.println("N stays the same");
        }
    }

    public synchronized boolean addBlock(Block block) {
        if (isValid(block)) {
            blocks.add(block);
            updateRules();
            printUpdates();
            System.out.println();
//            try {
//                serialize(this, FILE_PATH);
//            } catch (IOException e) {
//                e.printStackTrace();
            return true;
        }
        return false;
    }

    private void updateRules() {
        final var time = getLastBlock().getTimeToGenerate();
        if (time > 15) {
            zeroesList.add(getNextZeroes() - 1);
        } else if (time < 3) {
            zeroesList.add(getNextZeroes() + 1);
        } else {
            zeroesList.add(getNextZeroes());
        }
    }

//    private boolean isValid() {
//        return blocks.stream().allMatch(this::isValid);
//    }

    private boolean isValid(Block block) {
        return getNextBlockId() == block.getId() &&
                getLastBlockHash().equals(block.getPrevBlockHash()) &&
                block.isProved(getNextZeroes());
    }
}

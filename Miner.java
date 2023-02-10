package Blockchain;

public class Miner implements Runnable {
    private final Blockchain blockchain;

    public Miner(Blockchain blockchain, String threadName) {
        this.blockchain = blockchain;
        Thread.currentThread().setName(threadName);
    }

    @Override
    public void run() {
        try {
            while (blockchain.getNextBlockId() <= 15) {
                final var block = Block.getUnproved(
                        Thread.currentThread().getName(),
                        blockchain.getNextBlockId(),
                        blockchain.getLastBlockHash());
                block.prove(blockchain.getNextZeroes());
                blockchain.addBlock(block);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package cachesimulation;

import java.util.*;

// used by the Memory class
// this is what's inside the memory
class MemoryBlock{
    int[] data;

    MemoryBlock(int blockSize){
        this.data = new int[blockSize];
    }
}

class Memory{
    int blockSize;
    int numMemoryBlocks;
    int memorySize;
    int numCacheBlocks;
    LinkedList<MemoryBlock> memory;

    Memory(int numMemoryBlocks, int blockSize, int numCacheBlocks){
        this.blockSize = blockSize;
        this.numMemoryBlocks = numMemoryBlocks;
        this.memorySize = numMemoryBlocks * blockSize;
        this.numCacheBlocks = numCacheBlocks;
        this.memory = new LinkedList<>();

        // initializes each MemoryBlock
        for (int i = 0; i < numMemoryBlocks; i++) {
            memory.add(new MemoryBlock(blockSize));
        }
    }

    void printBlocks(){
        int ctr = 0;
        for(MemoryBlock block: memory){
            System.out.println("Memory Block: " + ctr);
            for (int i = 0; i < block.data.length; i++){
                System.out.printf("Word %d: %d\n", i, block.data[i]);
            }
            ctr++;
            System.out.println();
        }
    }
}

// used by the Cache class
// this is what's inside the cache
class CacheBlock {
    int tag;
    boolean valid;
    int[] data;

    CacheBlock(int tag, int blockSize) {
        this.tag = tag;
        this.valid = false;
        this.data = new int[blockSize];
    }
}

class Cache {
    int blockSize;
    int numMemoryBlocks;
    int cacheSize;
    int numCacheBlocks;
    LinkedList<CacheBlock> cache;
    Queue<Integer> fifoQueue;

    Cache(int numMemoryBlocks, int blockSize, int numCacheBlocks) {
        this.blockSize = blockSize;
        this.numMemoryBlocks = numMemoryBlocks;
        this.cacheSize = numCacheBlocks * blockSize;
        this.numCacheBlocks = numCacheBlocks;
        this.cache = new LinkedList<>();
        this.fifoQueue = new LinkedList<>();

        // initializes each CacheBlock with default values
        for (int i = 0; i < numCacheBlocks; i++) {
            cache.add(new CacheBlock(-1, blockSize));
            fifoQueue.add(i);
        }
    }

    boolean read(int address) {
        int blockNumber = address / blockSize;
        int tag = blockNumber;

        for (CacheBlock block : cache) {
            if (block.valid && block.tag == tag) {
                System.out.println("Cache Hit!");
                return true;
            }
        }

        System.out.println("Cache Miss!");
        replaceBlock(tag);
        return false;
    }

    void replaceBlock(int tag) {
        int replacedBlockIndex = fifoQueue.poll();
        CacheBlock replacedBlock = cache.get(replacedBlockIndex);

        replacedBlock.tag = tag;
        replacedBlock.valid = true;

        fifoQueue.add(replacedBlockIndex);
    }

    void printBlocks(){
        int ctr = 0;
        for(CacheBlock block: cache){
            System.out.println("Cache Block: " + ctr);
            for (int i = 0; i < block.data.length; i++){
                System.out.printf("Tag: %d Word %d: %d\n", block.tag, i, block.data[i]);
            }
            ctr++;
            System.out.println();
        }
    }
}

public class CacheSimulation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of memory blocks: ");
        int numMemoryBlocks = scanner.nextInt();

        int blockSize = 16;
        int numCacheBlocks = 4; // should be 32. temporarily decreased for testing purposes.


        Cache cache = new Cache(numMemoryBlocks, blockSize, numCacheBlocks);

        Memory memory = new Memory(numMemoryBlocks, blockSize, numCacheBlocks);

        // Simulation of test set scenarios
        for (int i = 0; i < numMemoryBlocks; i++) {
            //cache.read(i);

        }
        memory.printBlocks();
        System.out.println("--------------------------------------------------------------------------------------------");
        cache.printBlocks();

        scanner.close();
    }
}
package cachesimulation;

import java.util.*;

// used by the Memory class
// this is what's inside the memory
class MemoryBlock {
    int[] data;

    MemoryBlock(int blockSize) {
        this.data = new int[blockSize];
    }
}

class Memory {
    int blockSize;
    int numMemoryBlocks;
    int memorySize;
    int numCacheBlocks;
    MemoryBlock[] memory;

    Memory(int numMemoryBlocks, int blockSize, int numCacheBlocks) {
        this.blockSize = blockSize;
        this.numMemoryBlocks = numMemoryBlocks;
        this.memorySize = numMemoryBlocks * blockSize;
        this.numCacheBlocks = numCacheBlocks;
        this.memory = new MemoryBlock[numMemoryBlocks];

        // initializes each MemoryBlock
        for (int i = 0; i < numMemoryBlocks; i++) {
            memory[i] = new MemoryBlock(blockSize);
        }
    }

    void addRandomInputs() {
        Random random = new Random();

        int[] memoryInputs = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

        for (int i = 0; i < memory.length; i++) {
            for (int j = 0; j < memory[0].data.length; j++) {
                int randomIndex = random.nextInt(memoryInputs.length);
                memory[i].data[j] = memoryInputs[randomIndex];
            }
        }
    }

    void printBlocks() {
        int ctr = 0;
        for (int i = 0; i < memory.length; i++) {
            System.out.println("Memory Block: " + ctr);
            for (int j = 0; j < memory[0].data.length; j++) {
                System.out.printf("Word %d: %d\n", j, memory[i].data[j]);
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
    CacheBlock[] cache;
    Queue<Integer> fifoQueue;

    Cache(int numMemoryBlocks, int blockSize, int numCacheBlocks) {
        this.blockSize = blockSize;
        this.numMemoryBlocks = numMemoryBlocks;
        this.cacheSize = numCacheBlocks * blockSize;
        this.numCacheBlocks = numCacheBlocks;
        this.cache = new CacheBlock[numCacheBlocks];
        this.fifoQueue = new LinkedList<>();

        // initializes each CacheBlock with default values
        for (int i = 0; i < numCacheBlocks; i++) {
            cache[i] = new CacheBlock(-1, blockSize);

            // initializes each data with -1 values
            for (int j = 0; j < cache[0].data.length; j++) {
                cache[i].data[j] = -1;
            }

            fifoQueue.add(i);
        }
    }

    /*
     * boolean read(int address) {
     * int blockNumber = address / blockSize;
     * int tag = blockNumber;
     * 
     * for (CacheBlock block : cache) {
     * if (block.valid && block.tag == tag) {
     * System.out.println("Cache Hit!");
     * return true;
     * }
     * }
     * 
     * System.out.println("Cache Miss!");
     * //replaceBlock(tag);
     * return false;
     * }
     */
    void replaceBlock(int tag, MemoryBlock memory) {

        boolean isHit = false;

        // checks if the tag is already in the cache
        // if it is, then cache hit
        for (int i = 0; i < cache.length; i++) {
            if (cache[i].tag == tag) {
                isHit = true;
            }
        }

        // cache miss
        if (isHit == false) {
            int replacedBlockIndex = fifoQueue.poll(); // removes element from the queue

            // CacheBlock replacedBlock = cache[replacedBlockIndex];

            for (int i = 0; i < memory.data.length; i++) {
                cache[replacedBlockIndex].data[i] = memory.data[i];
            }

            cache[replacedBlockIndex].tag = tag;
            cache[replacedBlockIndex].valid = true; // valid means replaced

            fifoQueue.add(replacedBlockIndex);
        }

    }

    void printBlocks() {
        int ctr = 0;
        for (int i = 0; i < cache.length; i++) {
            System.out.println("Cache Block: " + ctr);

            for (int j = 0; j < cache[0].data.length; j++) {
                System.out.printf("Tag: %d Word %d: %d \n", cache[i].tag, j, cache[i].data[j]);
            }
            ctr++;
            System.out.println();
        }
    }
}

public class CacheSimulation {

    static void testCase1(Cache cache, Memory memory, int blockSize, int numCacheBlocks, int numMemoryBlocks, GUI gui) {

        System.out.println("--START OF TEST CASE 1--");
        int numRepeat = 4;
        memory.addRandomInputs();

        System.out.println("-----MEMORY-----");
        memory.printBlocks();
        System.out.println();
        System.out.println();

        // prints empty cache
        System.out.println("-----EMPTY CACHE-----");
        cache.printBlocks();
        System.out.println();
        System.out.println();

        gui.getSnapScreen().setSnapMenu(cache, memory);

        // loop for # of sequence
        for (int i = 0; i < numRepeat; i++) {
            int rep = i + 1;
            System.out.println("----------REPEAT: " + rep + "----------");
            System.out.println();

            // loop for replacing each cache block
            for (int j = 0; j < numMemoryBlocks; j++) {

                cache.replaceBlock(j, memory.memory[j]);

                // traces replacement of each block in cache
                System.out.println("-----INSERTING BLOCK " + j + " FROM MEMORY-----");
                cache.printBlocks();
                System.out.println();
                System.out.println();
            }
        }
        System.out.println("-----Final Snapshot-----");
        cache.printBlocks();
        System.out.println();
        System.out.println();

        System.out.println("--END OF TEST CASE 1--");
        System.out.println();
        System.out.println();
    }

    static void testCase2(Cache cache, Memory memory, int blockSize, int numCacheBlocks, int numMemoryBlocks, GUI gui) {

        System.out.println("--START OF TEST CASE 2--");
        int numRepeat = 1;
        memory.addRandomInputs();

        System.out.println("-----MEMORY-----");
        memory.printBlocks();
        System.out.println();
        System.out.println();

        // prints empty cache
        System.out.println("-----EMPTY CACHE-----");
        cache.printBlocks();
        System.out.println();
        System.out.println();

        Random random = new Random();

        gui.getSnapScreen().setSnapMenu(cache, memory);

        // loop for # of sequence
        for (int i = 0; i < numRepeat; i++) {
            int rep = i + 1;
            System.out.println("----------REPEAT: " + rep + "----------");
            System.out.println();

            // loop for replacing each cache block
            for (int j = 0; j < numMemoryBlocks; j++) {

                int randomIndex = random.nextInt(numMemoryBlocks);
                cache.replaceBlock(randomIndex, memory.memory[randomIndex]);

                // traces replacement of each block in cache
                System.out.println("-----INSERTING BLOCK " + randomIndex + " FROM MEMORY-----");
                cache.printBlocks();
                System.out.println();
                System.out.println();
            }
        }

        System.out.println("--END OF TEST CASE 2--");
        System.out.println();
        System.out.println();
    }

    static void testCase3(Cache cache, Memory memory, int blockSize, int numCacheBlocks, int numMemoryBlocks, GUI gui) {

        System.out.println("--START OF TEST CASE 3--");
        int numRepeats = 4;
        memory.addRandomInputs();

        System.out.println("-----MEMORY-----");
        memory.printBlocks();
        System.out.println();
        System.out.println();

        // prints empty cache
        System.out.println("-----EMPTY CACHE-----");
        cache.printBlocks();
        System.out.println();
        System.out.println();

        gui.getSnapScreen().setSnapMenu(cache, memory);

        // loop for # of repeats
        for (int i = 0; i < numRepeats; i++) {
            int seq = i + 1;
            System.out.println("----------REPEAT: " + (seq) + "----------");
            System.out.println();
            boolean repeated = false;
            // loop for replacing each cache block in the sequence
            for (int j = 0; j < numMemoryBlocks; j++) {

                cache.replaceBlock(j, memory.memory[j]);

                // traces replacement of each block in cache
                System.out.println("-----INSERTING BLOCK " + j + " FROM MEMORY-----");
                cache.printBlocks();
                System.out.println();
                System.out.println();

                if (j >= numCacheBlocks - 2 && repeated == false) {
                    j = 0;
                    repeated = true;
                }
            }
        }

        System.out.println("--END OF TEST CASE 3--");
        System.out.println();
        System.out.println();
    }

    static void sstestCase1(int blockSize, int numCacheBlocks, int numMemoryBlocks, GUI gui) {
        System.out.println("--START OF TEST CASE 1--");

        int numRepeat = 4;
        numMemoryBlocks = 2 * numMemoryBlocks; // 2n

        Cache cache = new Cache(numMemoryBlocks, blockSize, numCacheBlocks);
        Memory memory = new Memory(numMemoryBlocks, blockSize, numCacheBlocks);

        memory.addRandomInputs();

        gui.getStepScreen().setStepScreen(cache, memory);

        System.out.println("-----MEMORY-----");
        memory.printBlocks();
        System.out.println();
        System.out.println();

        // prints empty cache
        System.out.println("-----EMPTY CACHE-----");
        cache.printBlocks();
        System.out.println();
        System.out.println();

        // // loop for # of sequence
        for (int i = 0; i < numRepeat; i++) {
            int rep = i + 1;
            System.out.println("----------REPEAT: " + rep + "----------");
            System.out.println();

            // loop for replacing each cache block
            for (int j = 0; j < numMemoryBlocks; j++) {

                // highlight memblocks
                gui.getStepScreen().setCache(cache);
                gui.getStepScreen().setMainMemory(memory);
                // System.out.println("lol");
                // Thread.sleep(1000);

                cache.replaceBlock(j, memory.memory[j]);

                // traces replacement of each block in cache
                System.out.println("-----INSERTING BLOCK " + j + " FROM MEMORY-----");
                cache.printBlocks();
                System.out.println();
                System.out.println();
            }
        }

        System.out.println("-----Final Snapshot-----");
        cache.printBlocks();
        System.out.println();
        System.out.println();

        System.out.println("--END OF TEST CASE 1--");
        System.out.println();
        System.out.println();
    }
}
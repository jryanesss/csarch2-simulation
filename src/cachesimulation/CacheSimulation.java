package cachesimulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.SwingUtilities;

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

    Memory copyMemory() {
        Memory copiedMemory = new Memory(this.numMemoryBlocks, this.blockSize, this.numCacheBlocks);

        for (int i = 0; i < this.memory.length; i++) {
            MemoryBlock originalBlock = this.memory[i];
            MemoryBlock copiedBlock = new MemoryBlock(this.blockSize);
            System.arraycopy(originalBlock.data, 0, copiedBlock.data, 0, this.blockSize);
            copiedMemory.memory[i] = copiedBlock;
        }

        return copiedMemory;
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

    void printBlocks(BufferedWriter cacheTraceWriter) {
        int ctr = 0;
        try {
            for (int i = 0; i < memory.length; i++) {
                cacheTraceWriter.write("Memory Block: " + ctr + "\n");
                System.out.println("Memory Block: " + ctr);
                for (int j = 0; j < memory[0].data.length; j++) {
                    cacheTraceWriter.write(String.format("Word %d: %d\n", j, memory[i].data[j]));
                    System.out.printf("Word %d: %d\n", j, memory[i].data[j]);
                }
                ctr++;
                cacheTraceWriter.write("\n");
                System.out.println();
            }
            cacheTraceWriter.flush(); // Ensure that the content is written to the file immediately
        } catch (IOException e) {
            e.printStackTrace();
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

    int memoryAccessCount;
    int cacheHitCount;
    int cacheMissCount;
    float cacheHitRate;
    float cacheMissRate;
    float avgMemoryAccessTime;
    float totalMemoryAccessTime;

    float missPenalty;

    Cache(int numMemoryBlocks, int blockSize, int numCacheBlocks) {
        this.blockSize = blockSize;
        this.numMemoryBlocks = numMemoryBlocks;
        this.cacheSize = numCacheBlocks * blockSize;
        this.numCacheBlocks = numCacheBlocks;
        this.cache = new CacheBlock[numCacheBlocks];
        this.fifoQueue = new LinkedList<>();

        this.memoryAccessCount = 0;
        this.cacheHitCount = 0;
        this.cacheMissCount = 0;
        this.cacheHitRate = 0.0f;
        this.cacheMissRate = 0.0f;
        this.avgMemoryAccessTime = 0.0f;
        this.totalMemoryAccessTime = 0.0f;

        this.missPenalty = 1 + (10 * numCacheBlocks) + 1;

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

    Cache copyCache() {
        Cache copiedCache = new Cache(this.numMemoryBlocks, this.blockSize, this.numCacheBlocks);

        for (int i = 0; i < this.cache.length; i++) {
            CacheBlock originalBlock = this.cache[i];
            CacheBlock copiedBlock = new CacheBlock(originalBlock.tag, this.blockSize);
            System.arraycopy(originalBlock.data, 0, copiedBlock.data, 0, this.blockSize);
            copiedBlock.valid = originalBlock.valid;
            copiedCache.cache[i] = copiedBlock;
        }
        return copiedCache;
    }

    void computeSimulationMetrics() {
        this.memoryAccessCount = this.cacheHitCount + this.cacheMissCount;
        this.cacheHitRate = (float) this.cacheHitCount / this.memoryAccessCount;
        this.cacheMissRate = (float) this.cacheMissCount / this.memoryAccessCount;
        this.avgMemoryAccessTime = (this.cacheHitRate * 1) + (this.cacheMissRate * this.missPenalty);
        this.totalMemoryAccessTime = this.memoryAccessCount * this.avgMemoryAccessTime;
        printSimulationMetrics(this.cacheHitRate * 100, this.cacheMissRate * 100);
    }

    void printSimulationMetrics(float cacheHitRate, float cacheMissRate) {
        System.out.println("Memory Access Count: " + this.memoryAccessCount);
        System.out.println("Cache Hit Count: " + this.cacheHitCount);
        System.out.println("Cache Miss Count: " + this.cacheMissCount);
        System.out.println("Cache Hit Rate: " + cacheHitRate + "%");
        System.out.println("Cache Miss Rate: " + cacheMissRate + "%");
        System.out.println("Average Memory Access Time: " + this.avgMemoryAccessTime + " ns");
        System.out.println("Total Memory Access Time: " + this.totalMemoryAccessTime + " ns");
        System.out.println();
    }

    void replaceBlock(int tag, MemoryBlock memory) {

        boolean isHit = false;

        for (int i = 0; i < cache.length; i++) {
            if (cache[i].tag == tag) {
                isHit = true;
                this.cacheHitCount++;
            }
        }

        // cache miss
        if (isHit == false) {
            int replacedBlockIndex = fifoQueue.poll(); // removes element from the queue

            for (int i = 0; i < memory.data.length; i++) {
                cache[replacedBlockIndex].data[i] = memory.data[i];
            }

            this.cacheMissCount++;
            cache[replacedBlockIndex].tag = tag;
            cache[replacedBlockIndex].valid = true; // valid means replaced

            fifoQueue.add(replacedBlockIndex);
        }
    }

    int stepReplaceBlock(int tag, MemoryBlock memory) {

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

            return replacedBlockIndex;
        } else {
            return -2;
        }

    }

    void printBlocks(BufferedWriter cacheTraceWriter) {
        int ctr = 0;

        try {
            for (int i = 0; i < cache.length; i++) {
                cacheTraceWriter.write("Cache Block: " + ctr + "\n");
                System.out.println("Cache Block: " + ctr);
                for (int j = 0; j < cache[0].data.length; j++) {
                    cacheTraceWriter.write("Tag: " + cache[i].tag + " Word " + j + ": " + cache[i].data[j] + " \n");
                    System.out.printf("Tag: %d Word %d: %d \n", cache[i].tag, j, cache[i].data[j]);
                }
                ctr++;
                cacheTraceWriter.write("\n");
                System.out.println();
            }
            cacheTraceWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class CacheSimulation {

    static void testCase1(Cache cache, Memory memory, int blockSize, int numCacheBlocks, int numMemoryBlocks, GUI gui) {
        try (BufferedWriter cacheTraceWriter = new BufferedWriter(new FileWriter("cacheMemoryTrace.txt"))) {
            cacheTraceWriter.write("--START OF TEST CASE 1--\n\n");
            int numRepeat = 4;
            // memory.addRandomInputs();

            cacheTraceWriter.write("-----MEMORY-----\n");
            memory.printBlocks(cacheTraceWriter);
            cacheTraceWriter.write("\n\n");

            // prints empty cache
            cacheTraceWriter.write("-----EMPTY CACHE-----\n");
            cache.printBlocks(cacheTraceWriter);
            cacheTraceWriter.write("\n\n");

            gui.getSnapScreen().setSnapMenu(cache, memory);

            // loop for # of sequence
            for (int i = 0; i < numRepeat; i++) {
                int rep = i + 1;
                cacheTraceWriter.write("----------REPEAT: " + rep + "----------\n");

                // loop for replacing each cache block
                for (int j = 0; j < numMemoryBlocks; j++) {

                    cache.replaceBlock(j, memory.memory[j]);

                    // traces replacement of each block in cache
                    cacheTraceWriter.write("-----INSERTING BLOCK " + j + " FROM MEMORY-----\n");
                    cache.printBlocks(cacheTraceWriter);
                    cacheTraceWriter.write("\n\n");
                }
            }

            cacheTraceWriter.flush();

            cacheTraceWriter.write("-----Final Snapshot-----\n");
            cache.printBlocks(cacheTraceWriter);
            cacheTraceWriter.write("\n\n");

            cacheTraceWriter.write("--END OF TEST CASE 1--\n\n");

            cache.computeSimulationMetrics();

            cacheTraceWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void testCase2(Cache cache, Memory memory, int blockSize, int numCacheBlocks, int numMemoryBlocks, GUI gui) {
        try (BufferedWriter cacheTraceWriter = new BufferedWriter(new FileWriter("cacheMemoryTrace.txt"))) {
            cacheTraceWriter.write("--START OF TEST CASE 2--\n\n");
            int numRepeat = 1;
            // memory.addRandomInputs();

            cacheTraceWriter.write("-----MEMORY-----\n");
            memory.printBlocks(cacheTraceWriter);
            cacheTraceWriter.write("\n\n");

            // prints empty cache
            cacheTraceWriter.write("-----EMPTY CACHE-----\n");
            cache.printBlocks(cacheTraceWriter);
            cacheTraceWriter.write("\n\n");

            Random random = new Random();

            gui.getSnapScreen().setSnapMenu(cache, memory);

            // loop for # of sequence
            for (int i = 0; i < numRepeat; i++) {
                int rep = i + 1;
                cacheTraceWriter.write("----------REPEAT: " + rep + "----------\n");

                // loop for replacing each cache block
                for (int j = 0; j < numMemoryBlocks; j++) {

                    int randomIndex = random.nextInt(numMemoryBlocks);
                    cache.replaceBlock(randomIndex, memory.memory[randomIndex]);

                    // traces replacement of each block in cache
                    cacheTraceWriter.write("-----INSERTING BLOCK " + randomIndex + " FROM MEMORY-----\n");
                    cache.printBlocks(cacheTraceWriter);
                    cacheTraceWriter.write("\n\n");
                }
                cacheTraceWriter.flush();
            }

            cacheTraceWriter.write("--END OF TEST CASE 2--\n\n");

            cache.computeSimulationMetrics();

            cacheTraceWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void testCase3(Cache cache, Memory memory, int blockSize, int numCacheBlocks, int numMemoryBlocks, GUI gui) {
        try (BufferedWriter cacheTraceWriter = new BufferedWriter(new FileWriter("cacheMemoryTrace.txt"))) {
            cacheTraceWriter.write("--START OF TEST CASE 3--\n\n");
            int numRepeats = 4;
            // memory.addRandomInputs();

            cacheTraceWriter.write("-----MEMORY-----\n");
            memory.printBlocks(cacheTraceWriter);
            cacheTraceWriter.write("\n\n");

            // prints empty cache
            cacheTraceWriter.write("-----EMPTY CACHE-----\n");
            cache.printBlocks(cacheTraceWriter);
            cacheTraceWriter.write("\n\n");

            gui.getSnapScreen().setSnapMenu(cache, memory);

            // loop for # of repeats
            for (int i = 0; i < numRepeats; i++) {
                int seq = i + 1;
                cacheTraceWriter.write("----------REPEAT: " + (seq) + "----------\n");
                boolean repeated = false;
                // loop for replacing each cache block in the sequence
                for (int j = 0; j < numMemoryBlocks; j++) {

                    cache.replaceBlock(j, memory.memory[j]);

                    // traces replacement of each block in cache
                    cacheTraceWriter.write("-----INSERTING BLOCK " + j + " FROM MEMORY-----\n");
                    cache.printBlocks(cacheTraceWriter);
                    cacheTraceWriter.write("\n\n");

                    if (j >= numCacheBlocks - 2 && repeated == false) {
                        j = 0;
                        repeated = true;
                    }
                }
            }

            cacheTraceWriter.flush();

            cacheTraceWriter.write("--END OF TEST CASE 3--\n\n");

            cache.computeSimulationMetrics();

            cacheTraceWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void stepTestCase1(Cache cache, Memory memory, int blockSize, int numCacheBlocks, int numMemoryBlocks,
            GUI gui) {

        int numRepeat = 4;
        
        Thread computationThread = new Thread(() -> {
            for (int i = 0; i < numRepeat; i++) {
                for (int j = 0; j < numMemoryBlocks; j++) {
                    int index = cache.stepReplaceBlock(j, memory.memory[j]);

                    // // Highlights replaced cache block and current memory block
                    gui.getStepScreen().highlightCacheBlock(cache, index);
                    gui.getStepScreen().highlightMemBlock(memory, j);

                    // Reflects GUI Changes
                    SwingUtilities.invokeLater(() -> {
                        gui.getStepScreen().repaint();
                        gui.getStepScreen().revalidate();
                    });

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        computationThread.start();

        System.out.println("--END OF TEST CASE 1--");
        System.out.println();
        System.out.println();
    }

    static void stepTestCase2(Cache cache, Memory memory, int blockSize, int numCacheBlocks, int numMemoryBlocks,
            GUI gui) {

        Cache cacheCopy = cache.copyCache();
        Memory memoryCopy = memory.copyMemory();
        CacheSimulation.testCase2(cacheCopy, memoryCopy, blockSize, numCacheBlocks, numMemoryBlocks, gui);

        Random random = new Random();
        int numRepeat = 1;

        Thread computationThread = new Thread(() -> {
            // loop for # of sequence
            for (int i = 0; i < numRepeat; i++) {
                int rep = i + 1;
                System.out.println("----------REPEAT: " + rep + "----------");
                System.out.println();

                // loop for replacing each cache block
                for (int j = 0; j < numMemoryBlocks; j++) {
                    int randomIndex = random.nextInt(numMemoryBlocks);
                    int index = cache.stepReplaceBlock(j, memory.memory[randomIndex]);

                    // Highlights replaced cache block and current memory block
                    gui.getStepScreen().highlightCacheBlock(cache, index);
                    gui.getStepScreen().highlightMemBlock(memory, j);

                    // Reflects GUI Changes
                    SwingUtilities.invokeLater(() -> {
                        gui.getStepScreen().repaint();
                        gui.getStepScreen().revalidate();
                    });

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        computationThread.start();

        System.out.println("--END OF TEST CASE 2-");
        System.out.println();
        System.out.println();
    }

    static void stepTestCase3(Cache cache, Memory memory, int blockSize, int numCacheBlocks, int numMemoryBlocks,
            GUI gui) {

        Cache cacheCopy = cache.copyCache();
        Memory memoryCopy = memory.copyMemory();
        CacheSimulation.testCase3(cacheCopy, memoryCopy, blockSize, numCacheBlocks, numMemoryBlocks, gui);
        System.out.println("--START OF TEST CASE 3--");
        int numRepeats = 4;

        Thread computationThread = new Thread(() -> {
            // loop for # of repeats
            for (int i = 0; i < numRepeats; i++) {
                int seq = i + 1;
                System.out.println("----------REPEAT: " + (seq) + "----------");
                System.out.println();
                boolean repeated = false;
                // loop for replacing each cache block in the sequence
                for (int j = 0; j < numMemoryBlocks; j++) {

                    int index = cache.stepReplaceBlock(j, memory.memory[j]);

                    // Highlights replaced cache block and current memory block
                    gui.getStepScreen().highlightCacheBlock(cache, index);
                    gui.getStepScreen().highlightMemBlock(memory, j);

                    // Reflects GUI Changes
                    SwingUtilities.invokeLater(() -> {
                        gui.getStepScreen().repaint();
                        gui.getStepScreen().revalidate();
                    });

                    if (j >= numCacheBlocks - 2 && repeated == false) {
                        j = 0;
                        repeated = true;
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        computationThread.start();

        System.out.println("--END OF TEST CASE 3");
        System.out.println();
        System.out.println();
    }
}
package cachesimulation;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

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
    MemoryBlock[] memory;

    Memory(int numMemoryBlocks, int blockSize, int numCacheBlocks){
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

    void addRandomInputs(){
        Random random = new Random();

        int[] memoryInputs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};


        for(int i = 0; i < memory.length; i++){
            for (int j = 0; j < memory[0].data.length; j++){
                int randomIndex = random.nextInt(memoryInputs.length);
                memory[i].data[j] = memoryInputs[randomIndex];
            }
        }
    }

    void printBlocks(){
        int ctr = 0;
        for(int i = 0; i < memory.length; i++){
            System.out.println("Memory Block: " + ctr);
            for (int j = 0; j < memory[0].data.length; j++){
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

    void computeSimulationMetrics() {
        this.memoryAccessCount = this.cacheHitCount + this.cacheMissCount;
        this.cacheHitRate = (float) this.cacheHitCount / this.memoryAccessCount;
        this.cacheMissRate = (float) this.cacheMissCount / this.memoryAccessCount;
        this.avgMemoryAccessTime = (this.cacheHitRate * 1) + (this.cacheMissRate * this.missPenalty);
        // this.totalMemoryAccessTime = (this.cacheHitCount * blockSize * 1) + (this.cacheMissCount * (1 + (blockSize * (10 + 1))));
        this.totalMemoryAccessTime = this.memoryAccessCount * this.avgMemoryAccessTime;
        // Assuming memory access time is 10ns and cache acess time is 1ns -- NON L/T
        printSimulationMetrics(this.cacheHitRate * 100, this.cacheMissRate * 100);
    }

    void replaceBlock(int tag, MemoryBlock memory) {

        // WRONG -- FA uses TAG as the UID
        // int replacedBlockIndex = fifoQueue.poll(); // removes element from the queue

        // if(!cache[replacedBlockIndex].valid) {
        //     this.cacheMissCount++;
        // } else {
        //     this.cacheHitCount++;
        // }

        // for (int i = 0; i < memory.data.length; i++){
        //     cache[replacedBlockIndex].data[i] = memory.data[i];
        // }

        // cache[replacedBlockIndex].tag = tag;
        // cache[replacedBlockIndex].valid = true; // valid means replaced

        // fifoQueue.add(replacedBlockIndex); 

        boolean isHit = false;

        for (int i = 0; i < cache.length; i++){
            if (cache[i].tag == tag){
                isHit = true;
                this.cacheHitCount++;
            }
        }

        // cache miss
        if (isHit == false){
            int replacedBlockIndex = fifoQueue.poll(); // removes element from the queue

            for (int i = 0; i < memory.data.length; i++){
                cache[replacedBlockIndex].data[i] = memory.data[i];
            }

            this.cacheMissCount++;
            cache[replacedBlockIndex].tag = tag;
            cache[replacedBlockIndex].valid = true; // valid means replaced

            fifoQueue.add(replacedBlockIndex);
        }
    }

    void printBlocks(){
        int ctr = 0;
        for(int i = 0; i < cache.length; i++){
            System.out.println("Cache Block: " + ctr);

            for (int j = 0; j < cache[0].data.length; j++){
                System.out.printf("Tag: %d Word %d: %d \n", cache[i].tag, j, cache[i].data[j]);
            }
            ctr++;
            System.out.println();
        }
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
}

public class CacheSimulation {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int blockSize = 16;     // should be 16. temporarily decreased for testing purposes.
        int numCacheBlocks = 32; // should be 32. temporarily decreased for testing purposes.

        System.out.print("Enter the number of memory blocks: ");
        int numMemoryBlocks = scanner.nextInt();

        //----------------------------------------------TEST CASES------------------------------------------------------//
        //testCase1(blockSize, numCacheBlocks, numMemoryBlocks);
        //testCase2(blockSize, numCacheBlocks, numMemoryBlocks);
        testCase3(blockSize, numCacheBlocks, numMemoryBlocks);

        //----------------------------------------------TEST CASES------------------------------------------------------//

        // Cache cache = new Cache(numMemoryBlocks, blockSize, numCacheBlocks);
        // Memory memory = new Memory(numMemoryBlocks, blockSize, numCacheBlocks);

        // //places random data inside the memory
        // memory.addRandomInputs();

        // System.out.println("---------------------------------------MEMORY-----------------------------------------------");
        // memory.printBlocks();
        // System.out.println("--------------------------------------------------------------------------------------------");
        // System.out.println();

        // // prints empty cache
        // System.out.println("------------------------------------EMPTY CACHE---------------------------------------------");
        // cache.printBlocks();
        // System.out.println("--------------------------------------------------------------------------------------------");
        // System.out.println();

        // // replaces each word per block
        // // loop iterates per block, not per word
        //     for (int i = 0; i < numMemoryBlocks; i++) {
        //         // cache.accessMemory(i);
        //         cache.replaceBlock(i, memory.memory[i]);
        //     }

        // // prints final version of the cache after replacement from the memory
        // System.out.println("------------------------------------REPLACED CACHE------------------------------------------");
        // cache.printBlocks();
        // System.out.println("--------------------------------------------------------------------------------------------");
        // System.out.println();
       
        // scanner.close();

        // cache.computeSimulationMetrics();
        // cache.printSimulatrionMetrics();
    }

    static void testCase1(int blockSize, int numCacheBlocks, int numMemoryBlocks){

        System.out.println("--START OF TEST CASE 1--");

        int numRepeat = 4;
        numMemoryBlocks = 2 * numMemoryBlocks; // 2n

        Cache cache = new Cache(numMemoryBlocks, blockSize, numCacheBlocks);
        Memory memory = new Memory(numMemoryBlocks, blockSize, numCacheBlocks);

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

        // loop for # of sequence
        for (int i = 0; i < numRepeat; i++){
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

        cache.computeSimulationMetrics();
    }

    static void testCase2(int blockSize, int numCacheBlocks, int numMemoryBlocks){

        System.out.println("--START OF TEST CASE 2--");

        int numRepeat = 1;
        //int numMemoryBlocks = 4 * numCacheBlocks;
        
        Cache cache = new Cache(numMemoryBlocks, blockSize, numCacheBlocks);
        Memory memory = new Memory(numMemoryBlocks, blockSize, numCacheBlocks);

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

        // loop for # of sequence
        for (int i = 0; i < numRepeat; i++){
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

        cache.computeSimulationMetrics();
    }
    
    static void testCase3(int blockSize, int numCacheBlocks, int numMemoryBlocks){

        System.out.println("--START OF TEST CASE 3--");
        
        numMemoryBlocks = 2 * numMemoryBlocks;
        int numRepeats = 4;

        Cache cache = new Cache(numMemoryBlocks, blockSize, numCacheBlocks);
        Memory memory = new Memory(numMemoryBlocks, blockSize, numCacheBlocks);

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

        // loop for # of repeats
        for (int i = 0; i < numRepeats; i++) {
        	int seq = i+1;
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
                
                if (j >= numCacheBlocks - 2 && repeated == false){
                	j = 0;
                	repeated = true;
                }
            }
        }

        System.out.println("--END OF TEST CASE 3--");
        System.out.println();
        System.out.println();

        cache.computeSimulationMetrics();
    }

}

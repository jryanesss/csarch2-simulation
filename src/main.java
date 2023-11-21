import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class main {

    static String[][] enqueue(String[][] cacheBlocks, int i, int j, String value){

        cacheBlocks[i][j] = value;

        return cacheBlocks;
    }

    static String[][] dequeue(String[][] cacheBlocks, int i, int j){

        cacheBlocks[i][j] = null;

        return cacheBlocks;
    }

    static void printCache(String[][] cacheBlocks){

        for (int i = 0; i < cacheBlocks.length; i++){
            for (int j = 0; j < cacheBlocks[0].length; j++){
                System.out.printf("cacheBlock[%d] cacheLine[%d]: %s\n", i, j, cacheBlocks[i][j]);
            }
            System.out.println();
        }

    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Please enter memory block size: ");
        int memBlockSize = scanner.nextInt();

        int cacheBlockSize = 32;
        int cacheLineSize = 16;

        // each block has 16 words which is the cacheLineSize
        String[][] memBlocks = new String[memBlockSize][cacheLineSize];
        String[][] cacheBlocks = new String[cacheBlockSize][cacheLineSize];

        String[] memInputs = {"0x0000", "0x0001", "0x0010", "0x0011", "0x0100", "0x0101", "0x0110", "0x0111"};

        // initialize memory blocks with random values from memInputs
        for (int i = 0; i < memBlockSize; i++){
            for (int j = 0; j < cacheLineSize; j++){
                int randomIndex = random.nextInt(memInputs.length);
                memBlocks[i][j] = memInputs[randomIndex];
                System.out.printf("memBlock[%d] cacheLine[%d]: %s\n", i, j, memBlocks[i][j]);
            }
            System.out.println();
        }


        for (int i = 0; i < cacheBlocks.length; i++){
            for (int j = 0; j < cacheLineSize; j++){
                cacheBlocks = enqueue(cacheBlocks, i, j, "0x1111");
                cacheBlocks = dequeue(cacheBlocks, i, j);
            }

        }

        printCache(cacheBlocks);

    }
}

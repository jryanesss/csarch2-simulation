# csarch2-simulation
CSARCH2 Simulation Project <br>

MEMBERS <br>
<b>LADA, MARTIN JOSE MERCHAN <br>
MERTO, ALLEN NEO MASANGKAY <br>
MIRANDA, JAMAR LANCE FERNANDEZ <br>
SIMBAHON, JOOLZ RYANE CHAVEZ </b><br>
<br>

CACHE SPECIFICATIONS
- Mapping Function: <b>Full Associative (FA)</b>
- Replacement Algorithm: <b>First-In-First-Out (FIFO)</b>

TEST CASES ANALYSIS

<br>
<b> Test Case A: Sequential Sequence </b>



<br>
<b> Test Case B: Random Sequence </b>  <br>
<br>
  For this test case, the number of memory blocks is 4n where n is the number of memory blocks the user specified and the number of cache blocks is set at 32. <br>
  Unlike the first test case, the sequence has no order and memory blocks are inserted into the cache blocks at random. The same memory block can be inserted one after the other. <br>
  The sequence will go on 4n times. <br>
  <br>
  If n = 32, we will have a total of 128 memory blocks.
  <br>
  A random Memory Block will be inserted into Cache Block 0 and so on, this will happen 128 times.
   <br>
   There will be a total of 32 memory access counts, an average memory access time of 201.625 ns, and a total memory access time of 6425 ns. 
    <br>
    The hit rate and miss rate will vary due to the randomness of the sequence. 

<br>
<br>
<b> Test Case C: Mid-Repeat Sequence </b>  <be>
<br>
For this test case, the number of memory blocks is 2n where n is the number of memory blocks the user specified and the number of cache blocks is set at 32. <br>
  The first half of the memory blocks ( n - 1 ) will be inserted first, it will then be followed by memory block 1 until memory block 2n-1. This will be done 4 times.
   <br>
  <br>
If n = 32:
 * there will be a total of 376 memory access counts
 * 120 cache hits
 * 256 cache misses
 * an average memory access time of 219.55 ns
 * a total memory access time of 82552 ns

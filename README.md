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
<b> Test Case B: Random Sequence </b>
  For this test case, the number of memory blocks is 4n where n is the number of memory blocks the user specified and the number of cache blocks is set at 32. <br>
  Unlike the first test case, the sequence has no order and memory blocks are inserted into the cache blocks at random. The same memomry block can be inserted one after the other. <br>
  The sequence will go on 4n times. <br>
  <br>
  If n = 32, we will have a total of 128 memory blocks.
  <br>
  For the first iteration, a random Memory Block will be inserted into Cache Block 0, this will happen 128 times.
  
<br>
<b> Test Case C: Mid-Repeat Sequence </b>

package cachesimulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JTextArea;

public class Controller implements ActionListener {
    private GUI gui;
    private int blockSize = 16; // should be 16. temporarily decreased for testing purposes. CHANGE
                               // LATER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private int numCacheBlocks = 32; // should be 32. temporarily decreased for testing purposes. CHANGE
                                    // LATER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private Cache cache;
    private Memory memory;
    String selectedTestCase;
    String selectedSimOption;
    int numMemBlocks;

    public static void main(String[] args) throws Exception {
        new Controller(new GUI());
    }

    public Controller(GUI gui) {
        this.gui = gui;
        this.gui.setActionListenerToAll(this);
        updateView();
    }

    public void updateView() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("SIMULATE")) {

            JComboBox<String> testCaseComboBox = gui.getStartScreen().getTestCaseComboBox();
            JComboBox<String> simOptionComboBox = gui.getStartScreen().getSimOptionComboBox();
            JTextArea numMemBlocksTextArea = gui.getStartScreen().getNumMemBlocksTextArea();

            selectedTestCase = (String) testCaseComboBox.getSelectedItem();
            selectedSimOption = (String) simOptionComboBox.getSelectedItem();
            String textAreaStr = numMemBlocksTextArea.getText();

            try {
                numMemBlocks = Integer.parseInt(textAreaStr);

                System.out.println("Selected Test Case: " + selectedTestCase);
                System.out.println("Selected Simulation Option: " + selectedSimOption);
                System.out.println("Text from JTextArea: " + numMemBlocks);

                switch (selectedTestCase) {
                    case "Test Case 1":
                        if (selectedSimOption.equals("Snapshot")) {
                            numMemBlocks = 2 * numMemBlocks; // 2n
                            cache = new Cache(numMemBlocks, blockSize, numCacheBlocks);
                            memory = new Memory(numMemBlocks, blockSize, numCacheBlocks);
                            this.gui.changeScreen("SNAP");
                            CacheSimulation.testCase1(cache, memory, blockSize, numCacheBlocks, numMemBlocks, gui);
                        } else {
                            this.gui.changeScreen("STEP");
                        }
                        break;
                    case "Test Case 2":
                        if (selectedSimOption.equals("Snapshot")) {
                            numMemBlocks = 4 * numMemBlocks; // 4n
                            cache = new Cache(numMemBlocks, blockSize, numCacheBlocks);
                            memory = new Memory(numMemBlocks, blockSize, numCacheBlocks);
                            this.gui.changeScreen("SNAP");
                            CacheSimulation.testCase2(cache, memory, blockSize, numCacheBlocks, numMemBlocks, gui);
                        } else {
                            this.gui.changeScreen("STEP");
                        }
                        break;
                    case "Test Case 3":
                        if (selectedSimOption.equals("Snapshot")) {
                            numMemBlocks = 2 * numMemBlocks; // 2n
                            cache = new Cache(numMemBlocks, blockSize, numCacheBlocks);
                            memory = new Memory(numMemBlocks, blockSize, numCacheBlocks);
                            this.gui.changeScreen("SNAP");
                            CacheSimulation.testCase3(cache, memory, blockSize, numCacheBlocks, numMemBlocks, gui);
                        } else {
                            this.gui.changeScreen("STEP");
                        }
                        break;
                }
            } catch (NumberFormatException ex) {
                this.gui.getStartScreen().printError();
            }

        }
        if (e.getActionCommand().equals("\u25B6")) {
            this.gui.getSnapScreen().updateCache(cache);
        }
    }
}
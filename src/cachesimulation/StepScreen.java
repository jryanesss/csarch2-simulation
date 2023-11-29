package cachesimulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class StepScreen extends JPanel {
    private JPanel panelNorth = new JPanel();
    private JPanel panelCenter = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private JPanel panelWest = new JPanel(new BorderLayout());
    private JPanel panelEast = new JPanel(new BorderLayout());
    private JPanel panelSouth = new JPanel(new FlowLayout());
    private JButton btnPlay = new JButton("\u25B6");
    private JButton btnOutput = new JButton("OUTPUT");
    private JPanel panelCache = new JPanel();
    private JScrollPane scrollCache;
    private JPanel panelMainMemory = new JPanel();
    private JScrollPane scrollMainMemory;
    private JLabel lblHitMiss = new JLabel();

    public StepScreen() {
        this.setLayout(new BorderLayout());
    }

    public void setStepScreen(Cache cache, Memory memory) {
        // North Panel
        panelNorth.setBackground(Color.decode("#FFFFFF"));
        JLabel lblMain = new JLabel("CACHE SIMULATOR: STEP-BY-STEP");
        panelNorth.add(lblMain);

        // Center Panel
        panelCenter.add(lblHitMiss);

        // South Panel
        panelSouth.setBackground(Color.decode("#FFFFFF"));
        btnPlay.setBackground(Color.decode("#EDEDED"));
        panelSouth.add(btnPlay);
        btnPlay.setBackground(Color.decode("#EDEDED"));

        // West Panel
        panelWest.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        JPanel headerCache = new JPanel();
        JLabel lblCache = new JLabel("<html><div style='text-align: center;'>Cache</div></html>");
        panelCache.setLayout(new BoxLayout(panelCache, BoxLayout.Y_AXIS));
        scrollCache = new JScrollPane(panelCache, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        headerCache.add(lblCache);
        panelWest.add(headerCache, BorderLayout.NORTH);
        panelWest.add(scrollCache, BorderLayout.CENTER);

        // East Panel
        panelEast.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        JPanel headerMainMemory = new JPanel();
        JLabel lblMainMemory = new JLabel("Main Memory");
        panelMainMemory.setLayout(new BoxLayout(panelMainMemory, BoxLayout.Y_AXIS));
        scrollMainMemory = new JScrollPane(panelMainMemory, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        headerMainMemory.add(lblMainMemory);
        panelEast.add(headerMainMemory, BorderLayout.NORTH);
        panelEast.add(scrollMainMemory, BorderLayout.CENTER);

        setCache(cache);
        setMainMemory(memory);

        this.add(panelNorth, BorderLayout.NORTH);
        this.add(panelCenter, BorderLayout.CENTER);
        this.add(panelEast, BorderLayout.EAST);
        this.add(panelWest, BorderLayout.WEST);
        this.add(panelSouth, BorderLayout.SOUTH);

        btnOutput.addActionListener(e -> {
           JOptionPane.showMessageDialog(null,
                    "\nMEMORY ACCESS COUNT: " + cache.memoryAccessCount +
                    "\nCACHE HIT COUNT: " +  cache.cacheHitCount +
                    "\nCACHE MISS COUNT: " + cache.cacheMissCount +
                    "\nCACHE HIT RATE: " + cache.cacheHitRate * 100 + "%" +
                    "\nCACHE MISS RATE: " + cache.cacheMissRate * 100 + "%" +
                    "\nAVERAGE MEMORY ACCESS TIME: " + cache.avgMemoryAccessTime + " ns" +
                    "\nTOTAL MEMORY ACCESS TIME: " + cache.totalMemoryAccessTime + " ns" +
                    "\n\nOutput file can be found in the same folder.",
                    "Output", JOptionPane.PLAIN_MESSAGE);

            try {
                BufferedWriter outputWriter = new BufferedWriter(new FileWriter("simulationOutput.txt"));

                outputWriter.write("MEMORY ACCESS COUNT: " + cache.memoryAccessCount +
                            "\nCACHE HIT COUNT: " +  cache.cacheHitCount +
                            "\nCACHE MISS COUNT: " + cache.cacheMissCount +
                            "\nCACHE HIT RATE: " + cache.cacheHitRate * 100 + "%" +
                            "\nCACHE MISS RATE: " + cache.cacheMissRate * 100 + "%" +
                            "\nAVERAGE MEMORY ACCESS TIME: " + cache.avgMemoryAccessTime + " ns" +
                            "\nTOTAL MEMORY ACCESS TIME: " + cache.totalMemoryAccessTime + " ns");
                outputWriter.close();
            } catch (IOException err) {
                err.printStackTrace();
            }
        });

    }

    public void setCache(Cache c) {
        panelCache.removeAll();
        String color;
        int i = 0;
        String input;
        for (CacheBlock cacheBlock : c.cache) {
            for (int number : cacheBlock.data) {
                if (number == -1) {
                    input = "[EMPTY]";
                } else {
                    input = String.valueOf(number);
                }
                if (i / c.blockSize % 2 == 0) {
                    color = "#E8EEFF";
                } else {
                    color = "#FFFFFF";
                }
                JLabel messageLabel = new JLabel(
                        "<html><p style = \"width: 80px; padding: 2px; background-color:" + color
                                + "\">" + input + "</p></html>");
                messageLabel.setOpaque(true);
                panelCache.add(messageLabel);
                scrollCache.getViewport().setViewPosition(messageLabel.getLocation());
                i++;
            }

        }
        panelCache.revalidate();
        panelCache.repaint();
        panelCache.updateUI();
    }

    public void setMainMemory(Memory m) {
        panelMainMemory.removeAll();
        String color;
        int i = 0;
        String input;
        for (MemoryBlock memBlock : m.memory) {
            for (int number : memBlock.data) {
                if (number == -1) {
                    input = "[EMPTY]";
                } else {
                    input = String.valueOf(number);
                }
                if (i / m.blockSize % 2 == 0) {
                    color = "#E8EEFF";
                } else {
                    color = "#FFFFFF";
                }
                JLabel messageLabel = new JLabel(
                        "<html><p style = \"width: 80px; padding: 2px; background-color:" + color
                                + "\">" + input + "</p></html>");
                messageLabel.setOpaque(true);
                panelMainMemory.add(messageLabel);
                scrollMainMemory.getViewport().setViewPosition(messageLabel.getLocation());
                i++;
            }
        }
        panelMainMemory.revalidate();
        panelMainMemory.repaint();
        panelCache.updateUI();
    }

    public void displayHitOrMiss(boolean is) {
        if (is) {
            lblHitMiss.setText("MISS!");
            lblHitMiss.setForeground(Color.RED);
        } else {
            lblHitMiss.setText("HIT!");
            lblHitMiss.setForeground(Color.GREEN);
        }
    }

    public void highlightCacheBlock(Cache c, int index) {
        panelCache.removeAll();
        String color;
        int i = 0;
        Point scrollTo = new Point(0, 0);
        String input;
        boolean shouldScroll = false;
        for (CacheBlock cacheBlock : c.cache) {
            if (i / c.blockSize % 2 == 0) {
                color = "#E8EEFF";
            } else {
                color = "#FFFFFF";
            }
            if (cacheBlock.tag == index) {
                color = "#FDC898";
                scrollTo = new Point(0, i * 25);
            } else {
                lblHitMiss.setText("");
            }
            for (int number : cacheBlock.data) {
                if (number == -1) {
                    input = "[EMPTY]";
                } else {
                    input = String.valueOf(number);
                }

                JLabel messageLabel = new JLabel(
                        "<html><p style=\"width: 80px; padding: 2px; background-color:" + color
                                + "\">" + input + "</p></html>");
                messageLabel.setOpaque(true);
                panelCache.add(messageLabel);
                i++;

                int labelYPosition = i * 25;

                if (cacheBlock.tag == index && labelYPosition >= scrollCache.getHeight() - 100) {
                    shouldScroll = true;
                }
            }
        }
        panelCache.revalidate();
        panelCache.repaint();
        if (shouldScroll) {
            int scrollPosition = scrollTo.y - (scrollCache.getHeight() - 100);
            scrollCache.getViewport().setViewPosition(new Point(0, Math.max(0, scrollPosition)));
        }
    }

    public void highlightMemBlock(Memory m, int index) {
        panelMainMemory.removeAll();
        String color;
        int i = 0;
        Point scrollTo = new Point(0, 0);
        String input;
        boolean shouldScroll = false;
        for (int blockIndex = 0; blockIndex < m.numMemoryBlocks; blockIndex++) {
            MemoryBlock memBlock = m.memory[blockIndex];
            for (int number : memBlock.data) {
                if (number == -1) {
                    input = "[EMPTY]";
                } else {
                    input = String.valueOf(number);
                }
                if (i / m.blockSize % 2 == 0) {
                    color = "#E8EEFF";
                } else {
                    color = "#FFFFFF";
                }
                if (blockIndex == index) {
                    color = "#FDC898";
                    scrollTo = new Point(0, i * 25);
                }
                JLabel messageLabel = new JLabel(
                        "<html><p style=\"width: 80px; padding: 2px; background-color:" + color
                                + "\">" + input + "</p></html>");
                messageLabel.setOpaque(true);
                panelMainMemory.add(messageLabel);
                i++;
                int labelYPosition = i * 25;
                if (blockIndex == index && color.equals("#FDC898")
                        && labelYPosition >= scrollMainMemory.getHeight() - 100) {
                    shouldScroll = true;
                }
            }
        }
        panelMainMemory.revalidate();
        panelMainMemory.repaint();
        if (shouldScroll) {
            int scrollPosition = scrollTo.y - (scrollMainMemory.getHeight() - 100);
            scrollMainMemory.getViewport().setViewPosition(new Point(0, Math.max(0, scrollPosition)));
        }
    }

    public void showOutputBtn() {
        panelSouth.removeAll();
        panelSouth.add(btnOutput);
        this.revalidate();
        this.repaint();
    }

    public void setStepActionListener(Controller listener) {
        this.btnPlay.addActionListener(listener);
    }

}

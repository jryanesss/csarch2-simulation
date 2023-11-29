package cachesimulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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

public class SnapScreen extends JPanel {

    private JPanel panelNorth = new JPanel();
    private JPanel panelWest = new JPanel(new BorderLayout());
    private JPanel panelEast = new JPanel(new BorderLayout());
    private JPanel panelSouth = new JPanel(new FlowLayout());
    private JButton btnPlay = new JButton("\u25B6");
    private JButton btnOutput = new JButton("OUTPUT");
    private JPanel panelCache = new JPanel();
    private JScrollPane scrollCache;
    private JPanel panelMainMemory = new JPanel();
    private JScrollPane scrollMainMemory;

    public SnapScreen() {
        this.setLayout(new BorderLayout());
    }

    public void setSnapMenu(Cache cache, Memory memory) {
        // North Panel
        panelNorth.setBackground(Color.decode("#FFFFFF"));
        JLabel lblMain = new JLabel("CACHE SIMULATOR: SNAPSHOT");
        panelNorth.add(lblMain);

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
        this.add(panelEast, BorderLayout.EAST);
        this.add(panelWest, BorderLayout.WEST);
        this.add(panelSouth, BorderLayout.SOUTH);

        // btnPlay.addActionListener(e -> {
        // updateCache(cache);
        // });

        btnOutput.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "MEMORY ACCESS COUNT: " + cache.memoryAccessCount +
                    "\nCACHE HIT COUNT: " +  cache.cacheHitCount +
                    "\nCACHE MISS COUNT: " + cache.cacheMissCount +
                    "\nCACHE HIT RATE: " + cache.cacheHitRate * 100 + "%" +
                    "\nCACHE MISS RATE: " + cache.cacheMissRate * 100 + "%" +
                    "\nAVERAGE MEMORY ACCESS TIME: " + cache.avgMemoryAccessTime + " ns" +
                    "\nTOTAL MEMORY ACCESS TIME: " + cache.totalMemoryAccessTime + " ns" +
                    "\n\nOutput file can be found in the same folder.",
                    "Output", JOptionPane.PLAIN_MESSAGE);

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("SimulationOutput.txt")); // TODO: CHANGE FILENAME LATER!!!!!!!!!!!!!!!!!!!1

                writer.write("MEMORY ACCESS COUNT: " + cache.memoryAccessCount +
                            "\nCACHE HIT COUNT: " +  cache.cacheHitCount +
                            "\nCACHE MISS COUNT: " + cache.cacheMissCount +
                            "\nCACHE HIT RATE: " + cache.cacheHitRate * 100 + "%" +
                            "\nCACHE MISS RATE: " + cache.cacheMissRate * 100 + "%" +
                            "\nAVERAGE MEMORY ACCESS TIME: " + cache.avgMemoryAccessTime + " ns" +
                            "\nTOTAL MEMORY ACCESS TIME: " + cache.totalMemoryAccessTime + " ns");
                writer.close();
            } catch (IOException err) {
                err.printStackTrace();
            }
            
        });

    }

    private void setCache(Cache c) {
        panelCache.removeAll();
        String color;
        panelCache.revalidate();
        panelCache.repaint();
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
                this.revalidate();
                scrollCache.getViewport().setViewPosition(messageLabel.getLocation());
                this.repaint();

                i++;
            }

        }
    }

    public void updateCache(Cache c) {
        setCache(c);
        showOutputBtn();
    }

    private void setMainMemory(Memory m) {
        panelMainMemory.removeAll();
        String color;
        panelMainMemory.revalidate();
        panelMainMemory.repaint();
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
                this.revalidate();
                scrollMainMemory.getViewport().setViewPosition(messageLabel.getLocation());
                this.repaint();

                i++;
            }

        }
    }

    private void showOutputBtn() {
        panelSouth.removeAll();
        panelSouth.add(btnOutput);
        this.revalidate();
        this.repaint();
    }

    public void setSnapActionListener(Controller listener) {
        this.btnPlay.addActionListener(listener);
        this.btnOutput.addActionListener(listener);
    }

}

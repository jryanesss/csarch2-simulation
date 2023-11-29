package cachesimulation;

import java.awt.*;
import javax.swing.*;

public class StepScreen extends JPanel {
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

    public StepScreen() {
        this.setLayout(new BorderLayout());
    }

    public void setStepScreen(Cache cache, Memory memory) {
        // North Panel
        panelNorth.setBackground(Color.decode("#FFFFFF"));
        JLabel lblMain = new JLabel("CACHE SIMULATOR: STEP-BY-STEP");
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

        btnOutput.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "MEMORY ACCESS COUNT:\nCACHE HIT COUNT:\nCACHE MISS COUNT:\nCACHE HIT RATE:\nCACHE MISS RATE:\nAVERAGE MEMORY ACCESS TIME:\nTOTAL MEMORY ACCESS TIME:\n\nOutput file can be found in the same folder.",
                    "Output", JOptionPane.PLAIN_MESSAGE);
        });

    }

    public void setCache(Cache c) {
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

    public void setMainMemory(Memory m) {
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

    private void updateCache(Cache c) {
        setCache(c);
        showOutputBtn();
    }

    public void highlightMemBlock(Memory m, int index) {
        panelMainMemory.removeAll();
        String color;
        panelMainMemory.revalidate();
        panelMainMemory.repaint();
        int i = 0;
        Point scrollTo = new Point(0, 0);
        String input;
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
                        "<html><p style = \"width: 80px; padding: 2px; background-color:" + color
                                + "\">" + input + "</p></html>");
                messageLabel.setOpaque(true);
                panelMainMemory.add(messageLabel);
                this.revalidate();
                scrollMainMemory.getViewport().setViewPosition(scrollTo);
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

    public void setStepActionListener(Controller listener) {
        this.btnPlay.addActionListener(listener);
    }

}

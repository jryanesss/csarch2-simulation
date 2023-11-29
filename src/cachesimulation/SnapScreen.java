package cachesimulation;

import java.awt.*;
import javax.swing.*;

public class SnapScreen extends JPanel {

    private JPanel panelNorth = new JPanel();
    private JPanel panelWest = new JPanel(new BorderLayout());
    private JPanel panelEast = new JPanel(new BorderLayout());
    private JPanel panelSouth = new JPanel(new FlowLayout());
    private JButton btnPlay = new JButton("\u25B6");
    private JButton btnOutput = new JButton("OUTPUT");

    public SnapScreen() {
        this.setLayout(new BorderLayout());
        // System.out.println("Snap!");
    }

    public void setSnapMenu(Cache cache, Memory memory) {
        // North Panel
        panelNorth.setBackground(Color.decode("#FFFFFF"));
        JLabel lblMain = new JLabel("CACHE SIMULATOR: SNAPSHOT");
        panelNorth.add(lblMain);

        // South Panel
        panelSouth.setBackground(Color.decode("#FFFFFF"));
        btnPlay = new JButton("\u25B6");
        btnPlay.setBackground(Color.decode("#EDEDED"));
        panelSouth.add(btnPlay);
        btnOutput = new JButton("OUTPUT");
        btnPlay.setBackground(Color.decode("#EDEDED"));

        // West Panel
        panelWest.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        JPanel headerCache = new JPanel();
        // JLabel lblCache = new JLabel("Cache ");
        JLabel lblCache = new JLabel("<html><div style='text-align: center;'>Cache</div></html>");
        JPanel panelCache = new JPanel();
        // panelCache.setPreferredSize(new Dimension(80, 0));
        panelCache.setLayout(new BoxLayout(panelCache, BoxLayout.Y_AXIS));
        JScrollPane scrollCache = new JScrollPane(panelCache, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        headerCache.add(lblCache);
        panelWest.add(headerCache, BorderLayout.NORTH);
        panelWest.add(scrollCache, BorderLayout.CENTER);

        // East Panel

        panelEast.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        JPanel headerMainMemory = new JPanel();
        JLabel lblMainMemory = new JLabel("Main Memory");
        JPanel panelMainMemory = new JPanel();
        panelMainMemory.setLayout(new BoxLayout(panelMainMemory, BoxLayout.Y_AXIS));
        JScrollPane scrollMainMemory = new JScrollPane(panelMainMemory, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        headerMainMemory.add(lblMainMemory);
        panelEast.add(headerMainMemory, BorderLayout.NORTH);
        panelEast.add(scrollMainMemory, BorderLayout.CENTER);

        setCache(panelCache, scrollCache, cache);
        setMainMemory(panelMainMemory, scrollMainMemory, memory);

        this.add(panelNorth, BorderLayout.NORTH);
        this.add(panelEast, BorderLayout.EAST);
        this.add(panelWest, BorderLayout.WEST);
        this.add(panelSouth, BorderLayout.SOUTH);

        btnPlay.addActionListener(e -> {
            updateCache(panelCache, scrollCache, cache);
        });

        btnOutput.addActionListener(e -> {
            JOptionPane.showMessageDialog(null,
                    "MEMORY ACCESS COUNT:\nCACHE HIT COUNT:\nCACHE MISS COUNT:\nCACHE HIT RATE:\nCACHE MISS RATE:\nAVERAGE MEMORY ACCESS TIME:\nTOTAL MEMORY ACCESS TIME:\n\nOutput file can be found in the same folder.",
                    "Output", JOptionPane.PLAIN_MESSAGE);
        });

    }

    private void setCache(JPanel panel, JScrollPane scroll, Cache c) {
        panel.removeAll();
        String color;
        panel.revalidate();
        panel.repaint();
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
                panel.add(messageLabel);
                this.revalidate();
                scroll.getViewport().setViewPosition(messageLabel.getLocation());
                this.repaint();

                i++;
            }

        }
    }

    private void updateCache(JPanel panel, JScrollPane scroll, Cache c) {
        setCache(panel, scroll, c);
        showOutputBtn();
    }

    private void setMainMemory(JPanel panel, JScrollPane scroll, Memory m) {
        panel.removeAll();
        String color;
        panel.revalidate();
        panel.repaint();
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
                panel.add(messageLabel);
                this.revalidate();
                scroll.getViewport().setViewPosition(messageLabel.getLocation());
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

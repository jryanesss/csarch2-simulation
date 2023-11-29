package cachesimulation;

import java.awt.*;
import javax.swing.*;

public class StartScreen extends JPanel {
    private JButton btnSimulate = new JButton("SIMULATE");
    private JComboBox<String> cboxTestCase;
    private JComboBox<String> cboxSimOption;
    private JTextArea taNumMemBlocks;

    public StartScreen() {
        this.setLayout(new BorderLayout());
        setStartMenu();
    }

    public void setStartMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
        panel.setBackground(Color.decode("#E8EEFF"));
        JLabel lbl1 = new JLabel("CACHE SIMULATOR");
        Font font1 = lbl1.getFont();
        lbl1.setFont(new Font(font1.getName(), Font.PLAIN, 30));
        JLabel lbl2 = new JLabel("FA + FIFO");
        lbl2.setFont(new Font(font1.getName(), Font.PLAIN, 30));
        JLabel lbl3 = new JLabel("Number of Cache Blocks = 32 blocks");
        JLabel lbl4 = new JLabel("Cache Line = 16 words");
        JLabel lbl5 = new JLabel("Read Policy: Non-load Through");
        JLabel lbl6 = new JLabel("Number of Memory Blocks");
        JLabel lbl7 = new JLabel("Test Case");
        JLabel lbl8 = new JLabel("Simulation Option");

        String[] testcases = { "Test Case 1", "Test Case 2", "Test Case 3" };
        cboxTestCase = new JComboBox<>(testcases);
        String[] simulation = { "Snapshot", "Step-by-Step" };
        cboxSimOption = new JComboBox<>(simulation);

        taNumMemBlocks = new JTextArea();
        taNumMemBlocks.setMaximumSize(new Dimension(200, 30));

        cboxTestCase.setMaximumSize(new Dimension(200, 30));
        cboxSimOption.setMaximumSize(new Dimension(200, 30));
        cboxTestCase.setBackground(Color.decode("#FFFFFF"));
        cboxSimOption.setBackground(Color.decode("#FFFFFF"));
        taNumMemBlocks.setBorder(BorderFactory.createLineBorder(Color.decode("#7A8A99")));

        btnSimulate.setMaximumSize(new Dimension(200, 30));
        btnSimulate.setBackground(Color.decode("#FFFFFF"));

        lbl1.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl2.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl3.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl4.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl5.setAlignmentX(Component.CENTER_ALIGNMENT);
        taNumMemBlocks.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl6.setAlignmentX(Component.CENTER_ALIGNMENT);
        cboxTestCase.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl7.setAlignmentX(Component.CENTER_ALIGNMENT);
        cboxSimOption.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl7.setAlignmentX(Component.CENTER_ALIGNMENT);
        lbl8.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSimulate.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lbl1);
        panel.add(lbl2);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lbl3);
        panel.add(lbl4);
        panel.add(lbl5);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lbl6);
        panel.add(Box.createVerticalStrut(5));
        panel.add(taNumMemBlocks);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lbl7);
        panel.add(Box.createVerticalStrut(5));
        panel.add(cboxTestCase);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lbl8);
        panel.add(Box.createVerticalStrut(5));
        panel.add(cboxSimOption);
        panel.add(Box.createVerticalStrut(20));
        panel.add(btnSimulate);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
    }

    public void setStartActionListener(Controller listener) {
        this.btnSimulate.addActionListener(listener);
    }

    public JComboBox<String> getTestCaseComboBox() {
        return cboxTestCase;
    }

    public JComboBox<String> getSimOptionComboBox() {
        return cboxSimOption;
    }

    public JTextArea getNumMemBlocksTextArea() {
        return taNumMemBlocks;
    }

    public void printError() {
        JOptionPane.showMessageDialog(null, "Integer value must be entered for number of memory blocks.");
    }
}
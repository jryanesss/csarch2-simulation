package cachesimulation;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {
    // Screens of the game
    private String currentScreen = new String("START");
    private StartScreen startScreen = new StartScreen();
    private SnapScreen snapScreen = new SnapScreen();
    private StepScreen stepScreen = new StepScreen();

    private JPanel cardPanel = new JPanel(new CardLayout());

    public GUI() {
        this.setTitle("Simulation"); // Title of Window
        this.setSize(450, 650);
        this.setLocationRelativeTo(null);
        this.add(cardPanel);
        setupFrame();
        this.changeScreen(currentScreen);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * This function is used to change the screen on display.
     */
    public void changeScreen(String currentScreen) {
        ((CardLayout) this.cardPanel.getLayout()).show(cardPanel, currentScreen);
    }

    private void setupFrame() {
        cardPanel.add(startScreen, "START");
        cardPanel.add(snapScreen, "SNAP");
        cardPanel.add(stepScreen, "STEP");
    }

    /**
     * retrives start screen
     * 
     * @return - start screen
     */
    public StartScreen getStartScreen() {
        return startScreen;
    }

    /**
     * retrives snap screen
     * 
     * @return - snap screen
     */
    public SnapScreen getSnapScreen() {
        return snapScreen;
    }

    /**
     * retrives step screen
     * 
     * @return - step screen
     */
    public StepScreen getStepScreen() {
        return stepScreen;
    }

    /**
     * stores string that indicates current screen
     */
    public void setScreen(String currentScreen) {
        this.currentScreen = currentScreen;
    }

    /**
     * retrieves current screen displayed
     * 
     * @return
     */
    public String getScreen() {
        return this.currentScreen;
    }

    public void setActionListenerToAll(Controller listener) {
        startScreen.setStartActionListener(listener);
        stepScreen.setStepActionListener(listener);
        snapScreen.setSnapActionListener(listener);
    }
}
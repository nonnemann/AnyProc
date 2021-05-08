package executorModule;

import editorModule.Timeslot;
import main.App;
import main.FrameHandler;
import main.ToolSource;

import javax.swing.*;
import java.io.IOException;

import static main.App.globalGraph;

public class ExecutorView extends JFrame{

    private final Executor executorReference;
    
    private JPanel executorFramePanel;
    private JPanel previousIconPanel;
    private JPanel currentIconPanel;
    private JPanel nextIconPanel;

    private JLabel statusLabel;

    private JButton nextButton;
    private JButton backButton;
    private JButton resetProgressButton;
    private JLabel progressionLabel;

    public ExecutorView(Executor executor) {
	    
        // Get Reference
        executorReference = executor;
        
        // Set Styling Rules
        initContentPane();
        previousIconPanel.setName("PreviousIconPanel");
        currentIconPanel.setName("CurrentIconPanel");
        nextIconPanel.setName("NextIconPanel");

        // Enable Buttons
        backButton.addActionListener(e -> {
            try {
                executorReference.moveToPreviousTimeslot();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        nextButton.addActionListener(e -> {
            try {
                executorReference.moveToNextTimeslot();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        resetProgressButton.addActionListener(e -> {
            executorReference.resetProgress();
        });
        
        // Add Frame Handler
        FrameHandler frameHandler = new FrameHandler(this);
        frameHandler.setPositionAtBottomRight();
        frameHandler.setImageIcon();
        
        // Load in Graph
        refreshView();
    }
    
    private void initContentPane()
    {
        setTitle("Toolchain Executor");
        setContentPane(this.executorFramePanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setAlwaysOnTop(true);
        setVisible(true);
    }
    
    public void refreshView()
    {
        clearIconPanels();
        setupIconPanels();
        setupProgressionPanel();
        revalidate();
        repaint();
    }

    private void setupProgressionPanel() {
        // Get Current Stepnumber
        int currentStepNumber = executorReference.getExecutionStepNumber();
        int maxStepNumber = globalGraph.getTimeSlotList().size();

        progressionLabel.setText("<html>Progress:<br>Step " + currentStepNumber + " out of " + maxStepNumber + "</html>");

    }

    private void clearIconPanels() {
        previousIconPanel.removeAll();
        currentIconPanel.removeAll();
        nextIconPanel.removeAll();
    }

    private void setupIconPanels() {
        // Get Current Stepnumber
        int currentStepnumber = executorReference.getExecutionStepNumber();

        setToolIconsForStep(previousIconPanel, currentStepnumber-1);
        setToolIconsForStep(currentIconPanel, currentStepnumber);
        setToolIconsForStep(nextIconPanel, currentStepnumber+1);
    }

    private void setToolIconsForStep(JPanel inputPanel, int number) {
        // Get Timeslot
        Timeslot slotReference = globalGraph.getTimeSlot(number);

        // Find Tools
        if (slotReference != null)
        {
            for (ToolSource toolSource : slotReference.getToolSourceList())
            {
                // Get Icons
                String toolName = toolSource.getSourceName();
                ImageIcon toolIcon = toolSource.getSourceIcon();

                // Add Icons to Label
                JLabel toolIconLabel = new JLabel(toolName, toolIcon, JLabel.CENTER);
                toolIconLabel.setHorizontalTextPosition(JLabel.CENTER);
                toolIconLabel.setVerticalTextPosition(JLabel.BOTTOM);
                inputPanel.add(toolIconLabel);
            }
        }
        else
        {
            App.globalLogger.warning("Can't find ToolIcons in Empty Slot");
        }
    }

    public void startLoadingAnimation()
    {
        statusLabel.setText("<html>Status:<br>Loading</html>");
        statusLabel.setIcon(new ImageIcon("./src/main/resources/videos/loading_animation.gif"));
    }

    public void stopLoadingAnimation()
    {
        statusLabel.setText("<html>Status:<br>Application Ready</html>");
        statusLabel.setIcon(null);
    }
}

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ExecutorView extends JFrame{

    private final Executor executorReference;
    
    private JPanel executorFramePanel;
    private JPanel progressPanel;
    private JLabel numberLabel;
    private JLabel statusLabel;
    private JButton nextButton;
    private JButton backButton;
    private JButton resetProgressButton;

    // TODO @Martin deine Änderungen können in das progressPanel eingebunden werden
    
    public ExecutorView(Executor executor) {
	    
        // Get Reference
        executorReference = executor;
        
        // Set Styling Rules
        initContentPane();
        
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
        numberLabel.setText("Step " + executorReference.getExecutionStepNumber());
        revalidate();
        repaint();
    }

    public void setStatusToLoading()
    {
        statusLabel.setText("Loading");
        statusLabel.setIcon(new ImageIcon("./src/main/resources/videos/loading_animation.gif"));
    }

    public void setStatusToReady()
    {
        statusLabel.setText("Application ready");
        statusLabel.setIcon(null);
    }
}

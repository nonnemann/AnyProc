import javax.swing.*;

public class MainMenuUI extends JFrame {

    private JPanel menuFramePanel;
    private JButton toolchainEditorButton;
    private JButton toolchainExecutorButton;
    private JButton settingsButton;

    public MainMenuUI(String title) {
        this.setName(title);
        initContentPane();
        addFrameHandler();

        // Add Button Action Listener
        toolchainEditorButton.addActionListener( event -> {
            App.createNewEditor();
        });
        toolchainExecutorButton.addActionListener( event -> {
            App.createNewExecutor();
        });
        settingsButton.addActionListener( event -> new SettingsView(App.globalSettings));
    }

    private void initContentPane()
    {
        this.setContentPane(this.menuFramePanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private void addFrameHandler()
    {
        FrameHandler frameHandler = new FrameHandler(this);
        frameHandler.setPositionAtCenter();
        frameHandler.setImageIcon();
    }
}

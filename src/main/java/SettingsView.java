import javax.swing.*;

public class SettingsView extends JFrame {

    private Settings settingsReference;
    private JPanel settingsFramePanel;

    private JLabel fontTypeLabel;
    public JComboBox<String> fontTypeComboBox;

    private JLabel fontSizeLabel;
    public JComboBox<Integer> fontSizeComboBox;

    private JLabel changeEdgeColorLabel;
    private JPanel colorBoxPanel;
    private JButton changeColorButton;

    private JTextField preloadTimeInputField;

    private JButton applyButton;
    private JButton cancelButton;

    public SettingsView(Settings settings) {

        // Get global Settings
        settingsReference = settings;

        // Init Panel
        this.setName("Settings");
        initContentPane();
        addFrameHandler();
    
        // TODO Make colorBoxPanel updatable
        colorBoxPanel.setBackground(settingsReference.getEdgeColor());

        // Add Action Listener
        changeColorButton.addActionListener(buttonPressedEvent -> {
            new ColorPicker();
        });
        
        applyButton.addActionListener(buttonPressedEvent -> {
            settingsReference.setFontFamilyName((String) fontTypeComboBox.getSelectedItem());
            settingsReference.setFontSize((Integer) fontSizeComboBox.getSelectedItem());
            settingsReference.setPreloadTime(Integer.parseInt(preloadTimeInputField.getText()));
            settingsReference.updateView();

            App.globalLogger.info(App.globalSettings.getFontFamilyName() + " " + App.globalSettings.getFontSize());
        });
        
        cancelButton.addActionListener(buttonPressedEvent -> {this.dispose();});
    }

    private void initContentPane()
    {
        this.setContentPane(this.settingsFramePanel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        setFontTypeComboBoxContent();
        setFontSizeComboBoxContent();
    }

    private void setFontTypeComboBoxContent() {

        fontTypeComboBox.setModel(new DefaultComboBoxModel<>(settingsReference.getFontFamilyNames()));
        fontTypeComboBox.setSelectedItem(settingsReference.getFontFamilyName());
    }

    private void setFontSizeComboBoxContent() {

        fontSizeComboBox.setModel(new DefaultComboBoxModel<>(settingsReference.getFontSizes()));
        fontSizeComboBox.setSelectedItem(settingsReference.getFontSize());
    }

    private void addFrameHandler()
    {
        FrameHandler frameHandler = new FrameHandler(this);
        frameHandler.setPositionAtCenter();
        frameHandler.setImageIcon();
    }
}

package settings;

import main.App;
import main.FrameHandler;

import javax.swing.*;

public class ColorPicker extends JFrame
{
	private JPanel colorPickerPanel;
	private JButton cancelButton;
	private JButton applyButton;
	private JColorChooser colorChooser;
	
	public ColorPicker()
	{
		// Init
		setTitle("Color Picker");
		FrameHandler frameHandler = new FrameHandler(this);
		frameHandler.setPositionAtCenter();
		frameHandler.setImageIcon();
		initContentPane();
		
		// Add Listener
		applyButton.addActionListener(actionEvent -> {
			App.globalSettings.setEdgeColor(colorChooser.getColor());
			if(App.debugPrintout)
			{
				App.globalLogger.info("Edgecolor changed to " + App.globalSettings.getEdgeColor());
			}
			dispose();
		});
		cancelButton.addActionListener(actionEvent -> dispose());
	}
	
	private void initContentPane()
	{
		this.setContentPane(this.colorPickerPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
}

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.io.File;

public class FileChooser extends JFileChooser
{
	public FileChooser()
	{
		// Set Styling
		ChangeUIManagerStyling();
		
		// Set Default Directory
		setCurrentDirectory(new File(Settings.DEFAULT_EXPLORER_LOCATION));
	}
	
	private void ChangeUIManagerStyling()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			updateUI();
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
			refreshUI(this, false);
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
	}
	
	public File getFileFromChooser()
	{
		File contentFile = null;
		int returnVal = showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			// Create a LoadFile based on the user input
			contentFile = getSelectedFile();
			App.globalLogger.info("File loaded from " + contentFile);
		}
		else if (returnVal == JFileChooser.CANCEL_OPTION)
		{
			App.globalLogger.info("Process was cancelled by the the user.");
		}
		return contentFile;
	}
	
	public File setFileFromChooser()
	{
		File contentFile = null;
		int returnVal = showSaveDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			// Create a SaveFile based on user input
			contentFile = getSelectedFile();
			App.globalLogger.info("File saved at " + contentFile);
		}
		else if (returnVal == JFileChooser.CANCEL_OPTION)
		{
			App.globalLogger.info("Process was cancelled by the the user.");
		}
		return contentFile;
	}
	
	private static void refreshUI(JComponent c, boolean includeParent)
	{
		if (includeParent)
			c.updateUI();
		
		for (int i = 0; i < c.getComponentCount(); i++)
		{
			Component child = c.getComponent(i);
			if (child instanceof JComponent)
			{
				refreshUI((JComponent)child, true);
			}
		}
	}
}

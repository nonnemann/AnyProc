import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

public class SourceView extends DragPane
{
	private Source relatedSource;
	private JLabel titleLabel;
	
	public SourceView(Source source)
	{
		relatedSource = source;
		
		// Create View
		setPreferredSize(new Dimension(100, 100));
		ImageIcon imageIcon = getImageIcon();
		titleLabel = new JLabel(relatedSource.getSourceName(), imageIcon, JLabel.CENTER);
		titleLabel.setHorizontalTextPosition(JLabel.CENTER);
		titleLabel.setVerticalTextPosition(JLabel.BOTTOM);
		add(titleLabel);
	}

	public ImageIcon getImageIcon()
	{
		ImageIcon sourceIcon = new ImageIcon();
		try
		{
			sun.awt.shell.ShellFolder shellFolder = sun.awt.shell.ShellFolder.getShellFolder(relatedSource.getSourceFile());

			if (shellFolder != null)
			{
				sourceIcon = new ImageIcon(shellFolder.getIcon(true));
			}
			else
			{
				App.globalLogger.severe("No Applicationicon available.");
				sourceIcon = getIconFromType(relatedSource.getSourceType());
			}
		}
		catch (FileNotFoundException exception)
		{
			exception.printStackTrace();
		}
		return setupIcon(sourceIcon);
	}

	private ImageIcon getIconFromType(String type)
	{
		ImageIcon imageIcon = new ImageIcon();
		if(type.equals("tool"))
		{
			imageIcon.setImage(Toolkit.getDefaultToolkit().createImage(Settings.TOOL_ICON_PATH));
		}
		else if(type.equals("data"))
		{
			imageIcon.setImage(Toolkit.getDefaultToolkit().createImage(Settings.DATA_ICON_PATH));
		}
		else
		{
			App.globalLogger.warning("Unknown Type");
		}
		return imageIcon;
	}

	private ImageIcon setupIcon(ImageIcon imageIcon)
	{
		IconTransformer iconTrans = new IconTransformer(imageIcon);
		iconTrans.scaleIcon(60, 60);
		iconTrans.setTransparentBackground();
		return iconTrans.transformToIcon();
	}
	
	// Setter & Getter
	public Source getRelatedSource()
	{
		return relatedSource;
	}
}
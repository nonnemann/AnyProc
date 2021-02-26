import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.*;
import java.util.ArrayList;

public class Settings
{
    private SettingsView settingsView;
	
	public ArrayList<JFrame> frameList = new ArrayList<>();
	
	public static final String DEFAULT_SAVEFILE_LOCATION = System.getProperty("user.dir") + "/savefiles/";
	public static final String DEFAULT_EXPLORER_LOCATION = System.getProperty("user.dir");
	public static final String TEMPORARY_SAVEFILE_PATH = "./out/savestates/CurrentGraph.json";
	public static final String TOOL_ICON_PATH = "./src/main/resources/images/tool.png";
	public static final String DATA_ICON_PATH = "./src/main/resources/images/source.png";
	public static final String PLUS_ICON_PATH = "./src/main/resources/images/plus.png";
	public static final String ERROR_ICON_PATH = "./src/main/resources/images/error.png";
	
	private Color fontColor = new Color(225,225,225);
	private Color edgeColor = new Color(220,220,220);
	private Color buttonColor = new Color(20,20,20);
	private Color backgroundColorDarkGrey = new Color(50,50,50);
	private Color backgroundColorLightGrey = new Color(75,75,75);
	
	private Font font;
	private String[] fontFamilyNames = {""};
	private String fontFamilyName = "Calibri";
	private Integer fontStyle = Font.PLAIN;
	private Integer[] fontFamilySizes = {9, 10, 11, 12, 14, 16, 18, 20};
	private Integer fontSize = 16;

	private int preloadtimeForTools = 3000;
	
	public Settings()
	{
		setFontFamilyNames(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
	}
	
	public void SetUIManagerStyling()
	{
		try
		{
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
			ChangeUIAttributes();
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
	}
	
	private void ChangeUIAttributes()
	{
		UIManager.put("control", backgroundColorDarkGrey);
		UIManager.put("nimbusLightBackground", backgroundColorLightGrey);
		UIManager.put("nimbusBase", backgroundColorLightGrey);
		UIManager.put("Button.background", buttonColor);
		UIManager.put("ComboBox.background", buttonColor);
		UIManager.put("text", fontColor);
		
		UIManager.put("Label.font", font);
		
		UIManager.put("ColorChooser.swatchesRecentSwatchSize", new Dimension(18, 18));
		UIManager.put("ColorChooser.swatchesSwatchSize", new Dimension(18, 18));
	}
	
	public static void setUIFont (javax.swing.plaf.FontUIResource fontUIResource){
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get (key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put (key, fontUIResource);
		}
	}
	
	public void updateView()
	{
		font = new Font(fontFamilyName, fontStyle, fontSize);
		setUIFont (new javax.swing.plaf.FontUIResource(font));
		for(JFrame frame : frameList)
		{
			SwingUtilities.updateComponentTreeUI(frame);
		}
	}
	
	// Setter & Getter
	public SettingsView getSettingsView()
	{
		return settingsView;
	}
	
	public ArrayList<JFrame> getFrameList()
	{
		return frameList;
	}
	
	public String getFontFamilyName()
	{
		return fontFamilyName;
	}
	
	public void setFontFamilyName(String fontFamilyName)
	{
		this.fontFamilyName = fontFamilyName;
	}
	
	public String[] getFontFamilyNames() {
		return fontFamilyNames;
	}
	
	public void setFontFamilyNames(String[] fontFamilyNames)
	{
		this.fontFamilyNames = fontFamilyNames;
	}
	
	public int getFontSize()
	{
		return fontSize;
	}
	
	public void setFontSize(Integer fontSize)
	{
		this.fontSize = fontSize;
	}
	
	public Integer[] getFontSizes() {
		return fontFamilySizes;
	}
	
	public Color getFontColor()
	{
		return fontColor;
	}
	
	public Color getEdgeColor()
	{
		return edgeColor;
	}
	
	public void setEdgeColor(Color edgeColor)
	{
		this.edgeColor = edgeColor;
	}

	public int getPreloadtimeForTools() {
		return preloadtimeForTools;
	}

	public void setPreloadTime(int timeout) {
		this.preloadtimeForTools = timeout;
	}
}

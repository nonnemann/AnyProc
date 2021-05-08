package main;

import main.App;
import main.IconTransformer;
import main.RightClickPopUpListener;
import settings.Settings;

import javax.swing.*;
import java.awt.Toolkit;

public class SourcePanelUI extends JPanel{
    
    private JPanel sourcePanel;
    private JLabel titleLabel;
    
    private String sourceName;
    private String sourceType;
    
    public SourcePanelUI(String name, String type)
    {
        // Copy Data
        this.sourceName = name;
        this.sourceType = type;
        
        // Create View
        titleLabel.setText(sourceName);
        ImageIcon imageIcon = getIconFromType(sourceType);
        titleLabel.setIcon(imageIcon);
    
        // Enable Rightclick Options
        addMouseListener(new RightClickPopUpListener());
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
        IconTransformer iconTrans = new IconTransformer(imageIcon);
        iconTrans.scaleIcon(90, 90);
        iconTrans.setTransparentBackground();
        return iconTrans.transformToIcon();
    }
    
    // Getter & Setter
    public JPanel getSourcePanel()
    {
        return sourcePanel;
    }
}

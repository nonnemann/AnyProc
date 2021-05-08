package editorModule;

import main.App;
import settings.Settings;

import javax.swing.*;
import java.awt.*;

public class ConnectionSlotView extends JPanel {

    private ConnectionSlot referenceModel;

    public ConnectionSlotView(ConnectionSlot connectionSlot)
    {
        referenceModel = connectionSlot;

        //setLayout(new BoxLayout(connectionSlotViewPanel, BoxLayout.Y_AXIS));

        /*ImageIcon imageIcon = new ImageIcon();
        imageIcon.setImage(Toolkit.getDefaultToolkit().createImage(settings.Settings.PLUS_ICON_PATH));
        add(new JLabel("", imageIcon, JLabel.CENTER));*/

        setBorder(BorderFactory.createLineBorder(Color.black));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        ImageIcon imageIcon = new ImageIcon();
        imageIcon.setImage(Toolkit.getDefaultToolkit().createImage(Settings.PLUS_ICON_PATH));
        /*add(new JLabel("", imageIcon, JLabel.CENTER));*/

        JButton addConnectionSpecificationButton = new JButton(imageIcon);
        add(addConnectionSpecificationButton);
    }

    public void addViewToGraph()
    {
        App.editor.getEditorView().getGraphPoolPanel().add(this);
    }

	/*public void addConnectionNodeView(ConnectionSettingsView connectionNodeView)
	{
		add(connectionNodeView);
	}*/
}

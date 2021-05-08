package editorModule;

import main.IconTransformer;
import settings.Settings;

import javax.swing.*;
import java.awt.*;

public class ConnectionOptionButton extends JButton {

    public ConnectionOptionButton() {
        ImageIcon imageIcon = new ImageIcon(Settings.PLUS_ICON_PATH);
        IconTransformer iconTransformer = new IconTransformer(imageIcon);
        iconTransformer.scaleIcon(50,50);
        imageIcon = iconTransformer.transformToIcon();
        this.setIcon(imageIcon);
        this.setPreferredSize(new Dimension(50,50));
    }
}

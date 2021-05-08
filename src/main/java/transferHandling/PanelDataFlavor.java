package transferHandling;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;

public class PanelDataFlavor extends DataFlavor
{
    public static final PanelDataFlavor SHARED_INSTANCE = new PanelDataFlavor();

    private PanelDataFlavor()
    {
        super(JPanel.class, null);
    }
}
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataTransferHandler extends TransferHandler
{
    private JPanel dropArea;

    public DataTransferHandler(JPanel panel)
    {
        dropArea = panel;
    }
    
    @Override
    public boolean canImport(JComponent component, DataFlavor[] transferFlavors)
    {
        return true;
    }
    
    @Override
    public boolean importData(JComponent component, Transferable trans)
    {
        if(trans.isDataFlavorSupported(PanelDataFlavor.javaFileListFlavor))
        {
            createDragPaneFromDrop(component, trans);
        }
        else{
            App.globalLogger.warning("Flavor not supported");
            return false;
        }
        return true;
    }
    
    private void createDragPaneFromDrop(JComponent component, Transferable trans)
    {
        try
        {
            // Import Transfer Data
            List<File> transferData = (List<File>) trans.getTransferData(DataFlavor.javaFileListFlavor);
    
            for (File file : transferData)
            {
                if(dropArea == App.editor.getEditorView().getToolPoolPanel())
                {
                    App.editor.insertToolSource(file);
                }
                else if(dropArea == App.editor.getEditorView().getDataPoolPanel())
                {
                    App.editor.insertDataSource(file);
                }
                else
                {
                    App.globalLogger.warning("Area is not droppable.");
                }
                App.globalLogger.info(file + " imported");
            }
            refreshComponent(component);
        }
        catch(UnsupportedFlavorException | IOException exception)
        {
            exception.printStackTrace();
        }
    }
    
    private void refreshComponent(JComponent component)
    {
        component.revalidate();
        component.repaint();
    }
}

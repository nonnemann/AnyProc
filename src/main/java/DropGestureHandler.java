import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.Serializable;

public class DropGestureHandler implements DropTargetListener, Serializable
{
    private boolean success = false;

    @Override
    public void dragEnter(DropTargetDragEvent dropEvent)
    {
        checkViability(dropEvent);
    }
    
    @Override
    public void dragOver(DropTargetDragEvent dropEvent)
    {
    }
    
    @Override
    public void dropActionChanged(DropTargetDragEvent dropEvent)
    {
    }
    
    @Override
    public void dragExit(DropTargetEvent dropEvent)
    {
    }
    
    @Override
    public void drop(DropTargetDropEvent dropEvent)
    {
        if (dropEvent.isDataFlavorSupported(PanelDataFlavor.SHARED_INSTANCE))
        {
            try
            {
                // What is dropped?
                Transferable transferable = dropEvent.getTransferable();
                Object sourceObject = transferable.getTransferData(PanelDataFlavor.SHARED_INSTANCE);
                
                // Where is it dropped?
                DropTargetContext dropTargetContext = dropEvent.getDropTargetContext();
                Component component = dropTargetContext.getComponent();
                
                // What to do when it is dropped?
                if(componentIsTimeSlot(component))
                {
                    App.globalGraph.addObjectToExistingTimeslot(sourceObject, component);
                }
                else
                {
                    App.globalGraph.addObjectToNewTimeslot(sourceObject);
                }
                
                acceptDrop(dropEvent);
                refresh(App.editor.getEditorView());
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
        else
        {
            rejectDrop(dropEvent);
        }
        dropEvent.dropComplete(success);
    }
    
    private boolean componentIsTimeSlot(Component component)
    {
        if(component.getClass() == Timeslot.class)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    private void refresh(Component comp)
    {
        comp.revalidate();
        comp.repaint();
    }
    
    private void acceptDrop(DropTargetDropEvent dropEvent){
        success = true;
        dropEvent.acceptDrop(DnDConstants.ACTION_COPY);
    }
    
    private void rejectDrop(DropTargetDropEvent dropEvent){
        success = false;
        dropEvent.rejectDrop();
    
        App.globalLogger.severe("Drop rejected: DataFlavor is not supported.");
    }
    
    private void checkViability(DropTargetDragEvent dropEvent)
    {
        if (dropEvent.isDataFlavorSupported(PanelDataFlavor.SHARED_INSTANCE))
        {
            dropEvent.acceptDrag(DnDConstants.ACTION_COPY);
        }
        else
        {
            dropEvent.rejectDrag();
        }
    }
}

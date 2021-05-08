package dragAndDropHandling;

import main.App;
import transferHandling.PanelTransferable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.Serializable;

public class DragGestureHandler implements DragGestureListener, DragSourceListener, Serializable
{
    
    private Container parent;
    private final JPanel child;
    
    public DragGestureHandler(JPanel child)
    {
        this.child = child;
    }
    
    @Override
    public void dragGestureRecognized(DragGestureEvent dragEvent)
    {
        if(!App.edgeDrawMode)
        {
            // Grab reference from parent container
            parent = child.getParent();
            parent.remove(child);
    
            // Update the display
            parent.revalidate();
            parent.repaint();
    
            // Create transferable wrapper
            Transferable transferable = new PanelTransferable(child);
    
            // Start Dragging
            DragSource dragSource = dragEvent.getDragSource();
            dragSource.startDrag(dragEvent, null, transferable, this);
        }
    }
    
    @Override
    public void dragEnter(DragSourceDragEvent dragEvent)
    {
    }
    
    @Override
    public void dragOver(DragSourceDragEvent dragEvent)
    {
    }
    
    @Override
    public void dropActionChanged(DragSourceDragEvent dragEvent)
    {
    }
    
    @Override
    public void dragExit(DragSourceEvent dragEvent)
    {
    }
    
    @Override
    public void dragDropEnd(DragSourceDropEvent dragEvent)
    {
        // Return the component to the previous parent if drop is rejected
        if (!dragEvent.getDropSuccess())
        {
            parent.add(child);
        }
        else
        {
            child.remove(child);
        }
        refresh(parent);
    }
    
    private void refresh(Component comp)
    {
        comp.revalidate();
        comp.repaint();
    }
}

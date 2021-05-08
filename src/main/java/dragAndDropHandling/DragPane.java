package dragAndDropHandling;

import javax.swing.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;

public class DragPane extends JPanel
{
	
	private DragGestureRecognizer _dragGestureRecognizer;
	private DragGestureHandler _dragGestureHandler;
	
	public DragPane()
	{
	}
	
	@Override
	public void addNotify()
	{
		super.addNotify();
		if(_dragGestureRecognizer == null)
		{
			_dragGestureHandler = new DragGestureHandler(this);
			_dragGestureRecognizer = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, _dragGestureHandler);
		}
	}
	
	@Override
	public void removeNotify()
	{
		if(_dragGestureRecognizer != null)
		{
			_dragGestureRecognizer.removeDragGestureListener(_dragGestureHandler);
			_dragGestureHandler = null;
		}
		_dragGestureRecognizer = null;
		super.removeNotify();
	}
}

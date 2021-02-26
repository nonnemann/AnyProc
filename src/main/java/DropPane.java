import javax.swing.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;

public class DropPane extends JPanel
{
	private DropTarget dropTarget;
	private DropGestureHandler dropGestureHandler;
	
	public DropPane() {}
	
	@Override
	public void addNotify()
	{
		super.addNotify();
		dropGestureHandler = new DropGestureHandler();
		dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY, dropGestureHandler, true);
	}
	
	@Override
	public void removeNotify()
	{
		super.removeNotify();
		dropTarget.removeDropTargetListener(dropGestureHandler);
	}
}

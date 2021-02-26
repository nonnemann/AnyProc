import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RightClickPopUpListener implements MouseListener
{
	@Override
	public void mouseClicked(MouseEvent event)
	{
	
	}
	
	public void mousePressed(MouseEvent event) {
		if (event.isPopupTrigger())
		{
			createPopUpMenu(event);
		}
	}
	
	public void mouseReleased(MouseEvent event) {
		if (event.isPopupTrigger())
		{
			createPopUpMenu(event);
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent event)
	{
		if(false)
		{
			App.globalLogger.info(event.getComponent().getClass().getSimpleName());
		}
	}
	
	@Override
	public void mouseExited(MouseEvent event)
	{
	
	}
	
	private void createPopUpMenu(MouseEvent event) {
		RightClickPopUpMenu menu = new RightClickPopUpMenu(event.getComponent());
		menu.show(event.getComponent(), event.getX(), event.getY());
	}
}

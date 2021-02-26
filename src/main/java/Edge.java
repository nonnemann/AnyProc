import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Edge
{
	private transient Component startComponent;
	private transient Component endComponent;

	private Point startPoint;
	private Point startComponentCenter;
	private Point endPoint;
	private Point endComponentCenter;
	
	public Edge(Point startPoint, Point endPoint)
	{
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		
		getStartAndEndComponent();
	}
	
	public void calculateLine()
	{
		if(startComponent != null && endComponent != null)
		{
			startComponentCenter = getRectCenter(getBoundsInWindow(startComponent));
			endComponentCenter = getRectCenter(getBoundsInWindow(endComponent));
		}
		else
		{
			getStartAndEndComponent();
		}
	}

	private void getStartAndEndComponent()
	{
		startComponent = getSourceviewFromComponent(App.editor.getEditorView().findComponentAt(startPoint));
		endComponent = getSourceviewFromComponent(App.editor.getEditorView().findComponentAt(endPoint));
	}
	
	private Point getRectCenter(Rectangle rect)
	{
		return new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
	}
	
	private Rectangle getBoundsInWindow(Component component)
	{
		Point componentLocationOnScreen = component.getLocationOnScreen();
		Point relativeToLocationOnScreen = Objects.requireNonNull(getRootPaneAncestor(component)).getLocationOnScreen();
		Point location = new Point(componentLocationOnScreen.x - relativeToLocationOnScreen.x, componentLocationOnScreen.y - relativeToLocationOnScreen.y);
		return new Rectangle(location, component.getSize());
	}
	
	private JRootPane getRootPaneAncestor(Component component)
	{
		for (Container parent = component.getParent(); parent != null; parent = parent.getParent())
		{
			if (parent instanceof JRootPane)
			{
				return (JRootPane) parent;
			}
		}
		return null;
	}
	
	private SourceViewInSlot getSourceviewFromComponent(Component component)
	{
		if(component instanceof SourceViewInSlot)
		{
			return (SourceViewInSlot) component;
		}
		else if(component.getParent() instanceof SourceViewInSlot)
		{
			return (SourceViewInSlot) component.getParent();
		}
		else
		{
			App.globalLogger.warning("Connecting Nodes failed. Component is not Part of a SourceView.");
		}
		return null;
	}
	
	public void addToEdgeList()
	{
		if(!isEdgeInEdgelist())
		{
			App.globalGraph.getEdgeList().add(this);
			App.editor.getEditorView().refreshView();
		}
	}
	
	private boolean isEdgeInEdgelist()
	{
		for(Edge edgeInList : App.globalGraph.getEdgeList())
		{
			if(edgeInList.getStartComponentCenter() == this.startComponentCenter && edgeInList.getEndComponentCenter() == this.endComponentCenter)
			{
				return true;
			}
		}
		return false;
	}
	// Setter & Getter
	public Component getStartComponent()
	{
		return startComponent;
	}
	
	public Component getEndComponent()
	{
		return endComponent;
	}
	
	public Point getStartComponentCenter()
	{
		return startComponentCenter;
	}
	
	public Point getEndComponentCenter()
	{
		return endComponentCenter;
	}
}

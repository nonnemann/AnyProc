package editorModule;

import main.App;
import main.DataSource;
import main.Source;
import main.ToolSource;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Edge
{
	private Timeslot surroundingTimslot;

	private Component startComponent;
	private Component endComponent;

	private Point startPoint;
	private Point startComponentCenter;
	private Point endPoint;
	private Point endComponentCenter;
	
	public Edge(Point startPoint, Point endPoint)
	{
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		
		getStartAndEndComponent();

		App.globalLogger.info("Line between " + startComponent.getClass() + " and " + endComponent.getClass() + " drawn.");
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
		startComponent = App.editor.getEditorView().findComponentAt(startPoint);
		endComponent = App.editor.getEditorView().findComponentAt(endPoint);
	}
	
	private Point getRectCenter(Rectangle rect)
	{
		return new Point(rect.x + rect.width / 2 - 80, rect.y + rect.height / 2);
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

	public boolean isWithinTimeslot() {
		// Check Parents for Source Class
		Source startSource = findSurroundingSource(startComponent);
		Source endSource = findSurroundingSource(endComponent);

		if (startSource.getRelatedTimeslot() == endSource.getRelatedTimeslot())
		{
			surroundingTimslot = startSource.getRelatedTimeslot();
			return true;
		}
		else
		{
			return false;
		}
	}

	private Source findSurroundingSource(Component jComponent) {
		Component parent = jComponent;
		boolean check = false;
		while(!check)
		{
			parent = parent.getParent();
			if (parent instanceof ToolSource || parent instanceof DataSource)
			{
				check = true;
			}
		}
		return (Source) parent;
	}
	
	public boolean hasSurroundingToolsource(Component jComponent)
	{
		Component parent = jComponent;
		boolean check = false;
		while(!check)
		{
			parent = parent.getParent();
			if (parent instanceof ToolSource)
			{
				check = true;
			}
		}
		return check;
	}

	public ToolSource getSurroundingToolsource(Component jComponent)
	{
		Component parent = jComponent;
		boolean check = false;
		while(!check)
		{
			parent = parent.getParent();
			if (parent instanceof ToolSource)
			{
				check = true;
			}
		}
		return (ToolSource) parent;
	}

	public boolean hasSurroundingDatasource(Component jComponent)
	{
		Component parent = jComponent;
		boolean check = false;
		while(!check)
		{
			parent = parent.getParent();
			if (parent instanceof DataSource)
			{
				check = true;
			}
		}
		return check;
	}

	public DataSource getSurroundingDatasource(Component jComponent)
	{
		Component parent = jComponent;
		boolean check = false;
		while(!check)
		{
			parent = parent.getParent();
			if (parent instanceof DataSource)
			{
				check = true;
			}
		}
		return (DataSource) parent;
	}

	public Timeslot getSurroundingTimeslot() {
		return surroundingTimslot;
	}

	public Source getStartSource() {
		return findSurroundingSource(startComponent);
	}

	public Source getEndSource() {
		return findSurroundingSource(endComponent);
	}


	public DataSource getDataSource() {
		return null;
	}
}

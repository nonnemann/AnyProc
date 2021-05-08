package main;

import editorModule.Timeslot;
import transferHandling.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.io.File;

class RightClickPopUpMenu extends JPopupMenu
{
	private JMenuItem addToolSourceButton;
	private JMenuItem deleteToolSourceButton;
	
	private JMenuItem changeSlotNumberButton;
	private JMenuItem deleteTimeSlotButton;
	private JMenuItem deleteSlotelementButton;
	
	private JMenuItem addLocalDataSourceButton;
	private JMenuItem deleteDataSourceButton;
	private JMenuItem addWebDataSourceButton;
	
	public RightClickPopUpMenu(Component component) {
		
		if(isInToolPool(component))
		{
			addInsertToolsourceOption(component);
			addDeleteToolsourceOption(component);
		}
		else if(isInGraphPool(component))
		{
			changeSlotNumberButton = new JMenuItem("Change Position");
			changeSlotNumberButton.addActionListener(event -> {
				// TODO Überarbeiten da noch nicht Funktionstüchtig
				openSlotInformationWindow(component);
			});
			add(changeSlotNumberButton);
			changeSlotNumberButton.setEnabled(true);
			
			deleteTimeSlotButton = new JMenuItem("Delete TimeSlot");
			deleteTimeSlotButton.addActionListener(event -> {
				if(component instanceof Timeslot)
				{
					App.globalGraph.removeTimeslot((Timeslot) component);
				}
				else
				{
					App.globalLogger.severe("Deletion aborted. Component is not a Timeslot.");
				}
			});
			add(deleteTimeSlotButton);
			deleteTimeSlotButton.setEnabled(false);
			
			deleteSlotelementButton = new JMenuItem("Delete Element");
			deleteSlotelementButton.addActionListener(event -> {
				if(component instanceof ToolSource)
				{
					ToolSource toolSource = (ToolSource) component;
					App.globalGraph.removeToolsource(toolSource);
				}
				else if(component instanceof DataSource)
				{
					DataSource dataSource = (DataSource) component;
					App.globalGraph.removeDatasource(dataSource);
				}
				else
				{
					App.globalLogger.severe("Deletion aborted. Component is not a Sourceelement.");
				}
			});
			add(deleteSlotelementButton);
			deleteSlotelementButton.setEnabled(true);
			
			if(component instanceof Timeslot)
			{
				changeSlotNumberButton.setEnabled(true);
				deleteTimeSlotButton.setEnabled(true);
			}
			
			if(component instanceof Source)
			{
				deleteSlotelementButton.setEnabled(true);
			}
		}
		else if(isInDataPool(component))
		{
			addInsertDatasourceOption(component);
			addDeleteDatasourceOption(component);
		}
		else
		{
			App.globalLogger.warning("Rightclick Menu could not be create. Position out of Bounds");
		}
	}
	
	private void addInsertDatasourceOption(Component jComponent)
	{
		addLocalDataSourceButton = new JMenuItem("Add local Datasource");
		addLocalDataSourceButton.addActionListener(event -> {
			FileChooser fileChooser = new FileChooser();
			File importFile = fileChooser.getFileFromChooser();
			if(importFile != null)
			{
				App.editor.insertDataSource(importFile);
			}
			else
			{
				App.globalLogger.warning("File without content");
			}
		});
		add(addLocalDataSourceButton);
		addLocalDataSourceButton.setEnabled(true);
	}
	
	private void addDeleteDatasourceOption(Component jComponent)
	{
		deleteDataSourceButton = new JMenuItem("Delete Datasource");
		deleteDataSourceButton.addActionListener(event -> {
			if(jComponent instanceof DataSource)
			{
				App.editor.removeDatasourceFromDatalpanel((Source) jComponent);
			}
			else
			{
				App.globalLogger.severe("Component is not a Source.");
			}
		});
		add(deleteDataSourceButton);
		deleteDataSourceButton.setEnabled(false);
		if(jComponent instanceof DataSource)
		{
			deleteDataSourceButton.setEnabled(true);
		}
	}
	
	private void addInsertToolsourceOption(Component jComponent)
	{
		addToolSourceButton = new JMenuItem("Add Source");
		addToolSourceButton.addActionListener(event -> {
			FileChooser fileChooser = new FileChooser();
			File importFile = fileChooser.getFileFromChooser();
			if(importFile != null)
			{
				App.editor.insertToolSource(importFile);
			}
			else
			{
				App.globalLogger.warning("File without Content");
			}
		});
		add(addToolSourceButton);
		addToolSourceButton.setEnabled(true);
	}
	
	private void addDeleteToolsourceOption(Component jComponent)
	{
		deleteToolSourceButton = new JMenuItem("Delete Source");
		deleteToolSourceButton.addActionListener(event -> {
			if(jComponent instanceof ToolSource)
			{
				App.editor.removeToolsourceFromToolpanel((Source) jComponent);
			}
			else
			{
				App.globalLogger.severe("Component is not a Source.");
			}
		});
		add(deleteToolSourceButton);
		deleteToolSourceButton.setEnabled(false);
		
		
		if(jComponent instanceof ToolSource)
		{
			deleteToolSourceButton.setEnabled(true);
		}
	}
	
	private boolean isInToolPool(Component jComponent)
	{
		Component parent = jComponent;
		boolean check = false;
		while(!check && parent != App.editor.getEditorView())
		{
			parent = parent.getParent();
			if (parent == App.editor.getEditorView().getToolPoolPanel())
			{
				check = true;
			}
		}
		return check;
	}
	
	private boolean isInGraphPool(Component jComponent)
	{
		Component parent = jComponent;
		boolean check = false;
		while(!check && parent != App.editor.getEditorView())
		{
			parent = parent.getParent();
			if (parent == App.editor.getEditorView().getGraphPoolPanel())
			{
				check = true;
			}
			else
			{
				check = false;
			}
		}
		return check;
	}
	
	private boolean isInDataPool(Component jComponent)
	{
		Component parent = jComponent;
		boolean check = false;
		while(!check && parent != App.editor.getEditorView())
		{
			parent = parent.getParent();
			if (parent == App.editor.getEditorView().getDataPoolPanel())
			{
				check = true;
			}
			else
			{
				check = false;
			}
		}
		return check;
	}
	
	private void openSlotInformationWindow(Component component)
	{
		// TODO Überarbeiten da noch nicht Funktionstüchtig
		// Get Possible Positions
		Timeslot timeslot = (Timeslot) component;
		Integer[] possibilities = new Integer[App.globalGraph.getTimeSlotList().size()];
		for (int possiblePositions = 0; possiblePositions < App.globalGraph.getTimeSlotList().size()-1; possiblePositions++) {
			possibilities[possiblePositions] = possiblePositions + 1;
		}

		// Create Message
		Integer selectedNumber = (Integer) JOptionPane.showInputDialog(new JFrame(),"Change Slotnumber to:\n", "Change Position", JOptionPane.PLAIN_MESSAGE, null, possibilities, "ham");

		// Change Slot Position
		if(selectedNumber != null)
		{
			App.globalGraph.changePosition(timeslot, selectedNumber);
		}
		else
		{
			App.globalLogger.warning("Operation aborted. No number was selected.");
		}
	}
}

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
		
		if(component == App.editor.getEditorView().getToolPoolPanel() || component instanceof SourceView && component.getParent().getName().equals("Tool Pool"))
		{
			addToolSourceButton = new JMenuItem("Add Toolsource");
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
			
			deleteToolSourceButton = new JMenuItem("Delete Toolsource");
			deleteToolSourceButton.addActionListener(event -> {
				if(component instanceof SourceView)
				{
					App.editor.removeToolsourceFromToolpanel((SourceView) component);
				}
				else
				{
					App.globalLogger.severe("Component is not a SourceView.");
				}
			});
			add(deleteToolSourceButton);
			deleteToolSourceButton.setEnabled(false);
			
			
			if(component instanceof SourceView)
			{
				deleteToolSourceButton.setEnabled(true);
			}
		}
		else if(component == App.editor.getEditorView().getGraphPoolPanel() || component instanceof Timeslot || component instanceof SourceViewInSlot)
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
				if(component instanceof SourceViewInSlot)
				{
					SourceViewInSlot sourceViewInSlot = (SourceViewInSlot) component;
					App.globalGraph.removeSourceInSlot(sourceViewInSlot.getRelatedSource());
				}
				else
				{
					System.out.println(component.getName());
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
			
			if(component instanceof SourceViewInSlot)
			{
				deleteSlotelementButton.setEnabled(true);
			}
		}
		else if(component == App.editor.getEditorView().getDataPoolPanel() || component instanceof SourceView && component.getParent().getName().equals("Data Pool"))
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
			
			deleteDataSourceButton = new JMenuItem("Delete Datasource");
			deleteDataSourceButton.addActionListener(event -> {
				if(component instanceof SourceView)
				{
					App.editor.removeDatasourceFromDatalpanel((SourceView) component);
				}
				else
				{
					App.globalLogger.severe("Component is not a SourceView.");
				}
			});
			add(deleteDataSourceButton);
			deleteDataSourceButton.setEnabled(false);

			addWebDataSourceButton = new JMenuItem("Add WebDataSource");
			addWebDataSourceButton.addActionListener(event -> {
				//App.revizeConnector.dostuff();
			});
			add(addWebDataSourceButton);
			addWebDataSourceButton.setEnabled(true);
			
			if(component instanceof SourceView)
			{
				deleteDataSourceButton.setEnabled(true);
			}
		}
		else
		{
			App.globalLogger.warning("No Rightclickmenu registered.");
		}
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

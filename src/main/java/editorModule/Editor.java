package editorModule;

import main.DataSource;
import main.Source;
import main.ToolSource;

import java.io.File;

public class Editor
{
	private EditorView editorView;

	public Editor()
	{
		editorView = new EditorView(this);
	}
	
	public void insertToolSource(File importFile)
	{
		// Add Import
		ToolSource source = new ToolSource(importFile);
		editorView.getToolPoolPanel().add(source);
		
		// Update View
		editorView.removeToolPoolLabel();
		editorView.revalidate();
		editorView.repaint();
	}
	
	public void removeToolsourceFromToolpanel(Source Source)
	{
		editorView.getToolPoolPanel().remove(Source);
		editorView.addToolPoolLabel();
		editorView.revalidate();
		editorView.repaint();
	}
	
	public void insertDataSource(File importFile)
	{
		// Add Import
		DataSource dataSource = new DataSource(importFile);
		editorView.getDataPoolPanel().add(dataSource);
		
		// Update View
		editorView.removeDataPoolLabel();
		editorView.revalidate();
		editorView.repaint();
	}
	
	public void removeDatasourceFromDatalpanel(Source Source)
	{
		editorView.getDataPoolPanel().remove(Source);
		editorView.addDataPoolLabel();
		editorView.revalidate();
		editorView.repaint();
	}
	
	// Setter & Getter
	public EditorView getEditorView()
	{
		return editorView;
	}
}

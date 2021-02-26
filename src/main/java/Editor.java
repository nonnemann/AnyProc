import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Editor
{
	private EditorView editorView;
	private List<SourceModel> toolSourceList;
	private List<SourceModel> dataSourceList;

	public Editor()
	{
		toolSourceList = new ArrayList<>();
		dataSourceList = new ArrayList<>();
		
		editorView = new EditorView(this);
	}
	
	public void insertToolSource(File importFile)
	{
		// Add Import
		Source toolSource = new Source(importFile, "tool");
		editorView.getToolPoolPanel().add(toolSource.getSourceView());
		
		// Update View
		editorView.checkForToolPoolLabel();
		editorView.revalidate();
		editorView.repaint();
	}
	
	public void removeToolsourceFromToolpanel(SourceView sourceView)
	{
		getToolSourceList().remove(sourceView.getRelatedSource().getSourceModel());
		editorView.getToolPoolPanel().remove(sourceView);
		editorView.checkForToolPoolLabel();
		editorView.revalidate();
		editorView.repaint();
	}
	
	public void insertDataSource(File importFile)
	{
		// Add Import
		Source dataSource = new Source(importFile, "data");
		editorView.getDataPoolPanel().add(dataSource.getSourceView());
		
		// Update View
		editorView.checkForDataPoolLabel();
		editorView.revalidate();
		editorView.repaint();
	}
	
	public void removeDatasourceFromDatalpanel(SourceView sourceView)
	{
		getDataSourceList().remove(sourceView.getRelatedSource().getSourceModel());
		editorView.getDataPoolPanel().remove(sourceView);
		editorView.checkForDataPoolLabel();
		editorView.revalidate();
		editorView.repaint();
	}
	
	// Setter & Getter
	public EditorView getEditorView()
	{
		return editorView;
	}
	
	public List<SourceModel> getToolSourceList()
	{
		return toolSourceList;
	}
	
	public List<SourceModel> getDataSourceList() {
		return dataSourceList;
	}
}

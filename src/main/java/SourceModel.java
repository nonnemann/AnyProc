import datatype.ElementPosition;

import java.io.Serializable;

public class SourceModel implements Serializable
{
	private String name;
	private String filePath;
	private ElementPosition<Integer, Integer> position;
	
	public SourceModel(Source source)
	{
		name = source.getSourceName();
		filePath = source.getSourceFile().getPath();
		position = new ElementPosition<>(0,0);
		
		if(source.getSourceType() == "tool")
		{
			App.editor.getToolSourceList().add(this);
		}
		else if(source.getSourceType() == "data")
		{
			App.editor.getDataSourceList().add(this);
		}
		else
		{
			App.globalLogger.severe("Unknown Source Type.");
		}
	}
	
	// Setter & Getter
	
	public String getName()
	{
		return name;
	}
	
	public String getFilePath()
	{
		return filePath;
	}
	
	public ElementPosition<Integer, Integer> getPosition()
	{
		return position;
	}
}

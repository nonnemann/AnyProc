package transferHandling;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class ColumnNameConverter extends Transformation
{
	private final String NEW_COLUMN_NAME;
	
	public ColumnNameConverter(String correspondingColumn, String newColumnName)
	{
		super(correspondingColumn);
		NEW_COLUMN_NAME = newColumnName;
	}
	
	@Override
	public void transformColumn(File inputFile)
	{
		if(noChangeNeeded())
			return;
		
		try
		{
			List<String> lines = Files.readAllLines(inputFile.toPath());
			String lineWithColumns = lines.get(0);
			
			if(alreadyTransformed(lineWithColumns))
				return;
			
			lineWithColumns = lineWithColumns.replace(_correspondingColumn, NEW_COLUMN_NAME);
			lines.set(0, lineWithColumns);
			writeFile(inputFile, lines);
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	@Override
	public boolean alreadyTransformed(String lineWithColumns)
	{
		return !lineWithColumns.contains(_correspondingColumn);
	}
	
	private boolean noChangeNeeded()
	{
		return _correspondingColumn.equals(NEW_COLUMN_NAME);
	}
}

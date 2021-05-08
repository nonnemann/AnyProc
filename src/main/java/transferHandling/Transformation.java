package transferHandling;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public abstract class Transformation
{
	protected String _correspondingColumn;
	
	public Transformation(String correspondingColumn)
	{
		_correspondingColumn = correspondingColumn;
	}
	
	public abstract void transformColumn(File inputFile);
	public abstract boolean alreadyTransformed(String lineToTest);
	
	protected final List<String> readFile(File inputFile)
	{
		try
		{
			return Files.readAllLines(inputFile.toPath());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	protected final void writeFile(File inputFile, List<String> changes)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile, false));
			for(String line : changes)
				writer.write(line + System.lineSeparator());
			writer.close();
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
}

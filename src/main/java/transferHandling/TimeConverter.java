package transferHandling;

import java.io.File;
import java.util.List;

public class TimeConverter extends Transformation
{
	public TimeConverter(String correspondingColumn)
	{
		super(correspondingColumn);
	}
	
	// Regardless of selection, hh:mm:ss will be converted to hh:mm
	@Override
	public void transformColumn(File inputFile)
	{
		List<String> lines = readFile(inputFile);
		
		if(alreadyTransformed(lines.get(1))) return;
		
		for(int i = 1; i < lines.size(); i++)
		{
			// cut out the seconds by ignoring them in the substring commands
			String timePart = lines.get(i).substring(0, lines.get(i).indexOf(":") + 3);
			String restPart = lines.get(i).substring(lines.get(i).indexOf(":") + 6);
			lines.set(i, timePart + restPart);
		}
		writeFile(inputFile, lines);
	}
	
	// Test for format, if length == 2 then hh:mm can be assumed
	@Override
	public boolean alreadyTransformed(String lineToTest)
	{
		return lineToTest.split(":").length == 2;
	}
}

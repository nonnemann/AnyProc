package settings;

import transferHandling.Transformation;

import java.io.File;
import java.util.List;

public class ColorConverter extends Transformation
{
	public ColorConverter(String correspondingColumn)
	{
		super(correspondingColumn);
	}
	
	// Regardless of selection, RGB will be converted to HEX
	@Override
	public void transformColumn(File inputFile)
	{
		List<String> lines = readFile(inputFile);
		
		if(alreadyTransformed(lines.get(1)))
			return;
		
		for(int i = 1; i < lines.size(); i++)
		{
			String[] splittedLine = lines.get(i).split("\"");
			if(noColorData(splittedLine)) continue;
			
			String rgbPart = splittedLine[3];
			String[] rgbValues = rgbPart.split(",");
			String hex = String.format("#%02x%02x%02x", Integer.parseInt(rgbValues[0]), Integer.parseInt(rgbValues[1]), Integer.parseInt(rgbValues[2]));
			String newLine = splittedLine[0] + hex + splittedLine[6];
			lines.set(i, newLine);
		}
		
		writeFile(inputFile, lines);
	}
	
	// Because this class only converts from RGB to HEX, already occurred transformation can be assumed by the presence of a "#" in the color field
	@Override
	public boolean alreadyTransformed(String lineToCheck)
	{
		return lineToCheck.contains("#");
	}
	
	// RGB-values are encoded like ""="(R,G,B)"" in Excel --> if the string doesn't get split with regex "\"" then it does not contain RGB values
	private boolean noColorData(String[] splittedLine)
	{
		return splittedLine.length == 1;
	}
}

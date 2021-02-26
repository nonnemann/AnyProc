import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PlantAtHandDeltaQuantity implements Quantity
{
	@Override
	public TransportData getData(ExchangeProcess ep)
	{
		List<String> parameters = readParameters(ep);
		String startTime = null, endTime = null;
		
		if(parameters != null)
		{
			startTime = parameters.get(0);
			endTime = parameters.get(1);
		}
		
		File modifiedPart = getModifiedPartOfData(startTime, endTime, ep);
		
		return new TransportData(null, modifiedPart);
	}
	
	private List<String> readParameters(ExchangeProcess ep)
	{
		try
		{
			TransportData parameterData = ep.getAction().getParameter();
			return Files.readAllLines(parameterData.getParameters().toPath());
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
		return null;
	}
	
	private File getModifiedPartOfData(String startTime, String endTime, ExchangeProcess ep)
	{
		try
		{
			// Need to grab data of destination (in the scenario case excel) because P@H has no reference to the csv data
			File dataOfTool = App.globalGraph.findDataOfTool(ep.getDestination()).getSourceFile();
			List<String> data = Files.readAllLines(dataOfTool.toPath());
			
			int startIndex = 0, endIndex = 0;
			
			for(String line : data)
			{
				if(line.contains(startTime))
					startIndex = data.indexOf(line);
				
				if(line.contains(endTime))
					endIndex = data.indexOf(line) + 1;
			}
			
			List<String> modifiedPart = new ArrayList<>();
			modifiedPart.add(data.get(0));
			modifiedPart.addAll(data.subList(startIndex, endIndex));
			
			String newFileName = FilenameGenerator.addCustomString(dataOfTool, "_modified");
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(newFileName));
			for(String line : modifiedPart)
				writer.write(line + System.lineSeparator());
			writer.close();
			
			return new File(newFileName);
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
		return null;
	}
}

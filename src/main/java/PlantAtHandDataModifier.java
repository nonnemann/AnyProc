import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PlantAtHandDataModifier implements Reuse
{
	private final static String[] ACTIVITIES = new String[] {"Sleep", "Rest", "Active", "Walk", "Run"};
	private final static int START_INDEX = 145;
	private final static int END_INDEX = 5876;
	private File _visualizationFile;
	
	@Override
	public void reuseTransportData(ExchangeProcess ep)
	{
		try
		{
			String visualizationFileName = ep.getDestination().getSourceFile().toPath().getParent().toString();
			visualizationFileName += "/Plant@Hand3D_Data/StreamingAssets/hospital/anomaliedaten/IGD_DEMO_WEB/activity.html";
			_visualizationFile = new File(visualizationFileName);
			
			List<String> newActivityData = overrideActivity(getModifiedData(ep), getBaseData());
			
			overrideFile(newActivityData);
			
			Desktop.getDesktop().open(ep.getDestination().getSourceFile());
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}
	
	private List<String> getModifiedData(ExchangeProcess ep) throws IOException
	{
		return Files.readAllLines(ep.getTransportData().getData().toPath());
	}
	
	private List<String> getBaseData() throws IOException
	{
		return Files.readAllLines(_visualizationFile.toPath());
	}
	
	private List<String> overrideActivity(List<String> dataActivity, List<String> chartActivity)
	{
		List<String> newChartActivity = new ArrayList<>();
		
		newChartActivity = addFirstPart(chartActivity, newChartActivity);
		newChartActivity = addModifiedData(dataActivity, newChartActivity);
		newChartActivity = addLastPart(chartActivity, newChartActivity);
		return newChartActivity;
	}
	
	private List<String> addFirstPart(List<String> chartActivity, List<String> newChartActivity)
	{
		newChartActivity.addAll(chartActivity.subList(0, START_INDEX));
		return newChartActivity;
	}
	
	private List<String> addModifiedData(List<String> dataActivity, List<String> newChartActivity)
	{
		for(int index = 1; index < dataActivity.size(); index++)
		{
			// "-1" includes empty values in a given line
			String[] csvData = dataActivity.get(index).split(",", -1);
			StringBuilder newDataItem = new StringBuilder();
			newDataItem.append("{\n")
					   .append("\"Time\":\"")
					   .append(csvData[0])
					   .append("\",\n");
			
			// Only one column has data
			int indexWithData = -1;
			for(int j = 2; j < 7; j++)
			{
				if(csvData[j].equals(""))
					continue;
				indexWithData = j;
				break;
			}
			if(indexWithData != -1)
			{
				newDataItem.append("\"color\":\"")
						   .append(csvData[1])
						   .append("\",\n")
						   .append("\"")
						   .append(ACTIVITIES[indexWithData - 2])
						   .append("\":\"")
						   .append(csvData[indexWithData])
						   .append("\"");
			}
			newDataItem.append("},\n");
			newChartActivity.add(newDataItem.toString());
		}
		return newChartActivity;
	}
	
	private List<String> addLastPart(List<String> chartActivity, List<String> newChartActivity)
	{
		newChartActivity.addAll(chartActivity.subList(END_INDEX, chartActivity.size()));
		return newChartActivity;
	}
	
	private void overrideFile(List<String> newChartActivity) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(_visualizationFile));
		
		for(String line : newChartActivity)
			writer.write(line + System.lineSeparator());
		
		writer.close();
	}
}

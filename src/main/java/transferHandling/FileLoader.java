package transferHandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader extends FileChooser
{
	/*public transferHandling.FileLoader()
	{
		
		// Set Default Directory
		setCurrentDirectory(new File(settings.Settings.DEFAULT_SAVEFILE_LOCATION));
		setFileFilter(new FileNameExtensionFilter(".json", "json"));
		loadGraphFromChoosenPath();
	}*/
	
	/*public void loadGraphFromChoosenPath()
	{
		// Load File from Location
		File loadFile = getFileFromChooser();
		String jsonFile = readStringFromFile(loadFile);
		
		// Transform JSON to Graph
		Gson gson = new Gson();
		main.App.globalGraph = gson.fromJson(jsonFile, main.GlobalGraph.class);
		main.App.globalGraph.loadViewFromModel();
	}*/
	
	public String readStringFromFile(File loadFile)
	{
		StringBuilder jsonFile = new StringBuilder();
		String line;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(loadFile));
			while((line = reader.readLine()) != null) {
				jsonFile.append(line);
			}
			reader.close();
		}
		catch (IOException exception) {
			exception.printStackTrace();
		}
		return String.valueOf(jsonFile);
	}
}

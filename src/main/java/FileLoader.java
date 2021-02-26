import com.google.gson.Gson;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader extends FileChooser
{
	/*public FileLoader()
	{
		
		// Set Default Directory
		setCurrentDirectory(new File(Settings.DEFAULT_SAVEFILE_LOCATION));
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
		App.globalGraph = gson.fromJson(jsonFile, GlobalGraph.class);
		App.globalGraph.loadViewFromModel();
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

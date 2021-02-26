import com.google.gson.Gson;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FileSaver extends FileChooser
{
	public FileSaver()
	{
		// Set Default Directory
		setCurrentDirectory(new File(Settings.DEFAULT_SAVEFILE_LOCATION));
		setFileFilter(new FileNameExtensionFilter(".json", "json"));
		saveGraphToChoosenPath();
	}
	
	private void saveGraphToChoosenPath()
	{
		// Create a Savefile
		File saveFile = setFileFromChooser();
		saveFile = createExtensionIfMissing(saveFile);
		
		// Transform Graph to JSON
		App.globalGraph.setGraphName(saveFile.getName());
		Gson gson = new Gson();
		String jsonFile = gson.toJson(App.globalGraph);
		
		// Save File to Location
		writeStringToFile(jsonFile, saveFile);
	}
	
	private File createExtensionIfMissing(File file)
	{
		String filePath = file.getAbsolutePath();
		if(!filePath.endsWith(".json")) {
			file = new File(filePath + ".json");
		}
		return file;
	}
	
	public void writeStringToFile(String stringContent, File file)
	{
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getPath()));
			bufferedWriter.write(stringContent);
			bufferedWriter.close();
			
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}

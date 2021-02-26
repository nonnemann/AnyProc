import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ColumnExtractor
{
	// CSV Format is assumed
	public static List<String> extractColumnNames(ConnectionSettings node)
	{
		try
		{
			SourceInSlot data = App.globalGraph.findDataOfTool(node);
			BufferedReader reader = new BufferedReader(new FileReader(data.getSourceFile()));
			String columnNames = reader.readLine();
			return Arrays.asList(columnNames.split(","));
		}
		catch(NullPointerException | FileNotFoundException npe)
		{
			npe.printStackTrace();
			App.globalLogger.severe("No Data Source defined");
			return null;
		}
		catch(IOException exception)
		{
			exception.printStackTrace();
			return null;
		}
	}
}

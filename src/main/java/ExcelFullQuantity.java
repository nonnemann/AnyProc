import java.io.File;

public class ExcelFullQuantity implements Quantity
{
	@Override
	public TransportData getData(ExchangeProcess ep)
	{
		return new TransportData(null, getLastModifiedActivityData(ep));
	}
	
	private File getLastModifiedActivityData(ExchangeProcess ep)
	{
		File activityData = App.globalGraph.findDataOfTool(ep.getOrigin()).getSourceFile();
		String newFileName = FilenameGenerator.addCustomString(activityData, "_modified");
   		File modifiedActivityData = new File(newFileName);
		
		long activityDataLastModified = activityData.lastModified(); // Don't need to test if exists because lastModified return 0L if it doesn't
		long modifiedActivityDataLastModified = modifiedActivityData.lastModified();
		
		if(modifiedActivityDataLastModified >= activityDataLastModified) return modifiedActivityData;
		return activityData;
	}
}

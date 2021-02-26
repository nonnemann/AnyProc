import java.io.File;

public class ExcelSorting extends Action
{
	public ExcelSorting(File file)
	{
		super(file);
	}
	
	@Override
	public TransportData getParameter()
	{
		// Does not have any parameters
		return new TransportData(null, null);
	}
}

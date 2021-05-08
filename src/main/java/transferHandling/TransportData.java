package transferHandling;

import java.io.File;

public class TransportData
{
	private final File _parameters;
	private final File _data;
	
	public TransportData(File parameters, File data)
	{
		_parameters = parameters;
		_data = data;
	}
	public File getParameters()
	{
		return _parameters;
	}
	public File getData()
	{
		return _data;
	}
}

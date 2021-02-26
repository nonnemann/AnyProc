import java.io.File;

abstract class Action
{
	protected final File _fileToCheck;
	
	public Action(File file)
	{
		_fileToCheck = file;
	}
	
	public boolean wasTaken()
	{
		return _fileToCheck.exists();
	}
	
	public TransportData getParameter()
	{
		return new TransportData(_fileToCheck, null);
	}
}
